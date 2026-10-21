import { useCallback, useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, Trash2 } from 'lucide-react';
import {
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
                      <th>Fecha entrega</th>
                      <th>Flores</th>
                      <th>Color</th>
                      <th>Venta</th>
                      <th>Inversión</th>
                      <th>Ganancia</th>
                      <th>Cliente</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {pedidos.map((pedido) => (
                      <tr key={pedido.id}>
                        <td>{formatearFecha(pedido.fechaEntrega)}</td>
                        <td>{pedido.cantidadFlores}</td>
                        <td>{pedido.colorFlores}</td>
                        <td>{formatearPrecio(pedido.precioVenta)}</td>
                        <td>{formatearPrecio(pedido.precioInversion)}</td>
                        <td className="admin-dorado">{formatearPrecio(pedido.precioGanancia)}</td>
                        <td>{pedido.cliente?.nombre || '—'}</td>
                        <td className="admin-acciones">
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