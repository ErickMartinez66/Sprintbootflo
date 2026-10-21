import { Flower, Heart, Clock, Sparkles, BadgeCheck } from 'lucide-react';

function Hero() {
  const irAPedido = () => {
    document.getElementById('pedido')?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <section id="inicio" className="hero">
      <div className="container hero-inner">
        <div className="hero-texto" data-aos="fade-up">
          <span className="eyebrow">Arreglos artesanales</span>
          <h1 className="hero-titulo">
            Flores hechas con <em>amor</em>,<br /> para quienes amas.
          </h1>
          <p className="hero-descripcion">
            En Ady Bloom creamos arreglos florales únicos y personalizados para
            cada ocasión. Cuéntanos cómo lo quieres y haz tu pedido en unos
            minutos.
          </p>
          <div className="hero-acciones">
            <button className="btn btn-primario" onClick={irAPedido}>
              Hacer mi pedido
            </button>
            <button className="btn btn-secundario" onClick={irAPedido}>
              Pedir a domicilio
            </button>
          </div>
          <div className="hero-detalles">
            <div className="hero-detalle">
              <span className="hero-detalle-icono">
                <BadgeCheck size={20} aria-hidden="true" />
              </span>
              <div>
                <strong>100%</strong>
                <span>Hecho a mano</span>
              </div>
            </div>
            <div className="hero-detalle">
              <span className="hero-detalle-icono">
                <Heart size={20} aria-hidden="true" />
              </span>
              <div>
                <strong>+500</strong>
                <span>Clientes felices</span>
              </div>
            </div>
            <div className="hero-detalle">
              <span className="hero-detalle-icono">
                <Clock size={20} aria-hidden="true" />
              </span>
              <div>
                <strong>24h</strong>
                <span>Entrega prioritaria</span>
              </div>
            </div>
          </div>
        </div>
        <div className="hero-flor" data-aos="fade-up" data-aos-delay="120" aria-hidden="true">
          <div className="hero-flor-circulo">
            <span className="hero-flor-icono">
              <Flower size={46} aria-hidden="true" />
            </span>
            <span className="petalo p1">
              <Sparkles size={20} />
            </span>
            <span className="petalo p2">
              <Flower size={20} />
            </span>
            <span className="petalo p3">
              <Flower size={20} />
            </span>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Hero;