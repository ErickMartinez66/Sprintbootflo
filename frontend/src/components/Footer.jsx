import { Phone, Mail, MapPin, Flower, Camera, MessageCircle, Globe } from 'lucide-react';

function Footer() {
  return (
    <footer id="contacto" className="footer">
      <div className="container footer-inner" data-aos="fade-up">
        <div>
          <p className="footer-marca">
            <span className="footer-marca-icono">
              <Flower size={22} aria-hidden="true" />
            </span>
            Ady Bloom
          </p>
          <p className="footer-lema">
            Flores hechas con amor, arreglos únicos y personalizados para cada
            ocasión especial.
          </p>
          <div className="footer-redes">
            <a className="footer-red-enlace" href="#" aria-label="Instagram">
              <Camera size={17} />
            </a>
            <a className="footer-red-enlace" href="#" aria-label="Facebook">
              <Globe size={17} />
            </a>
            <a className="footer-red-enlace" href="#" aria-label="WhatsApp">
              <MessageCircle size={17} />
            </a>
          </div>
        </div>

        <div>
          <h3 className="footer-titulo">Navegación</h3>
          <ul className="footer-lista">
            <li><a href="#inicio">Inicio</a></li>
            <li><a href="#pedido">Nuestro pedido</a></li>
            <li><a href="#contacto">Contacto</a></li>
            <li><a href="/admin">Panel administrativo</a></li>
          </ul>
        </div>

        <div>
          <h3 className="footer-titulo">Contacto</h3>
          <div className="footer-contacto-item">
            <span className="footer-contacto-icono">
              <Phone size={16} aria-hidden="true" />
            </span>
            310-555-1234
          </div>
          <div className="footer-contacto-item">
            <span className="footer-contacto-icono">
              <Mail size={16} aria-hidden="true" />
            </span>
            hola@adybloom.com
          </div>
          <div className="footer-contacto-item">
            <span className="footer-contacto-icono">
              <MapPin size={16} aria-hidden="true" />
            </span>
            Centro de flores, Bogotá
          </div>
        </div>

        <div>
          <h3 className="footer-titulo">Información</h3>
          <ul className="footer-lista">
            <li><a href="#pedido">Entrega prioritaria</a></li>
            <li><a href="#pedido">Arreglos a medida</a></li>
            <li><a href="#pedido">Hecho 100% a mano</a></li>
            <li><a href="#contacto">Atención personalizada</a></li>
          </ul>
        </div>
      </div>
      <div className="footer-copy">
        © {new Date().getFullYear()} <strong>Ady Bloom</strong> · Todos los
        derechos reservados.
      </div>
    </footer>
  );
}

export default Footer;