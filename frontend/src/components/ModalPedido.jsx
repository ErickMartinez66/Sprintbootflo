import { useEffect, useState } from 'react';
import { crearPedido, formatearPrecio } from '../api.js';

const COLORES = [
  { valor: 'rosa', etiqueta: 'Rosa' },
  { valor: 'pastel', etiqueta: 'Pastel' },
  { valor: 'blanco', etiqueta: 'Blanco' },
  { valor: 'amarillo', etiqueta: 'Amarillo' },
  { valor: 'multicolor', etiqueta: 'Multicolor' },
  { valor: 'otro', etiqueta: 'Otro' },
];

function hoyISO() {
  return new Date().toISOString().split('T')[0];
}

function ModalPedido({ arreglo, onCerrar }) {
  const precioPorFlor = arreglo.precioVenta / arreglo.cantidadFlores;

  const [cantidadFlores, setCantidadFlores] = useState(arreglo.cantidadFlores);
  const [colorFlores, setColorFlores] = useState(arreglo.colorFlores);
  const [accesorios, setAccesorios] = useState(arreglo.accesorios);
  const [fechaEntrega, setFechaEntrega] = useState('');
  const [nombre, setNombre] = useState('');
  const [direccion, setDireccion] = useState('');
  const [numero, setNumero] = useState('');
  const [estado, setEstado] = useState('formulario');
  const [error, setError] = useState('');

  const precioVenta = Math.round(precioPorFlor * cantidadFlores);

  useEffect(() => {
    const alPresionar = (e) => {
      if (e.key === 'Escape') onCerrar();
    };
    window.addEventListener('keydown', alPresionar);
    return () => window.removeEventListener('keydown', alPresionar);
  }, [onCerrar]);

  const envio = async (e) => {
    e.preventDefault();
    setError('');
    setEstado('enviando');

    const pedido = {
      precioVenta,
      cantidadFlores: Number(cantidadFlores),
      colorFlores,
      fechaEntrega,
      accesorios,
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
    const numero = Math.max(1, Number(valor) || 1);
    setCantidadFlores(numero);
  };

  return (
    <div className="modal-overlay" onClick={onCerrar}>
      <div
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-label="Formulario de pedido"
        onClick={(e) => e.stopPropagation()}
      >
        <button className="modal-cerrar" onClick={onCerrar} aria-label="Cerrar">
          ✕
        </button>

        {estado === 'exito' ? (
          <div className="modal-exito">
            <div className="modal-exito-icono">💐</div>
            <h3 className="modal-titulo">¡Gracias por tu pedido!</h3>
            <p className="modal-exito-texto">
              Tu arreglo <strong>{arreglo.nombre}</strong> fue registrado. Muy
              pronto estará listo y nos pondremos en contacto para coordinar la
              entrega el día {fechaEntrega}.
            </p>
            <button className="btn btn-primario" onClick={onCerrar}>
              Seguir explorando
            </button>
          </div>
        ) : (
          <>
            <div
              className="modal-banner"
              style={{ background: arreglo.gradiente }}
            >
              <span className="modal-banner-emoji">{arreglo.emoji}</span>
              <div>
                <h2 className="modal-titulo">{arreglo.nombre}</h2>
                <p className="modal-precio">
                  {formatearPrecio(precioVenta)}
                </p>
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
                  {COLORES.map((c) => (
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
                <label htmlFor="accesorios">Accesorios</label>
                <input
                  id="accesorios"
                  type="text"
                  value={accesorios}
                  onChange={(e) => setAccesorios(e.target.value)}
                  placeholder="Lazo, moño, tarjeta..."
                />
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
        )}
      </div>
    </div>
  );
}

export default ModalPedido;