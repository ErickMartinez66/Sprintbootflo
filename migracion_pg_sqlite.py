import sys
import sqlite3
from decimal import Decimal
from datetime import date, datetime

import psycopg2

PG = dict(host="localhost", port=5432, dbname="floreria",
          user="postgres", password="Blanca23*")
SQLITE_PATH = r"D:\Sprint\data\floreria.db"

TABLES = ["cliente", "viaje", "arreglo", "mes",
          "inversion_total", "pedido", "producto", "costo_c"]


def convert(value):
    if value is None:
        return None
    if isinstance(value, bool):
        return 1 if value else 0
    if isinstance(value, Decimal):
        return float(value)
    if isinstance(value, datetime):
        return value.strftime("%Y-%m-%d %H:%M:%S.") + f"{value.microsecond // 1000:03d}"
    if isinstance(value, date):
        return value.strftime("%Y-%m-%d 00:00:00.000")
    if isinstance(value, (bytes, bytearray)):
        return sqlite3.Binary(value)
    return value


def main():
    pg = psycopg2.connect(**PG)
    pcur = pg.cursor()
    sq = sqlite3.connect(SQLITE_PATH)
    scur = sq.cursor()

    pcur.execute(
        "select table_name from information_schema.tables "
        "where table_schema='public' order by table_name")
    pg_tables = {r[0] for r in pcur.fetchall()}

    sq.execute("PRAGMA foreign_keys=OFF")
    sq.execute("BEGIN")

    ok = True
    for table in TABLES:
        if table not in pg_tables:
            print(f"  [SKIP] {table}: no existe en Postgres")
            continue

        pcur.execute(
            "select column_name from information_schema.columns "
            "where table_schema='public' and table_name=%s "
            "order by ordinal_position", (table,))
        cols = [r[0] for r in pcur.fetchall()]
        col_list = ", ".join(f'"{c}"' for c in cols)
        placeholders = ", ".join("?" for _ in cols)

        pcur.execute(f'select {col_list} from "{table}" order by 1')
        rows = [[convert(v) for v in row] for row in pcur.fetchall()]

        scur.execute(f'DELETE FROM "{table}"')
        if rows:
            scur.executemany(
                f'insert into "{table}" ({col_list}) values ({placeholders})',
                rows)

        scur.execute(f'select count(*) from "{table}"')
        dest = scur.fetchone()[0]
        status = "OK" if dest == len(rows) else "MISMATCH"
        if dest != len(rows):
            ok = False
        print(f"  [{status}] {table}: origen={len(rows)} destino={dest}")

    sq.commit()
    sq.execute("PRAGMA foreign_keys=ON")
    pg.close()
    sq.close()
    print("Migracion completada" if ok else "Migracion con discrepancias")
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
