import { useEffect, useState } from 'react';
import { PackageCheck } from 'lucide-react';
import { crearPedido, formatearPrecio, listarAccesorios } from '../api.js';
import COLORES from '../data/colores.js';
import IconoArreglo from './IconoArreglo.jsx';

export const CONFIG_POR_DEFECTO = {
  nombre: 'Arreglo personalizado',
  precioVenta: 110000,
  cantidadFlores: 24,
  colorFlores: 'rosa',
  accesorios: '',
  emoji: '💐',
  gradiente: 'linear-gradient(160deg, #faead6, #efbe8f)',
};

function hoyISO() {
  return new Date().toISOString().split('T')[0];
}

function Formulario({ config }) {
  const floresBase = Number(config.cantidadFlores) > 0 ? Number(config.cantidadFlores) : 1;
  const precioPorFlor = (Number(config.precioVenta) || 0) / floresBase;

  const [cantidadFlores, setCantidadFlores] = useState(floresBase);
  const [colorFlores, setColorFlores] = useState(config.colorFlores || 'rosa');
  const [accesoriosSeleccionados, setAccesoriosSeleccionados] = useState([]);
  const [accesoriosDisponibles, setAccesoriosDisponibles] = useState([]);
  const [fechaEntrega, setFechaEntrega] = useState('');
  const [nombre, setNombre] = useState('');
  const [direccion, setDireccion] = useState('');
  const [numero, setNumero] = useState('');
  const [estado, setEstado] = useState('formulario');
  const [error, setError] = useState('');

  const precioVenta = Math.round(
    precioPorFlor * cantidadFlores +
      accesoriosSeleccionados.reduce((sum, a) => sum + (Number(a.precio) || 0), 0)
  );

  const colores = COLORES.some((c) => c.valor === colorFlores)
    ? COLORES
    : [...COLORES, { valor: colorFlores, etiqueta: `${colorFlores} (actual)` }];

  useEffect(() => {
    const cargarAccesorios = async () => {
      try {
        const lista = await listarAccesorios();
        setAccesoriosDisponibles(lista || []);
      } catch (err) {
        console.error('Error al cargar accesorios:', err);
      }
    };
    cargarAccesorios();
  }, []);

  const envio = async (e) => {
    e.preventDefault();
    setError('');
    setEstado('enviando');

    const pedido = {
      precioVenta,
      cantidadFlores: Number(cantidadFlores),
      colorFlores,
      fechaEntrega,
      accesorios: accesoriosSeleccionados,
      cliente: { nombre, direccion, numero },
    };

    try {
      await crearPedido(pedido);
      setEstado('exito');
    } catch (err) {
      setEstado('formulario');
      setError(
        err.message || 'No se pudo registrar tu pedido. Inténtalo de nuevo.'
      );
    }
  };

  const alCambiarCantidad = (valor) => {
    setCantidadFlores(Math.max(1, Number(valor) || 1));
  };

  const reiniciar = () => {
    setCantidadFlores(floresBase);
    setColorFlores(config.colorFlores || 'rosa');
    setAccesoriosSeleccionados([]);
    setFechaEntrega('');
    setNombre('');
    setDireccion('');
    setNumero('');
    setError('');
    setEstado('formulario');
  };

  if (estado === 'exito') {
    return (
      <div className="modal-exito">
        <div className="modal-exito-icono">
          <PackageCheck size={44} aria-hidden="true" />
        </div>
        <h3 className="modal-titulo">¡Gracias por tu pedido!</h3>
        <p className="modal-exito-texto">
          Tu arreglo <strong>{config.nombre}</strong> fue registrado. Muy pronto
          estará listo y nos pondremos en contacto para coordinar la entrega el
          día {fechaEntrega}.
        </p>
        <button className="btn btn-primario" onClick={reiniciar}>
          Hacer otro pedido
        </button>
      </div>
    );
  }

  return (
    <>
      <div
        className="modal-banner"
        style={{ background: config.gradiente || 'var(--grad-primario)' }}
      >
        <span className="modal-banner-icono">
          <IconoArreglo emoji={config.emoji} size={34} />
        </span>
        <div>
          <h3 className="modal-titulo">{config.nombre}</h3>
          <p className="modal-precio">{formatearPrecio(precioVenta)}</p>
        </div>
      </div>

      <form className="modal-form" onSubmit={envio}>
        <div className="campo">
          <label htmlFor="cantidadFlores">Cantidad de flores</label>
          <input
            id="cantidadFlores"
            type="number"
            min="1"
            value={cantidadFlores}
            onChange={(e) => alCambiarCantidad(e.target.value)}
            required
          />
        </div>

        <div className="campo">
          <label htmlFor="colorFlores">Color de las flores</label>
          <select
            id="colorFlores"
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
          <label htmlFor="fechaEntrega">Fecha de entrega</label>
          <input
            id="fechaEntrega"
            type="date"
            min={hoyISO()}
            value={fechaEntrega}
            onChange={(e) => setFechaEntrega(e.target.value)}
            required
          />
        </div>

        <div className="campo">
          <label>Accesorios (puedes seleccionar varios)</label>
          <div className="accesorios-lista">
            {accesoriosDisponibles.length === 0 && (
              <p className="accesorios-vacio">No hay accesorios disponibles.</p>
            )}
            {accesoriosDisponibles.map((acc) => {
              const seleccionado = accesoriosSeleccionados.some(
                (a) => a.id === acc.id
              );
              return (
                <label
                  key={acc.id}
                  className={`accesorio-item${seleccionado ? ' activo' : ''}`}
                >
                  <input
                    type="checkbox"
                    checked={seleccionado}
                    onChange={() =>
                      setAccesoriosSeleccionados((prev) =>
                        seleccionado
                          ? prev.filter((a) => a.id !== acc.id)
                          : [...prev, acc]
                      )
                    }
                  />
                  <span className="accesorio-nombre">{acc.nombre}</span>
                  <span className="accesorio-precio">
                    {formatearPrecio(acc.precio)}
                  </span>
                </label>
              );
            })}
          </div>
        </div>

        <div className="modal-separador">Datos de contacto</div>

        <div className="campo">
          <label htmlFor="nombre">Nombre completo</label>
          <input
            id="nombre"
            type="text"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
            placeholder="Ej: Ana María Pérez"
          />
        </div>

        <div className="campo">
          <label htmlFor="direccion">Dirección de entrega</label>
          <input
            id="direccion"
            type="text"
            value={direccion}
            onChange={(e) => setDireccion(e.target.value)}
            required
            placeholder="Ej: Av. Principal #12-34"
          />
        </div>

        <div className="campo">
          <label htmlFor="numero">Teléfono / celular</label>
          <input
            id="numero"
            type="text"
            value={numero}
            onChange={(e) => setNumero(e.target.value)}
            required
            placeholder="Ej: 310-555-1234"
          />
        </div>

        {error && <p className="modal-error">{error}</p>}

        <button
          className="btn btn-primario modal-enviar"
          type="submit"
          disabled={estado === 'enviando'}
        >
          {estado === 'enviando'
            ? 'Registrando...'
            : `Confirmar pedido · ${formatearPrecio(precioVenta)}`}
        </button>
      </form>
    </>
  );
}

function FormularioPedido({ config, cargando, error }) {
  return (
    <section id="pedido" className="pedido">
      <div className="container">
        <span className="eyebrow" data-aos="fade-up">
          Pedido
        </span>
        <h2 className="titulo-seccion" data-aos="fade-up">
          Haz tu pedido
        </h2>
        <p className="subtitulo-seccion" data-aos="fade-up" data-aos-delay="60">
          Cuéntanos cómo quieres tu arreglo: elige la cantidad de flores, el
          color, los accesorios y la fecha de entrega.
        </p>

        {cargando && <p className="pedido-estado">Cargando el formulario…</p>}

        {!cargando && error && (
          <p className="pedido-estado pedido-error">
            No pudimos cargar el formulario. Inténtalo de nuevo en un momento.
          </p>
        )}

        {!cargando && !error && (
          <div className="pedido-caja" data-aos="zoom-in-up">
            <Formulario
              key={config?.id ?? 'default'}
              config={{ ...CONFIG_POR_DEFECTO, ...config }}
            />
          </div>
        )}
      </div>
    </section>
  );
}

export default FormularioPedido;
