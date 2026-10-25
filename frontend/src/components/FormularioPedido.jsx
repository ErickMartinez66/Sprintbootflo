import { useEffect, useState } from 'react';
import { PackageCheck } from 'lucide-react';
import {
  crearPedido,
  formatearPrecio,
  listarAccesorios,
  listarColoresActivos,
} from '../api.js';
import IconoArreglo from './IconoArreglo.jsx';

function hoyISO() {
  return new Date().toISOString().split('T')[0];
}

function Formulario({ config, colores }) {
  const floresBaseInicial = Number(config.cantidadFlores) > 0 ? Number(config.cantidadFlores) : 1;
  const colorBaseInicial = colores.some((c) => c.valor === config.colorFlores)
    ? config.colorFlores
    : (colores[0]?.valor ?? null);

  const [cantidadFlores, setCantidadFlores] = useState(floresBaseInicial);
  const [coloresSeleccionados, setColoresSeleccionados] = useState(
    colorBaseInicial ? [{ color: colorBaseInicial, cantidad: floresBaseInicial }] : []
  );
  const [accesoriosSeleccionados, setAccesoriosSeleccionados] = useState([]);
  const [accesoriosDisponibles, setAccesoriosDisponibles] = useState([]);
  const [fechaEntrega, setFechaEntrega] = useState('');
  const [nombre, setNombre] = useState('');
  const [direccion, setDireccion] = useState('');
  const [numero, setNumero] = useState('');
  const [estado, setEstado] = useState('formulario');
  const [error, setError] = useState('');
  const [pedidoCreado, setPedidoCreado] = useState(null);

  const floresActuales = Math.max(1, Number(cantidadFlores) || 1);
  const floresBase = Math.max(1, Number(config.cantidadFlores) || floresActuales);
  const precioPorFlorBase = (Number(config.precioVenta) || 0) / floresBase;
  const precioVenta = Math.round(
    precioPorFlorBase * floresActuales +
      accesoriosSeleccionados.reduce((sum, a) => sum + (Number(a.precio) || 0), 0)
  );

  useEffect(() => {
    const floresBaseNueva = Number(config.cantidadFlores) > 0 ? Number(config.cantidadFlores) : 1;
    const colorBaseNueva = colores.some((c) => c.valor === config.colorFlores)
      ? config.colorFlores
      : (colores[0]?.valor ?? null);
    setCantidadFlores(floresBaseNueva);
    setColoresSeleccionados(colorBaseNueva ? [{ color: colorBaseNueva, cantidad: floresBaseNueva }] : []);
    setAccesoriosSeleccionados([]);
    setFechaEntrega('');
    setNombre('');
    setDireccion('');
    setNumero('');
    setError('');
    setEstado('formulario');
  }, [
    config?.id,
    config?.nombre,
    config?.precioVenta,
    config?.cantidadFlores,
    config?.colorFlores,
    config?.emoji,
    config?.gradiente,
    colores,
  ]);

  const sumaFlores = coloresSeleccionados.reduce(
    (sum, c) => sum + (Number(c.cantidad) || 0),
    0
  );
  const cuadraSuma = sumaFlores === cantidadFlores && coloresSeleccionados.length > 0;

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

    if (coloresSeleccionados.length === 0) {
      setError('Elige al menos un color para tu arreglo.');
      return;
    }
    if (sumaFlores !== cantidadFlores) {
      setError(
        `La suma de flores por color (${sumaFlores}) debe ser igual a la cantidad total (${cantidadFlores}).`
      );
      return;
    }

    setEstado('enviando');

    const pedido = {
      precioVenta,
      cantidadFlores: Number(cantidadFlores),
      colores: coloresSeleccionados.map((c) => ({
        cantidad: Number(c.cantidad),
        color: c.color,
      })),
      fechaEntrega,
      accesorios: accesoriosSeleccionados,
      cliente: { nombre, direccion, numero },
    };

    try {
      const respuesta = await crearPedido(pedido);
      setPedidoCreado(respuesta);
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

  const alternarColor = (color) => {
    setColoresSeleccionados((prev) => {
      const existe = prev.some((c) => c.color === color);
      if (existe) {
        return prev.filter((c) => c.color !== color);
      }
      const resto = cantidadFlores - sumaFlores;
      const cantidadInicial = prev.length === 0 ? cantidadFlores : Math.max(1, resto);
      return [...prev, { color, cantidad: cantidadInicial }];
    });
  };

  const cambiarCantidadColor = (color, valor) => {
    const cantidad = Math.max(1, Number(valor) || 1);
    setColoresSeleccionados((prev) =>
      prev.map((c) => (c.color === color ? { ...c, cantidad } : c))
    );
  };

  const nombreColor = (valor) =>
    colores.find((c) => c.valor === valor)?.nombre || valor;

  const reiniciar = () => {
    const floresBaseNueva = Number(config.cantidadFlores) > 0 ? Number(config.cantidadFlores) : 1;
    const colorBaseNueva = colores.some((c) => c.valor === config.colorFlores)
      ? config.colorFlores
      : (colores[0]?.valor ?? null);
    setCantidadFlores(floresBaseNueva);
    setColoresSeleccionados(colorBaseNueva ? [{ color: colorBaseNueva, cantidad: floresBaseNueva }] : []);
    setAccesoriosSeleccionados([]);
    setFechaEntrega('');
    setNombre('');
    setDireccion('');
    setNumero('');
    setError('');
    setPedidoCreado(null);
    setEstado('formulario');
  };

  if (estado === 'exito') {
    const pedidoId = pedidoCreado?.id || pedidoCreado?._id || '';
    const precioConfirmar = pedidoCreado?.precioVenta || precioVenta;
    const mensajeWhatsapp = `Hola quiero el pedido con ID ${pedidoId} y precio del pedido creado ${formatearPrecio(precioConfirmar)}`;
    const urlWhatsapp = `https://wa.me/5354912292?text=${encodeURIComponent(mensajeWhatsapp)}`;

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
        <button
          className="btn btn-primario"
          onClick={() => window.open(urlWhatsapp, '_blank')}
        >
          Confirmar en WhatsApp
        </button>
      </div>
    );
  }

  return (
    <>
      <div
        className="modal-banner"
        style={{ background: 'var(--grad-primario)' }}
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
          <label>Colores de las flores (elige uno o varios)</label>
          {colores.length === 0 ? (
            <p className="accesorios-vacio">
              No hay colores disponibles por ahora. Inténtalo más tarde.
            </p>
          ) : (
            <>
              <div className="colores-circulos">
                {colores.map((c) => {
                  const seleccionado = coloresSeleccionados.some(
                    (item) => item.color === c.valor
                  );
                  return (
                    <button
                      key={c.valor}
                      type="button"
                      className={`color-circulo${seleccionado ? ' activo' : ''}`}
                      onClick={() => alternarColor(c.valor)}
                      aria-pressed={seleccionado}
                      title={c.nombre}
                    >
                      <span
                        className="color-circulo-relleno"
                        style={{ background: c.hex }}
                      />
                      <span className="color-circulo-etiqueta">{c.nombre}</span>
                    </button>
                  );
                })}
              </div>

              {coloresSeleccionados.length > 0 && (
                <div className="colores-elegidos">
                  {coloresSeleccionados.map((item) => (
                    <div key={item.color} className="color-elegido">
                      <span className="color-elegido-nombre">
                        {nombreColor(item.color)}
                      </span>
                      <input
                        type="number"
                        min="1"
                        aria-label={`Cantidad de flores ${nombreColor(item.color)}`}
                        value={item.cantidad}
                        onChange={(e) =>
                          cambiarCantidadColor(item.color, e.target.value)
                        }
                      />
                      <span className="color-elegido-unidad">flores</span>
                    </div>
                  ))}
                </div>
              )}

              <p
                className={`suma-indicador${cuadraSuma ? ' ok' : ' error'}`}
                aria-live="polite"
              >
                {cuadraSuma
                  ? `Suma correcta: ${sumaFlores} de ${cantidadFlores} flores`
                  : `Suma actual: ${sumaFlores} de ${cantidadFlores} flores — ajusta las cantidades`}
              </p>
            </>
          )}
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
  const [colores, setColores] = useState(null);

  useEffect(() => {
    let vigente = true;
    const cargarColores = async () => {
      try {
        const lista = await listarColoresActivos();
        if (vigente) setColores(lista || []);
      } catch (err) {
        console.error('Error al cargar colores:', err);
        if (vigente) setColores([]);
      }
    };
    cargarColores();
    return () => {
      vigente = false;
    };
  }, []);

  const listo = !cargando && !error && colores !== null && config;

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
          Cuéntanos cómo quieres tu arreglo: elige la cantidad de flores, los
          colores (con su cantidad), los accesorios y la fecha de entrega.
        </p>

        {(cargando || (colores === null && !error)) && (
          <p className="pedido-estado">Cargando el formulario…</p>
        )}

        {!cargando && error && (
          <p className="pedido-estado pedido-error">
            No pudimos cargar el formulario. Inténtalo de nuevo en un momento.
          </p>
        )}

        {!cargando && !error && !config && (
          <p className="pedido-estado pedido-error">
            No hay configuración del formulario disponible.
          </p>
        )}

        {listo && (
          <div className="pedido-caja" data-aos="zoom-in-up">
            <Formulario
              key={
                config
                  ? `${config.id}-${config.nombre}-${config.precioVenta}-${config.cantidadFlores}-${config.colorFlores}-${config.emoji}-${config.gradiente}`
                  : 'default'
              }
              config={config}
              colores={colores}
            />
          </div>
        )}
      </div>
    </section>
  );
}

export default FormularioPedido;
