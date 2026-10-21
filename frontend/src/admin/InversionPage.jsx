import { useCallback, useEffect, useState } from 'react';
import {
  actualizarInversionTotal,
  formatearPrecio,
  obtenerInversionTotal,
} from '../api.js';
import useAosRefresh from '../hooks/useAosRefresh.js';
import DataState from './DataState.jsx';

function InversionPage() {
  const [registro, setRegistro] = useState(null);
  const [inversionFija, setInversionFija] = useState('');
  const [cargando, setCargando] = useState(true);
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState('');
  const [exito, setExito] = useState('');

  const cargar = useCallback(async () => {
    try {
      setCargando(true);
      setError('');
      const data = await obtenerInversionTotal();
      setRegistro(data);
      setInversionFija(data.inversionFija ?? 0);
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

  const guardar = async (e) => {
    e.preventDefault();
    setGuardando(true);
    setError('');
    setExito('');
    try {
      const actualizado = await actualizarInversionTotal({
        id: registro?.id ?? 1,
        inversion: registro?.inversion ?? 0,
        inversionDolares: registro?.inversionDolares ?? 0,
        inversionFija: Number(inversionFija) || 0,
      });
      setRegistro(actualizado);
      setInversionFija(actualizado.inversionFija ?? 0);
      setExito('Inversión fija actualizada.');
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  };

  return (
    <div className="admin-panel">
      <div className="admin-cabecera" data-aos="fade-up">
        <div>
          <h1 className="admin-titulo">Inversión fija</h1>
          <p className="admin-subtitulo">
            Edita manualmente el saldo disponible. Acepta valores negativos y se
            descuenta automáticamente con cada viaje que crees.
          </p>
        </div>
      </div>

      <DataState cargando={cargando} error={error || undefined}>
        {registro && (
          <>
            <div className="admin-resumen" data-aos="fade-up" data-aos-delay="80">
              <div className="admin-tarjetas">
                <div className="admin-tarjeta">
                  <small>Inversión fija actual</small>
                  <strong className="admin-dorado">
                    {formatearPrecio(registro.inversionFija ?? 0)}
                  </strong>
                </div>
                <div className="admin-tarjeta">
                  <small>Inversión en pesos</small>
                  <strong>{formatearPrecio(registro.inversion ?? 0)}</strong>
                </div>
                <div className="admin-tarjeta">
                  <small>Inversión en dólares</small>
                  <strong>{formatearPrecio(registro.inversionDolares ?? 0)}</strong>
                </div>
              </div>
            </div>

            <form className="admin-form" onSubmit={guardar} data-aos="fade-up" data-aos-delay="140">
              <div className="campo">
                <label htmlFor="inversionFija">Inversión fija (pesos)</label>
                <input
                  id="inversionFija"
                  type="number"
                  step="0.01"
                  value={inversionFija}
                  onChange={(e) => setInversionFija(e.target.value)}
                  required
                />
                <small className="admin-ayuda">
                  Puedes escribir un valor negativo. Este valor no se valida en el
                  servidor.
                </small>
              </div>

              {exito && <p className="admin-ayuda">{exito}</p>}

              <div className="admin-form-acciones">
                <button
                  type="submit"
                  className="btn btn-primario"
                  disabled={guardando}
                >
                  {guardando ? 'Guardando…' : 'Guardar inversión fija'}
                </button>
              </div>
            </form>
          </>
        )}
      </DataState>
    </div>
  );
}

export default InversionPage;
