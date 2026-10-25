import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, Trash2 } from 'lucide-react';
import {
  actualizarEstadoPedido,
  eliminarPedido,
  formatearFecha,
  formatearPrecio,
  listarPedidosDelMes,
  obtenerMes,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';

const NOMBRES_MESES = [
  'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
  'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre',
];

function nombreMes(mes) {
  return NOMBRES_MESES[(mes || 1) - 1];
}

function MesDetallePage() {
  const { id } = useParams();
  const [mes, setMes] = useState(null);
  const [pedidos, setPedidos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [cambiandoEstadoId, setCambiandoEstadoId] = useState(null);

  const cargar = useCallback(async () => {
    const [info, lista] = await Promise.all([obtenerMes(id), listarPedidosDelMes(id)]);
    setMes(info);
    setPedidos(lista);
  }, [id]);

  useEffect(() => {
    let activo = true;
    (async () => {
      try {
        setCargando(true);
        setError('');
        const [info, lista] = await Promise.all([obtenerMes(id), listarPedidosDelMes(id)]);
        if (!activo) return;
        setMes(info);
        setPedidos(lista);
      } catch (err) {
        if (activo) setError(err.message);
      } finally {
        if (activo) setCargando(false);
      }
    })();
    return () => {
      activo = false;
    };
  }, [id]);

  useAosRefresh(cargando);

  const eliminar = async (pedido) => {
    if (
      !window.confirm(
        `¿Eliminar el pedido del ${formatearFecha(pedido.fechaEntrega)}? Se actualizarán la ganancia del mes y los totales.`
      )
    ) {
      return;
    }
    setError('');
    try {
      await eliminarPedido(pedido.id);
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  const alternarEstado = async (pedido) => {
    const nuevoEstado = pedido.estado === 'entregado' ? 'pendiente' : 'entregado';
    setError('');
    setCambiandoEstadoId(pedido.id);
    try {
      await actualizarEstadoPedido(pedido.id, nuevoEstado);
      await cargar();
    } catch (err) {
      setError(err.message);
    } finally {
      setCambiandoEstadoId(null);
    }
  };

  return (
    <div className="admin-panel">
      <Link className="admin-volver-inline" to="/admin/meses">
        <ArrowLeft size={16} aria-hidden="true" /> Volver a meses
      </Link>

      <DataState cargando={cargando} error={error}>
        {mes && (
          <>
            <div className="admin-resumen" data-aos="fade-up">
              <div>
                <h1 className="admin-titulo">
                  {nombreMes(mes.mes)} {mes.anio}
                </h1>
                <p className="admin-subtitulo">Resumen del mes.</p>
              </div>
              <div className="admin-tarjetas">
                <div className="admin-tarjeta">
                  <small>Ganancia total</small>
                  <strong className="admin-dorado">{formatearPrecio(mes.gananciaTotal)}</strong>
                </div>
                <div className="admin-tarjeta">
                  <small>Pedidos entregados</small>
                  <strong>{mes.cantidadPedidos}</strong>
                </div>
              </div>
            </div>

            <h2 className="admin-subtitulo-admin">Pedidos del mes</h2>
            <DataState
              cargando={false}
              vacio={pedidos.length === 0}
              mensajeVacio="Este mes no tiene pedidos registrados."
            >
              <div className="admin-tabla-envoltura">
                <table className="admin-tabla" data-aos="fade-up" data-aos-delay="120">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Fecha entrega</th>
                      <th>Flores</th>
                      <th>Color</th>
                      <th>Venta</th>
                      <th>Inversi��n</th>
                      <th>Ganancia</th>
                      <th>Cliente</th>
                      <th>Estado</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {pedidos.map((pedido) => (
                      <tr key={pedido.id}>
                        <td>{pedido.id}</td>
                        <td>{formatearFecha(pedido.fechaEntrega)}</td>
                        <td>{pedido.cantidadFlores}</td>
                        <td>
                          {pedido.colores?.length
                            ? pedido.colores
                                .map((c) => `${c.color}: ${c.cantidad}`)
                                .join(', ')
                            : '—'}
                        </td>
                        <td>{formatearPrecio(pedido.precioVenta)}</td>
                        <td>{formatearPrecio(pedido.precioInversion)}</td>
                        <td className="admin-dorado">{formatearPrecio(pedido.precioGanancia)}</td>
                        <td>{pedido.cliente?.nombre || '—'}</td>
                        <td>
                          <span
                            className={
                              pedido.estado === 'entregado'
                                ? 'admin-estado-activo'
                                : 'admin-estado-pendiente'
                            }
                          >
                            {pedido.estado === 'entregado' ? 'Entregado' : 'Pendiente'}
                          </span>
                        </td>
                        <td className="admin-acciones">
                          <button
                            className="btn btn-secundario admin-btn-xs"
                            onClick={() => alternarEstado(pedido)}
                            disabled={cambiandoEstadoId === pedido.id}
                          >
                            {pedido.estado === 'entregado'
                              ? 'Marcar pendiente'
                              : 'Marcar entregado'}
                          </button>
                          <button
                            className="btn admin-btn-xs admin-btn-peligro"
                            onClick={() => eliminar(pedido)}
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
    </div>
  );
}

export default MesDetallePage;