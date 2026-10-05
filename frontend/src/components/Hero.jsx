function Hero() {
  const irACatalogo = () => {
    document.getElementById('catalogo')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <section id="inicio" className="hero">
      <div className="container hero-inner">
        <div className="hero-texto">
          <span className="hero-etiqueta">Arreglos artesanales</span>
          <h1 className="hero-titulo">
            Flores hechas con <em>amor</em>,<br /> para quienes amas.
          </h1>
          <p className="hero-descripcion">
            En Ady Bloom creamos arreglos florales únicos y personalizados para
            cada ocasión. Escoge tu favorito y haz tu pedido en unos minutos.
          </p>
          <div className="hero-acciones">
            <button className="btn btn-primario" onClick={irACatalogo}>
              Ver arreglos
            </button>
            <button className="btn btn-secundario" onClick={irACatalogo}>
              Pedir a domicilio
            </button>
          </div>
          <div className="hero-detalles">
            <div>
              <strong>100%</strong>
              <span>Hecho a mano</span>
            </div>
            <div>
              <strong>+500</strong>
              <span>Clientes felices</span>
            </div>
            <div>
              <strong>24h</strong>
              <span>Entrega prioritaria</span>
            </div>
          </div>
        </div>
        <div className="hero-flor" aria-hidden="true">
          <div className="hero-flor-circulo">
            <span>💐</span>
            <span className="petalo p1">🌷</span>
            <span className="petalo p2">🌹</span>
            <span className="petalo p3">🌸</span>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Hero;