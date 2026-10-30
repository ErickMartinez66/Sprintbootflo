import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { RotateCw } from 'lucide-react';
import { formatearPrecio, listarMeses, sincronizarMeses } from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';

const NOMBRES_MESES = [
  'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
  'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre',
];

function MesesPage() {
  const [meses, setMeses] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [sincronizando, setSincronizando] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      setMeses(await listarMeses());
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

  const sincronizar = async () => {
    setSincronizando(true);
    setError('');
    try {
      await sincronizarMeses();
      await cargar();
    } catch (err) {
      setError(err.message);
    } finally {
      setSincronizando(false);
    }
  };

  return (
    <div className="admin-panel">
      <div className="admin-cabecera" data-aos="fade-up">
        <div>
          <h1 className="admin-titulo">Información por meses</h1>
          <p className="admin-subtitulo">
            Ganancia de pedidos entregados y total de pedidos por mes.
          </p>
        </div>
        <button
          className="btn btn-primario"
          onClick={sincronizar}
          disabled={sincronizando}
        >
          <RotateCw size={17} aria-hidden="true" />
          {sincronizando ? 'Sincronizando…' : 'Sincronizar meses'}
        </button>
      </div>

      <DataState cargando={cargando} error={error || undefined} vacio={meses.length === 0}>
        <div className="admin-tabla-envoltura" data-aos="fade-up" data-aos-delay="120">
          <table className="admin-tabla">
            <thead>
              <tr>
                <th>ID</th>
                <th>Mes</th>
                <th>Ganancia total</th>
                <th>Pedidos totales</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {meses.map((mes) => (
                <tr key={mes.id}>
                  <td className="admin-mono">{mes.id}</td>
                  <td>
                    {NOMBRES_MESES[mes.mes - 1]} {mes.anio}
                  </td>
                  <td className="admin-dorado">{formatearPrecio(mes.gananciaTotal)}</td>
                  <td>{mes.cantidadPedidos}</td>
                  <td className="admin-acciones">
                    <Link className="btn btn-secundario admin-btn-xs" to={`/admin/meses/${mes.id}`}>
                      Ver pedidos
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </DataState>
    </div>
  );
}

export default MesesPage;