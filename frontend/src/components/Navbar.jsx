function Navbar() {
  const irACatalogo = () => {
    document.getElementById('catalogo')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <header className="navbar">
      <div className="container navbar-inner">
        <a href="#inicio" className="navbar-brand">
          <span className="navbar-flor">🌸</span> Ady Bloom
        </a>
        <nav className="navbar-links">
          <a href="#inicio">Inicio</a>
          <a href="#catalogo">Arreglos</a>
          <a href="#contacto">Contacto</a>
          <button className="btn btn-primario btn-nav" onClick={irACatalogo}>
            Hacer mi pedido
          </button>
        </nav>
      </div>
    </header>
  );
}

export default Navbar;