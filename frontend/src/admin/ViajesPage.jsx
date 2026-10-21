import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Plus, Trash2 } from 'lucide-react';
import {
  actualizarViaje,
  crearViaje,
  eliminarViaje,
  formatearFecha,
  formatearPrecio,
  listarViajes,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';
import FormModal from './FormModal.jsx';

function ViajeForm({ viaje, onGuardar, onCancelar }) {
  const [inversion, setInversion] = useState(viaje?.inversion ?? 0);
  const [fechaCreacion, setFechaCreacion] = useState(viaje?.fechaCreacion ?? '');

  const enviar = (e) => {
    e.preventDefault();
    onGuardar({ inversion: Number(inversion) || 0, fechaCreacion });
  };

  return (
    <form className="admin-form" onSubmit={enviar}>
      <div className="campo">
        <label htmlFor="viajeInversion">Inversión (pesos)</label>
        <input
          id="viajeInversion"
          type="number"
          step="0.01"
          min="0"
          value={inversion}
          onChange={(e) => setInversion(e.target.value)}
          required
        />
      </div>
      <div className="campo">
        <label htmlFor="viajeFecha">Fecha de creación</label>
        <input
          id="viajeFecha"
          type="date"
          value={fechaCreacion}
          onChange={(e) => setFechaCreacion(e.target.value)}
          required
        />
      </div>
      <div className="admin-form-acciones">
        <button type="button" className="btn btn-secundario" onClick={onCancelar}>
          Cancelar
        </button>
        <button type="submit" className="btn btn-primario">
          Guardar viaje
        </button>
      </div>
    </form>
  );
}

function ViajesPage() {
  const [viajes, setViajes] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);
  const [guardando, setGuardando] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      setViajes(await listarViajes());
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

  const guardar = async (datos) => {
    setGuardando(true);
    setError('');
    try {
      if (modal === 'crear') {
        await crearViaje(datos);
      } else {
        await actualizarViaje(modal.id, datos);
      }
      setModal(null);
      await cargar();
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  };

  const eliminar = async (viaje) => {
    if (!window.confirm(`¿Eliminar el viaje del ${formatearFecha(viaje.fechaCreacion)} y sus productos?`)) {
      return;
    }
    setError('');
    try {
      await eliminarViaje(viaje.id);
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="admin-panel">
      <div className="admin-cabecera" data-aos="fade-up">
        <div>
          <h1 className="admin-titulo">Viajes</h1>
          <p className="admin-subtitulo">
            Gestiona los viajes de compras y sus productos asociados.
          </p>
        </div>
        <button className="btn btn-primario" onClick={() => setModal('crear')}>
          <Plus size={18} aria-hidden="true" /> Nuevo viaje
        </button>
      </div>

      <DataState cargando={cargando} error={error || undefined} vacio={viajes.length === 0}>
        <div className="admin-tabla-envoltura" data-aos="fade-up" data-aos-delay="120">
          <table className="admin-tabla">
            <thead>
              <tr>
                <th>ID</th>
                <th>Fecha de creación</th>
                <th>Inversión</th>
                <th>Productos</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {viajes.map((viaje) => (
                <tr key={viaje.id}>
                  <td className="admin-mono">{viaje.id}</td>
                  <td>{formatearFecha(viaje.fechaCreacion)}</td>
                  <td className="admin-dorado">{formatearPrecio(viaje.inversion)}</td>
                  <td>{viaje.productos?.length ?? 0}</td>
                  <td className="admin-acciones">
                    <Link className="btn btn-primario admin-btn-xs" to={`/admin/viajes/${viaje.id}`}>
                      Ver productos
                    </Link>
                    <button
                      className="btn btn-secundario admin-btn-xs"
                      onClick={() => setModal({ modo: 'editar', id: viaje.id, viaje })}
                    >
                      Editar
                    </button>
                    <button
                      className="btn admin-btn-xs admin-btn-peligro"
                      onClick={() => eliminar(viaje)}
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

      {modal && (
        <FormModal
          titulo={modal === 'crear' ? 'Nuevo viaje' : 'Editar viaje'}
          onCerrar={() => setModal(null)}
        >
          <ViajeForm
            viaje={modal === 'crear' ? null : modal.viaje}
            onGuardar={guardar}
            onCancelar={() => setModal(null)}
          />
          {guardando && <p className="admin-estado">Guardando…</p>}
        </FormModal>
      )}
    </div>
  );
}

export default ViajesPage;