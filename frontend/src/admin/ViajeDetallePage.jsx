import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, Plus, Trash2 } from 'lucide-react';
import {
  actualizarProducto,
  agregarProducto,
  eliminarProducto,
  formatearFecha,
  formatearPrecio,
  listarProductosDeViaje,
  obtenerViaje,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';
import FormModal from './FormModal.jsx';

function ProductoForm({ producto, onGuardar, onCancelar }) {
  const [nombre, setNombre] = useState(producto?.nombre ?? '');
  const [cantidad, setCantidad] = useState(producto?.cantidad ?? 1);
  const [costoPorUnidad, setCostoPorUnidad] = useState(producto?.costoPorUnidad ?? '');

  const enviar = (e) => {
    e.preventDefault();
    onGuardar({ nombre, cantidad: Number(cantidad) || 0, costoPorUnidad: Number(costoPorUnidad) || 0 });
  };

  return (
    <form className="admin-form" onSubmit={enviar}>
      <div className="campo">
        <label htmlFor="productoNombre">Nombre del producto</label>
        <input
          id="productoNombre"
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
          placeholder="Ej: Palos de madera"
        />
      </div>
      <div className="campo">
        <label htmlFor="productoCantidad">Cantidad</label>
        <input
          id="productoCantidad"
          type="number"
          min="0"
          value={cantidad}
          onChange={(e) => setCantidad(e.target.value)}
          required
        />
      </div>
      <div className="campo">
        <label htmlFor="productoCosto">Costo por unidad</label>
        <input
          id="productoCosto"
          type="number"
          step="0.01"
          min="0"
          value={costoPorUnidad}
          onChange={(e) => setCostoPorUnidad(e.target.value)}
          required
        />
      </div>
      <div className="admin-form-acciones">
        <button type="button" className="btn btn-secundario" onClick={onCancelar}>
          Cancelar
        </button>
        <button type="submit" className="btn btn-primario">
          Guardar producto
        </button>
      </div>
    </form>
  );
}

function ViajeDetallePage() {
  const { id } = useParams();
  const [viaje, setViaje] = useState(null);
  const [productos, setProductos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      const [info, lista] = await Promise.all([obtenerViaje(id), listarProductosDeViaje(id)]);
      setViaje(info);
      setProductos(lista);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  }, [id]);

  useEffect(() => {
    cargar();
  }, [cargar]);

  useAosRefresh(cargando);

  const guardarProducto = async (datos) => {
    setError('');
    try {
      if (modal === 'crear') {
        await agregarProducto(id, datos);
      } else {
        await actualizarProducto(modal.id, datos);
      }
      setModal(null);
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  const eliminar = async (producto) => {
    if (!window.confirm(`¿Eliminar el producto "${producto.nombre}"?`)) return;
    setError('');
    try {
      await eliminarProducto(producto.id);
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  const totalProductos = productos.reduce((acc, p) => acc + (p.cantidad * p.costoPorUnidad || 0), 0);

  return (
    <div className="admin-panel">
      <Link className="admin-volver-inline" to="/admin/viajes">
        <ArrowLeft size={16} aria-hidden="true" /> Volver a viajes
      </Link>

      <DataState cargando={cargando} error={error}>
        {viaje && (
          <>
            <div className="admin-resumen" data-aos="fade-up">
              <div>
                <h1 className="admin-titulo">Viaje #{viaje.id}</h1>
                <p className="admin-subtitulo">
                  Creado el {formatearFecha(viaje.fechaCreacion)}.
                </p>
              </div>
              <div className="admin-tarjetas">
                <div className="admin-tarjeta">
                  <small>Inversión</small>
                  <strong className="admin-dorado">{formatearPrecio(viaje.inversion)}</strong>
                </div>
                <div className="admin-tarjeta">
                  <small>Total productos</small>
                  <strong>{formatearPrecio(totalProductos)}</strong>
                </div>
              </div>
            </div>

            <div className="admin-cabecera admin-cabecera-interna">
              <h2 className="admin-subtitulo-admin">Productos del viaje</h2>
              <button className="btn btn-primario" onClick={() => setModal('crear')}>
                <Plus size={17} aria-hidden="true" /> Agregar producto
              </button>
            </div>

            <DataState
              cargando={false}
              vacio={productos.length === 0}
              mensajeVacio="Este viaje no tiene productos todavía."
            >
              <div className="admin-tabla-envoltura">
                <table className="admin-tabla" data-aos="fade-up" data-aos-delay="120">
                  <thead>
                    <tr>
                      <th>Producto</th>
                      <th>Cantidad</th>
                      <th>Costo por unidad</th>
                      <th>Subtotal</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {productos.map((producto) => (
                      <tr key={producto.id}>
                        <td>
                          <strong>{producto.nombre}</strong>
                        </td>
                        <td>{producto.cantidad}</td>
                        <td>{formatearPrecio(producto.costoPorUnidad)}</td>
                        <td className="admin-dorado">
                          {formatearPrecio(producto.cantidad * producto.costoPorUnidad)}
                        </td>
                        <td className="admin-acciones">
                          <button
                            className="btn btn-secundario admin-btn-xs"
                            onClick={() => setModal({ modo: 'editar', id: producto.id, producto })}
                          >
                            Editar
                          </button>
                          <button
                            className="btn admin-btn-xs admin-btn-peligro"
                            onClick={() => eliminar(producto)}
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
          </>
        )}
      </DataState>

      {modal && (
        <FormModal
          titulo={modal === 'crear' ? 'Agregar producto' : 'Editar producto'}
          onCerrar={() => setModal(null)}
        >
          <ProductoForm
            producto={modal === 'crear' ? null : modal.producto}
            onGuardar={guardarProducto}
            onCancelar={() => setModal(null)}
          />
        </FormModal>
      )}
    </div>
  );
}

export default ViajeDetallePage;