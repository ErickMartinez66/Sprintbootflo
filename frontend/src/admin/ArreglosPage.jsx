import { useCallback, useEffect, useMemo, useState } from 'react';
import { Package, Plus, Save, Trash2 } from 'lucide-react';
import {
  actualizarColor,
  crearColor,
  eliminarColor,
  listarColores,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import AccesoriosManager from './AccesoriosManager.jsx';
import DataState from './DataState.jsx';
import FormModal from './FormModal.jsx';
import { guardarFormConfig, obtenerFormConfig } from '../data/formConfig.js';

const GRADIENTE_POR_DEFECTO = 'linear-gradient(160deg, #faead6, #efbe8f)';
const NOMBRE_POR_DEFECTO = 'Arreglo personalizado';

function precioPorFlorDe(arreglo) {
  const flores = Number(arreglo?.cantidadFlores) || 0;
  const precio = Number(arreglo?.precioVenta) || 0;
  return flores > 0 ? Math.round(precio / flores) : precio;
}

function ColorForm({ color, onGuardar, onCancelar }) {
  const [nombre, setNombre] = useState(color?.nombre ?? '');
  const [hex, setHex] = useState(color?.hex ?? '#f4a7c3');
  const [activo, setActivo] = useState(color?.activo ?? true);
  const [orden, setOrden] = useState(color?.orden ?? 0);

  const hexValido = /^#[0-9a-fA-F]{6}$/.test(hex);

  const enviar = (e) => {
    e.preventDefault();
    onGuardar({ nombre, hex, activo, orden: Number(orden) || 0 });
  };

  return (
    <form className="admin-form" onSubmit={enviar}>
      <div className="campo">
        <label htmlFor="colorNombre">Nombre del color</label>
        <input
          id="colorNombre"
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          placeholder="Ej: Rosa claro"
          required
        />
      </div>

      <div className="admin-form-fila">
        <div className="campo">
          <label htmlFor="colorHex">Color</label>
          <div className="admin-color-picker">
            <input
              id="colorHex"
              type="color"
              value={hexValido ? hex : '#000000'}
              onChange={(e) => setHex(e.target.value)}
            />
            <input
              type="text"
              value={hex}
              onChange={(e) => setHex(e.target.value)}
              placeholder="#f4a7c3"
              aria-label="Código hexadecimal"
              required
            />
          </div>
        </div>

        <div className="campo">
          <label htmlFor="colorOrden">Orden</label>
          <input
            id="colorOrden"
            type="number"
            value={orden}
            onChange={(e) => setOrden(e.target.value)}
          />
          <small className="admin-ayuda">Menor número aparece primero.</small>
        </div>
      </div>

      <label className="admin-check">
        <input
          type="checkbox"
          checked={activo}
          onChange={(e) => setActivo(e.target.checked)}
        />
        Mostrar en el formulario
      </label>

      <div className="admin-form-acciones">
        <button type="button" className="btn btn-secundario" onClick={onCancelar}>
          Cancelar
        </button>
        <button type="submit" className="btn btn-primario">
          Guardar color
        </button>
      </div>
    </form>
  );
}

function ConfigForm({ arreglo, colores, onGuardar, guardando }) {
  const [nombre, setNombre] = useState(arreglo?.nombre ?? NOMBRE_POR_DEFECTO);
  const [precioPorFlor, setPrecioPorFlor] = useState(precioPorFlorDe(arreglo));
  const [cantidadFlores, setCantidadFlores] = useState(arreglo?.cantidadFlores ?? 24);
  const [colorFlores, setColorFlores] = useState(
    colores.some((c) => c.valor === arreglo?.colorFlores)
      ? arreglo.colorFlores
      : (colores[0]?.valor ?? '')
  );
  const [accesorios] = useState(arreglo?.accesorios ?? '');
  const [emoji, setEmoji] = useState(arreglo?.emoji ?? '💐');
  const [gradiente, setGradiente] = useState(arreglo?.gradiente ?? GRADIENTE_POR_DEFECTO);

  useEffect(() => {
    setNombre(arreglo?.nombre ?? NOMBRE_POR_DEFECTO);
    setPrecioPorFlor(precioPorFlorDe(arreglo));
    setCantidadFlores(arreglo?.cantidadFlores ?? 24);
    setColorFlores(
      colores.some((c) => c.valor === arreglo?.colorFlores)
        ? arreglo.colorFlores
        : (colores[0]?.valor ?? '')
    );
    setEmoji(arreglo?.emoji ?? '💐');
    setGradiente(arreglo?.gradiente ?? GRADIENTE_POR_DEFECTO);
  }, [
    arreglo?.id,
    arreglo?.nombre,
    arreglo?.precioVenta,
    arreglo?.cantidadFlores,
    arreglo?.colorFlores,
    arreglo?.emoji,
    arreglo?.gradiente,
    colores,
  ]);

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
            disabled={colores.length === 0}
          >
            {colores.length === 0 && (
              <option value="">Sin colores disponibles</option>
            )}
            {colores.map((c) => (
              <option key={c.valor} value={c.valor}>
                {c.nombre}
              </option>
            ))}
          </select>
          {colores.length === 0 && (
            <small className="admin-ayuda">
              Crea un color en "Colores del formulario" para poder elegirlo aquí.
            </small>
          )}
        </div>

        <div className="campo">
          <label>Accesorios por defecto</label>
          <button
            type="button"
            className="btn btn-secundario admin-btn-xs"
            style={{ alignSelf: 'flex-start' }}
            onClick={() =>
              document
                .getElementById('accesorios-formulario')
                ?.scrollIntoView({ behavior: 'smooth', block: 'start' })
            }
          >
            <Package size={14} aria-hidden="true" /> Gestionar accesorios
          </button>
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
  const [config, setConfig] = useState(null);
  const [colores, setColores] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [cargandoColores, setCargandoColores] = useState(true);
  const [error, setError] = useState('');
  const [errorColores, setErrorColores] = useState('');
  const [guardando, setGuardando] = useState(false);
  const [exito, setExito] = useState(false);
  const [colorModal, setColorModal] = useState(null);
  const [guardandoColor, setGuardandoColor] = useState(false);

  const cargar = useCallback(() => {
    try {
      setCargando(true);
      setError('');
      setConfig(obtenerFormConfig());
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, []);

  const cargarColores = useCallback(async () => {
    try {
      setCargandoColores(true);
      setErrorColores('');
      setColores(await listarColores());
    } catch (err) {
      setErrorColores(err.message);
    } finally {
      setCargandoColores(false);
    }
  }, []);

  useEffect(() => {
    cargar();
    cargarColores();
  }, [cargar, cargarColores]);

  useAosRefresh(cargando || cargandoColores);

  const coloresActivos = useMemo(
    () => colores.filter((color) => color.activo !== false),
    [colores]
  );

  const guardar = async (datos) => {
    setGuardando(true);
    setError('');
    setExito(false);
    try {
      const actualizado = guardarFormConfig({
        ...(config || {}),
        ...datos,
      });
      setConfig(actualizado);
      setExito(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  };

  const guardarColor = async (datos) => {
    setGuardandoColor(true);
    setErrorColores('');
    try {
      if (colorModal === 'crear') {
        await crearColor(datos);
      } else {
        await actualizarColor(colorModal.color.id, datos);
      }
      setColorModal(null);
      await cargarColores();
    } catch (err) {
      setErrorColores(err.message);
    } finally {
      setGuardandoColor(false);
    }
  };

  const alternarColor = async (color) => {
    setErrorColores('');
    try {
      await actualizarColor(color.id, {
        nombre: color.nombre,
        hex: color.hex,
        activo: !color.activo,
        orden: color.orden,
      });
      await cargarColores();
    } catch (err) {
      setErrorColores(err.message);
    }
  };

  const eliminarColorFlor = async (color) => {
    if (!window.confirm(`¿Eliminar el color "${color.nombre}"?`)) {
      return;
    }
    setErrorColores('');
    try {
      await eliminarColor(color.id);
      await cargarColores();
    } catch (err) {
      setErrorColores(err.message);
    }
  };

  return (
    <div className="admin-panel">
      <div className="admin-cabecera" data-aos="fade-up">
        <div>
          <h1 className="admin-titulo">Formulario de pedido</h1>
          <p className="admin-subtitulo">
            Configura los valores por defecto del formulario único que el
            cliente ve en la tienda. Los cambios se guardan localmente en este navegador.
          </p>
        </div>
      </div>

      <DataState cargando={cargando} vacio={false}>
        <div className="admin-config" data-aos="fade-up" data-aos-delay="80">
          {error && <p className="admin-error">{error}</p>}
          {exito && (
            <p className="admin-ok">Configuración guardada correctamente.</p>
          )}
          <ConfigForm
            key={
              config
                ? `${config.id}-${config.nombre}-${config.precioVenta}-${config.cantidadFlores}-${config.colorFlores}-${config.emoji}-${config.gradiente}`
                : 'nuevo'
            }
            arreglo={config}
            colores={coloresActivos}
            guardando={guardando}
            onGuardar={guardar}
          />
        </div>
      </DataState>

      <div className="admin-config" data-aos="fade-up" data-aos-delay="120">
        <div className="admin-cabecera admin-cabecera-interna">
          <div>
            <strong>Colores del formulario</strong>
            <p className="admin-ayuda">
              Crea los colores con su nombre. Los colores visibles aparecerán en
              la sección de colores del formulario de la tienda.
            </p>
          </div>
          <button
            type="button"
            className="btn btn-primario admin-btn-xs"
            onClick={() => setColorModal('crear')}
          >
            <Plus size={16} aria-hidden="true" /> Nuevo color
          </button>
        </div>

        {errorColores && <p className="admin-error">{errorColores}</p>}

        <DataState
          cargando={cargandoColores}
          vacio={colores.length === 0}
          mensajeVacio="Aún no hay colores. Crea el primero para que aparezca en la tienda."
        >
          <div className="admin-tabla-envoltura">
            <table className="admin-tabla">
              <thead>
                <tr>
                  <th>Color</th>
                  <th>Nombre</th>
                  <th>Identificador</th>
                  <th>Estado</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {colores.map((color) => (
                  <tr key={color.id}>
                    <td>
                      <span
                        className="admin-color-muestra"
                        style={{ background: color.hex }}
                      />
                    </td>
                    <td>{color.nombre}</td>
                    <td className="admin-mono">{color.valor}</td>
                    <td>
                      <span
                        className={
                          color.activo
                            ? 'admin-estado-activo'
                            : 'admin-estado-oculto'
                        }
                      >
                        {color.activo ? 'Visible' : 'Oculto'}
                      </span>
                    </td>
                    <td className="admin-acciones">
                      <button
                        type="button"
                        className="btn btn-secundario admin-btn-xs"
                        onClick={() => alternarColor(color)}
                      >
                        {color.activo ? 'Ocultar' : 'Mostrar'}
                      </button>
                      <button
                        type="button"
                        className="btn btn-secundario admin-btn-xs"
                        onClick={() => setColorModal({ color })}
                      >
                        Editar
                      </button>
                      <button
                        type="button"
                        className="btn admin-btn-xs admin-btn-peligro"
                        onClick={() => eliminarColorFlor(color)}
                      >
                        <Trash2 size={14} aria-hidden="true" /> Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </DataState>
      </div>

      <AccesoriosManager id="accesorios-formulario" />

      {colorModal && (
        <FormModal
          titulo={colorModal === 'crear' ? 'Nuevo color' : 'Editar color'}
          onCerrar={() => setColorModal(null)}
        >
          <ColorForm
            color={colorModal === 'crear' ? null : colorModal.color}
            onGuardar={guardarColor}
            onCancelar={() => setColorModal(null)}
          />
          {guardandoColor && <p className="admin-estado">Guardando…</p>}
        </FormModal>
      )}
    </div>
  );
}

export default ArreglosPage;