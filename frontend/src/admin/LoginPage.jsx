import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Flower } from 'lucide-react';
import { guardarToken, estaAutenticado } from '../auth.js';
import { loginAdmin } from '../api.js';

function LoginPage() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  useEffect(() => {
    if (estaAutenticado()) {
      navigate('/admin/arreglos', { replace: true });
    }
  }, [navigate]);

  const enviar = async (e) => {
    e.preventDefault();
    setError('');
    setCargando(true);

    try {
      const respuesta = await loginAdmin(usuario.trim(), password);
      if (respuesta?.token) {
        guardarToken(respuesta.token);
        navigate('/admin/arreglos', { replace: true });
        return;
      }
      setError('No se pudo iniciar sesión.');
    } catch (err) {
      setError(err.message || 'Usuario o contraseña incorrectos.');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="admin-login">
      <div className="admin-login-card" data-aos="fade-up">
        <div className="admin-login-cabecera">
          <span className="admin-marca-icono">
            <Flower size={24} aria-hidden="true" />
          </span>
          <div>
            <h1 className="admin-login-titulo">Ady Bloom</h1>
            <p className="admin-login-subtitulo">Panel de administración</p>
          </div>
        </div>

        <form className="admin-login-form" onSubmit={enviar}>
          <div className="campo">
            <label htmlFor="usuario">Usuario</label>
            <input
              id="usuario"
              name="usuario"
              type="text"
              autoComplete="username"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              required
            />
          </div>

          <div className="campo">
            <label htmlFor="password">Contraseña</label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {error && <p className="admin-login-error">{error}</p>}

          <button className="btn btn-primario admin-login-boton" type="submit" disabled={cargando}>
            {cargando ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
