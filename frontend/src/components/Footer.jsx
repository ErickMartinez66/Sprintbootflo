function Footer() {
  return (
    <footer id="contacto" className="footer">
      <div className="container footer-inner">
        <div>
          <span className="navbar-flor">🌸</span>
          <strong>Ady Bloom</strong>
          <p className="footer-lema">Flores hechas con amor.</p>
        </div>
        <div className="footer-contacto">
          <p>📞 310-555-1234</p>
          <p>✉️ hola@adybloom.com</p>
          <p>📍 Centro de flores, Bogotá</p>
        </div>
      </div>
      <div className="footer-copy">
        © {new Date().getFullYear()} Ady Bloom · Todos los derechos reservados.
      </div>
    </footer>
  );
}

export default Footer;