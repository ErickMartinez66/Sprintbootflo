#!/usr/bin/env python3
"""Migra la base SQLite local a PostgreSQL (Neon).

Uso:
    python scripts/migrate_sqlite_to_neon.py --replace
    python scripts/migrate_sqlite_to_neon.py --database-url "postgresql://..."

Variables de entorno:
    SOURCE_SQLITE   Ruta al archivo SQLite (default: data/floreria.db)
    DATABASE_URL    URL de conexion PostgreSQL/Neon
"""

from __future__ import annotations

import argparse
import os
import sqlite3
import sys
from datetime import date, datetime, timezone
from pathlib import Path
from urllib.parse import urlparse

try:
    import psycopg2
    from psycopg2.extras import execute_values
except ImportError:
    sys.exit("Falta psycopg2. Instala con: pip install psycopg2-binary")


TABLES = [
    ("accesorio", ["id", "nombre", "precio"]),
    ("color_flor", ["id", "activo", "hex", "nombre", "orden", "valor"]),
    ("cliente", ["id", "direccion", "nombre", "numero"]),
    ("viaje", ["id", "fecha_creacion", "inversion"]),
    ("arreglo", [
        "id", "activo", "cantidad_flores", "color_flores", "descripcion", "emoji",
        "gradiente", "imagen_url", "nombre", "orden", "precio_venta",
    ]),
    ("arreglo_accesorio", ["arreglo_id", "accesorio_id"]),
    ("pedido", [
        "id", "cantidad_flores", "fecha_entrega", "precio_ganancia", "precio_inversion",
        "precio_venta", "cliente_id", "colores", "estado", "inversion_fija_aplicada",
    ]),
    ("pedido_accesorio", ["pedido_id", "accesorio_id"]),
    ("costo_c", [
        "id", "cilicona", "hoja", "hojam", "lazo", "mariposa", "otro", "palos",
        "petalos", "pedido_id",
    ]),
    ("producto", ["id", "cantidad", "costo_por_unidad", "nombre", "viaje_id"]),
    ("mes", ["id", "anio", "cantidad_pedidos", "ganancia_total", "mes"]),
    ("inversion_total", ["id", "inversion", "inversion_dolares", "inversion_fija"]),
]

IDENTITY_TABLES = [
    "accesorio", "arreglo", "cliente", "color_flor", "costo_c", "pedido", "producto", "viaje",
]

BOOLEAN_COLUMNS = {
    ("arreglo", "activo"),
    ("color_flor", "activo"),
    ("pedido", "inversion_fija_aplicada"),
}

DATE_MILLIS_COLUMNS = {
    ("pedido", "fecha_entrega"),
    ("viaje", "fecha_creacion"),
}


def quote(identifier: str) -> str:
    return '"' + identifier.replace('"', '""') + '"'


def convert_value(table: str, column: str, value):
    if value is None:
        return None

    if (table, column) in BOOLEAN_COLUMNS:
        if isinstance(value, bool):
            return value
        if isinstance(value, (int, float)):
            return value != 0
        return str(value).strip().lower() in {"1", "true", "t", "yes", "si", "sí"}

    if (table, column) in DATE_MILLIS_COLUMNS:
        if isinstance(value, datetime):
            return value.date()
        if isinstance(value, date):
            return value
        if isinstance(value, (int, float)):
            return datetime.fromtimestamp(value / 1000, tz=timezone.utc).date()
        text = str(value).strip()
        if not text:
            return None
        if text.isdigit():
            return datetime.fromtimestamp(int(text) / 1000, tz=timezone.utc).date()
        return date.fromisoformat(text[:10])

    return value


def validate_source(source: sqlite3.Connection) -> None:
    existing = {
        row[0]
        for row in source.execute(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'"
        )
    }
    missing_tables = [table for table, _ in TABLES if table not in existing]
    if missing_tables:
        raise RuntimeError(f"Faltan tablas en SQLite: {', '.join(missing_tables)}")

    for table, columns in TABLES:
        current = {row[1] for row in source.execute(f"PRAGMA table_info({quote(table)})")}
        missing_columns = [column for column in columns if column not in current]
        if missing_columns:
            raise RuntimeError(
                f"Faltan columnas en SQLite.{table}: {', '.join(missing_columns)}"
            )


def validate_destination(destination) -> None:
    with destination.cursor() as cursor:
        for table, columns in TABLES:
            cursor.execute(
                """
                SELECT column_name
                FROM information_schema.columns
                WHERE table_schema = 'public' AND table_name = %s
                """,
                (table,),
            )
            current = {row[0] for row in cursor.fetchall()}
            if not current:
                raise RuntimeError(
                    f"La tabla public.{table} no existe. Arranca el backend una vez "
                    "con JPA_DDL_AUTO=update antes de migrar."
                )
            missing_columns = [column for column in columns if column not in current]
            if missing_columns:
                raise RuntimeError(
                    f"Faltan columnas en PostgreSQL.{table}: {', '.join(missing_columns)}"
                )


def truncate_destination(destination) -> None:
    tables = ", ".join(quote(table) for table, _ in TABLES)
    with destination.cursor() as cursor:
        cursor.execute(f"TRUNCATE TABLE {tables} RESTART IDENTITY CASCADE")


def copy_table(source: sqlite3.Connection, destination, table: str, columns: list[str]) -> int:
    select_columns = ", ".join(quote(column) for column in columns)
    rows = source.execute(f"SELECT {select_columns} FROM {quote(table)}").fetchall()
    converted = [
        tuple(convert_value(table, column, value) for column, value in zip(columns, row))
        for row in rows
    ]

    if not converted:
        print(f"  {table}: 0 filas")
        return 0

    insert_columns = ", ".join(quote(column) for column in columns)
    sql = (
        f"INSERT INTO {quote(table)} ({insert_columns}) VALUES %s "
        "ON CONFLICT DO NOTHING"
    )
    with destination.cursor() as cursor:
        execute_values(cursor, sql, converted, page_size=500)
    print(f"  {table}: {len(converted)} filas")
    return len(converted)


def reset_sequences(destination) -> None:
    with destination.cursor() as cursor:
        for table in IDENTITY_TABLES:
            cursor.execute("SELECT pg_get_serial_sequence(%s, 'id')", (table,))
            row = cursor.fetchone()
            sequence = row[0] if row else None
            if not sequence:
                print(f"  {table}: sin secuencia (se omite)")
                continue
            cursor.execute(
                f"""
                SELECT setval(
                    %s,
                    COALESCE((SELECT MAX({quote('id')}) FROM {quote(table)}), 1),
                    EXISTS (SELECT 1 FROM {quote(table)})
                )
                """,
                (sequence,),
            )
            print(f"  {table}: secuencia {sequence} sincronizada")


def verify_counts(source: sqlite3.Connection, destination) -> None:
    with destination.cursor() as cursor:
        for table, _ in TABLES:
            source_count = source.execute(f"SELECT COUNT(*) FROM {quote(table)}").fetchone()[0]
            cursor.execute(f"SELECT COUNT(*) FROM {quote(table)}")
            destination_count = cursor.fetchone()[0]
            print(f"  {table}: SQLite={source_count} PostgreSQL={destination_count}")


def main() -> int:
    parser = argparse.ArgumentParser(description="Migra SQLite local a Neon PostgreSQL.")
    parser.add_argument(
        "--source",
        default=os.getenv("SOURCE_SQLITE", "data/floreria.db"),
        help="Ruta al archivo SQLite (default: data/floreria.db)",
    )
    parser.add_argument(
        "--database-url",
        default=os.getenv("DATABASE_URL"),
        help="URL PostgreSQL/Neon; tambien puede venir en DATABASE_URL",
    )
    parser.add_argument(
        "--replace",
        action="store_true",
        help="Vacia las tablas destino antes de copiar (recomendado en la primera migracion)",
    )
    args = parser.parse_args()

    if not args.database_url:
        parser.error("Falta --database-url o la variable DATABASE_URL")

    source_path = Path(args.source).resolve()
    if not source_path.is_file():
        parser.error(f"No existe el archivo SQLite: {source_path}")

    parsed = urlparse(args.database_url)
    print(f"Origen: {source_path}")
    print(f"Destino: {parsed.hostname or '(host oculto)'}{parsed.path}")

    source = sqlite3.connect(source_path.as_uri() + "?mode=ro", uri=True)
    try:
        validate_source(source)
        destination = psycopg2.connect(args.database_url)
        try:
            validate_destination(destination)
            with destination:
                if args.replace:
                    print("Vaciando tablas destino...")
                    truncate_destination(destination)

                print("Copiando datos...")
                for table, columns in TABLES:
                    copy_table(source, destination, table, columns)

                print("Sincronizando secuencias...")
                reset_sequences(destination)

            print("Verificando conteos...")
            verify_counts(source, destination)
        finally:
            destination.close()
    finally:
        source.close()

    print("Migracion completada. Cambia JPA_DDL_AUTO a validate y reinicia el backend.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
