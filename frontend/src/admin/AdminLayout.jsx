import { Link, NavLink, Outlet } from 'react-router-dom';
import { Flower, Sprout, CalendarDays, ShoppingBag, Wallet, Package, ArrowLeft } from 'lucide-react';

function AdminLayout() {
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
            to="/admin/accesorios"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <Package size={18} aria-hidden="true" /> Accesorios
          </NavLink>
          <NavLink
            to="/admin/inversion"
            className={({ isActive }) => (isActive ? 'admin-menu-item activo' : 'admin-menu-item')}
          >
            <Wallet size={18} aria-hidden="true" /> Inversión fija
          </NavLink>
        </nav>
        <Link to="/" className="admin-volver">
          <ArrowLeft size={15} aria-hidden="true" /> Volver a la tienda
        </Link>
      </aside>
      <main className="admin-contenido">
        <Outlet />
      </main>
    </div>
  );
}

export default AdminLayout;