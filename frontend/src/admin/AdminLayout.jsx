import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom';
import { Flower, Sprout, CalendarDays, ShoppingBag, Wallet, ArrowLeft, LogOut } from 'lucide-react';
import { limpiarToken } from '../auth.js';
import { logoutAdmin } from '../api.js';

function AdminLayout() {
  const navigate = useNavigate();

  const cerrarSesion = async () => {
    try {
      await logoutAdmin();
    } catch {
      // Ignorar errores al cerrar sesión
    } finally {
      limpiarToken();
      navigate('/admin/login', { replace: true });
    }
  };

  return (
    <div className="admin">
      <aside className="admin-sidebar">
        <div className="admin-marca">
          <span className="admin-marca-icono">
            <Flower size={22} aria-hidden="true" />
          </span>
          <div>
            <strong>Ady Bloom</strong>
            <small>Panel de administración</small>
          </div>
        </div>
        <nav className="admin-menu">
          <NavLink
            to="/admin/arreglos"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <Sprout size={18} aria-hidden="true" /> Formulario de pedido
          </NavLink>
          <NavLink
            to="/admin/meses"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <CalendarDays size={18} aria-hidden="true" /> Información por meses
          </NavLink>
          <NavLink
            to="/admin/viajes"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <ShoppingBag size={18} aria-hidden="true" /> Viajes
          </NavLink>
          <NavLink
            to="/admin/inversion"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <Wallet size={18} aria-hidden="true" /> Inversión fija
          </NavLink>
        </nav>
        <button type="button" className="admin-volver" onClick={cerrarSesion}>
          <LogOut size={15} aria-hidden="true" /> Cerrar sesión
        </button>
      </aside>
      <main className="admin-contenido">
        <Outlet />
      </main>
    </div>
  );
}

export default AdminLayout;