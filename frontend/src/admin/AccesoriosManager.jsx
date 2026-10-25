import { useCallback, useEffect, useState } from 'react';
import { Plus, Trash2 } from 'lucide-react';
import {
  actualizarAccesorio,
  crearAccesorio,
  eliminarAccesorio,
  formatearPrecio,
  listarAccesorios,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';
import FormModal from './FormModal.jsx';

function AccesorioForm({ accesorio, onGuardar, onCancelar }) {
  const [nombre, setNombre] = useState(accesorio?.nombre ?? '');
  const [precio, setPrecio] = useState(accesorio?.precio ?? 0);

  const enviar = (e) => {
    e.preventDefault();
    onGuardar({ nombre, precio: Number(precio) || 0 });
  };

  return (
    <form className="admin-form" onSubmit={enviar}>
      <div className="campo">
        <label htmlFor="accesorioNombre">Nombre</label>
        <input
          id="accesorioNombre"
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
          placeholder="Ej: Lazo de terciopelo"
        />
      </div>
      <div className="campo">
        <label htmlFor="accesorioPrecio">Precio (pesos)</label>
        <input
          id="accesorioPrecio"
          type="number"
          step="0.01"
          min="0"
          value={precio}
          onChange={(e) => setPrecio(e.target.value)}
          required
        />
      </div>
      <div className="admin-form-acciones">
        <button type="button" className="btn btn-secundario" onClick={onCancelar}>
          Cancelar
        </button>
        <button type="submit" className="btn btn-primario">
          Guardar accesorio
        </button>
      </div>
    </form>
  );
}

function AccesoriosManager({ id }) {
  const [accesorios, setAccesorios] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);
  const [guardando, setGuardando] = useState(false);

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      setAccesorios(await listarAccesorios());
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
        await crearAccesorio(datos);
      } else {
        await actualizarAccesorio(modal.id, datos);
      }
      setModal(null);
      await cargar();
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  };

  const eliminar = async (accesorio) => {
    if (!window.confirm(`¿Eliminar el accesorio "${accesorio.nombre}"?`)) {
      return;
    }
    setError('');
    try {
      await eliminarAccesorio(accesorio.id);
      await cargar();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="admin-config" id={id} data-aos="fade-up" data-aos-delay="160">
      <div className="admin-cabecera admin-cabecera-interna">
        <div>
          <strong>Accesorios</strong>
          <p className="admin-ayuda">
            Crea los accesorios con sus precios; aparecerán en el formulario de
            pedido de la tienda.
          </p>
        </div>
        <button
          type="button"
          className="btn btn-primario admin-btn-xs"
          onClick={() => setModal('crear')}
        >
          <Plus size={16} aria-hidden="true" /> Nuevo accesorio
        </button>
      </div>

      {error && <p className="admin-error">{error}</p>}

      <DataState
        cargando={cargando}
        vacio={accesorios.length === 0}
        mensajeVacio="Aún no hay accesorios. Crea el primero para que aparezca en la tienda."
      >
        <div className="admin-tabla-envoltura">
          <table className="admin-tabla">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Precio</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {accesorios.map((accesorio) => (
                <tr key={accesorio.id}>
                  <td className="admin-mono">{accesorio.id}</td>
                  <td>{accesorio.nombre}</td>
                  <td className="admin-dorado">{formatearPrecio(accesorio.precio)}</td>
                  <td className="admin-acciones">
                    <button
                      className="btn btn-secundario admin-btn-xs"
                      onClick={() =>
                        setModal({ modo: 'editar', id: accesorio.id, accesorio })
                      }
                    >
                      Editar
                    </button>
                    <button
                      className="btn admin-btn-xs admin-btn-peligro"
                      onClick={() => eliminar(accesorio)}
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
          titulo={modal === 'crear' ? 'Nuevo accesorio' : 'Editar accesorio'}
          onCerrar={() => setModal(null)}
        >
          <AccesorioForm
            accesorio={modal === 'crear' ? null : modal.accesorio}
            onGuardar={guardar}
            onCancelar={() => setModal(null)}
          />
          {guardando && <p className="admin-estado">Guardando…</p>}
        </FormModal>
      )}
    </div>
  );
}

export default AccesoriosManager;
