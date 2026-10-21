import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Package, Save, Trash2 } from 'lucide-react';
import {
  actualizarArreglo,
  crearArreglo,
  eliminarArreglo,
  listarArreglos,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import COLORES from '../data/colores.js';
import DataState from './DataState.jsx';

const GRADIENTE_POR_DEFECTO = 'linear-gradient(160deg, #faead6, #efbe8f)';
const NOMBRE_POR_DEFECTO = 'Arreglo personalizado';

function precioPorFlorDe(arreglo) {
  const flores = Number(arreglo?.cantidadFlores) || 0;
  const precio = Number(arreglo?.precioVenta) || 0;
  return flores > 0 ? Math.round(precio / flores) : precio;
}

function ConfigForm({ arreglo, onGuardar, guardando }) {
  const [nombre, setNombre] = useState(arreglo?.nombre ?? NOMBRE_POR_DEFECTO);
  const [precioPorFlor, setPrecioPorFlor] = useState(precioPorFlorDe(arreglo));
  const [cantidadFlores, setCantidadFlores] = useState(arreglo?.cantidadFlores ?? 24);
  const [colorFlores, setColorFlores] = useState(arreglo?.colorFlores ?? 'rosa');
  const [accesorios] = useState(arreglo?.accesorios ?? '');
  const [emoji, setEmoji] = useState(arreglo?.emoji ?? '💐');
  const [gradiente, setGradiente] = useState(arreglo?.gradiente ?? GRADIENTE_POR_DEFECTO);

  const colores = COLORES.some((c) => c.valor === colorFlores)
    ? COLORES
    : [...COLORES, { valor: colorFlores, etiqueta: `${colorFlores} (actual)` }];

  const enviar = (e) => {
    e.preventDefault();
    const flores = Math.max(1, Number(cantidadFlores) || 1);
    onGuardar({
      nombre: nombre.trim() || NOMBRE_POR_DEFECTO,
      descripcion: arreglo?.descripcion ?? '',
      precioVenta: Math.round((Number(precioPorFlor) || 0) * flores),
      cantidadFlores: flores,
      colorFlores,
      accesorios: accesorios.trim(),
      emoji: emoji.trim(),
      gradiente: gradiente.trim() || GRADIENTE_POR_DEFECTO,
      imagenUrl: arreglo?.imagenUrl ?? '',
      activo: true,
      orden: 0,
    });
  };

  return (
    <form className="admin-form" onSubmit={enviar}>
      <div className="campo">
        <label htmlFor="configNombre">Nombre del arreglo</label>
        <input
          id="configNombre"
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          placeholder="Ej: Arreglo personalizado"
          required
        />
      </div>

      <div className="admin-form-fila">
        <div className="campo">
          <label htmlFor="configPrecioFlor">Precio por flor (pesos)</label>
          <input
            id="configPrecioFlor"
            type="number"
            min="0"
            step="1"
            value={precioPorFlor}
            onChange={(e) => setPrecioPorFlor(e.target.value)}
            required
          />
          <small className="admin-ayuda">
            El total se calcula multiplicando el precio por flor por la cantidad
            de flores.
          </small>
        </div>

        <div className="campo">
          <label htmlFor="configFlores">Cantidad de flores por defecto</label>
          <input
            id="configFlores"
            type="number"
            min="1"
            value={cantidadFlores}
            onChange={(e) => setCantidadFlores(e.target.value)}
            required
          />
          <small className="admin-ayuda">
            Valor inicial que verá el cliente en el formulario.
          </small>
        </div>
      </div>

      <div className="admin-form-fila">
        <div className="campo">
          <label htmlFor="configColor">Color de las flores por defecto</label>
          <select
            id="configColor"
            value={colorFlores}
            onChange={(e) => setColorFlores(e.target.value)}
          >
            {colores.map((c) => (
              <option key={c.valor} value={c.valor}>
                {c.etiqueta}
              </option>
            ))}
          </select>
        </div>

        <div className="campo">
          <label>Accesorios por defecto</label>
          <Link
            to="/admin/accesorios"
            className="btn btn-secundario admin-btn-xs"
            style={{ alignSelf: 'flex-start' }}
          >
            <Package size={14} aria-hidden="true" /> Gestionar accesorios
          </Link>
        </div>
      </div>

      <div className="admin-form-seccion">Apariencia del formulario</div>

      <div className="admin-form-fila">
        <div className="campo">
          <label htmlFor="configEmoji">Icono (emoji)</label>
          <input
            id="configEmoji"
            type="text"
            value={emoji}
            onChange={(e) => setEmoji(e.target.value)}
            placeholder="💐"
          />
          <small className="admin-ayuda">
            Se convierte en un icono en el banner del formulario.
          </small>
        </div>

        <div className="campo">
          <label htmlFor="configGradiente">Degradado (CSS)</label>
          <input
            id="configGradiente"
            type="text"
            value={gradiente}
            onChange={(e) => setGradiente(e.target.value)}
          />
        </div>
      </div>

      <div className="admin-form-acciones">
        <button type="submit" className="btn btn-primario" disabled={guardando}>
          <Save size={16} aria-hidden="true" />
          {guardando ? 'Guardando…' : 'Guardar configuración'}
        </button>
      </div>
    </form>
  );
}

function ArreglosPage() {
  const [arreglos, setArreglos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [guardando, setGuardando] = useState(false);
  const [exito, setExito] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      setArreglos(await listarArreglos());
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, []);

  useEffect(() => {
    cargar();
  }, [cargar]);

  useAosRefresh(cargando);

  const config = arreglos[0] ?? null;
  const duplicados = arreglos.slice(1);

  const guardar = async (datos) => {
    setGuardando(true);
    setError('');
    setExito(false);
    try {
      if (config) {
        await actualizarArreglo(config.id, datos);
      } else {
        await crearArreglo(datos);
      }
      await cargar();
      setExito(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  };

  const limpiarDuplicados = async () => {
    if (
      !window.confirm(
        `Se eliminarán ${duplicados.length} configuraciones antiguas. Esta acción no se puede deshacer.`
      )
    ) {
      return;
    }
    setError('');
    try {
      for (const arreglo of duplicados) {
        await eliminarArreglo(arreglo.id);
      }
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="admin-panel">
      <div className="admin-cabecera" data-aos="fade-up">
        <div>
          <h1 className="admin-titulo">Formulario de pedido</h1>
          <p className="admin-subtitulo">
            Configura los valores por defecto del formulario único que el
            cliente ve en la tienda.
          </p>
        </div>
      </div>

      {duplicados.length > 0 && (
        <div className="admin-resumen" data-aos="fade-up" data-aos-delay="40">
          <div>
            <strong>Configuraciones antiguas detectadas</strong>
            <p className="admin-subtitulo">
              Se encontraron {duplicados.length} registros anteriores. Se usa el
              primero como configuración activa.
            </p>
          </div>
          <button
            type="button"
            className="btn admin-btn-xs admin-btn-peligro"
            onClick={limpiarDuplicados}
          >
            <Trash2 size={14} aria-hidden="true" /> Limpiar duplicados
          </button>
        </div>
      )}

      <DataState cargando={cargando} vacio={false}>
        <div className="admin-config" data-aos="fade-up" data-aos-delay="80">
          {error && <p className="admin-error">{error}</p>}
          {exito && (
            <p className="admin-ok">Configuración guardada correctamente.</p>
          )}
          <ConfigForm
            key={config?.id ?? 'nuevo'}
            arreglo={config}
            guardando={guardando}
            onGuardar={guardar}
          />
        </div>
      </DataState>
    </div>
  );
}

export default ArreglosPage;
