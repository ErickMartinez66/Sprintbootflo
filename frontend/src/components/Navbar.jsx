import { Flower } from 'lucide-react';

function Navbar() {
  const irAPedido = () => {
    document.getElementById('pedido')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <header className="navbar" data-aos="fade-down">
      <div className="container navbar-inner">
        <a href="#inicio" className="navbar-brand">
          <span className="navbar-brand-icon">
            <Flower size={20} aria-hidden="true" />
          </span>
          Ady Bloom
        </a>
        <nav className="navbar-links">
          <a href="#inicio">Inicio</a>
          <a href="#pedido">Pedido</a>
          <a href="#contacto">Contacto</a>
          <button className="btn btn-primario btn-nav" onClick={irAPedido}>
            Hacer mi pedido
          </button>
        </nav>
      </div>
    </header>
  );
}

export default Navbar;