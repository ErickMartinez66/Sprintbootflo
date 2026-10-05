import arreglos from '../data/arreglos.js';
import ArregloCard from './ArregloCard.jsx';

function Catalogo({ onSeleccionar }) {
  return (
    <section id="catalogo" className="catalogo">
      <div className="container">
        <h2 className="titulo-seccion">Nuestros arreglos</h2>
        <p className="subtitulo-seccion">
          Cada arreglo es creado a mano con flores seleccionadas. Toca una card
          para personalizar tu pedido.
        </p>
        <div className="catalogo-grid">
          {arreglos.map((arreglo) => (
            <ArregloCard key={arreglo.id} arreglo={arreglo} onSeleccionar={onSeleccionar} />
          ))}
        </div>
      </div>
    </section>
  );
}

export default Catalogo;