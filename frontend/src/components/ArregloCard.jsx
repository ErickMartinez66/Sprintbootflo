import { formatearPrecio } from '../api.js';

function ArregloCard({ arreglo, onSeleccionar }) {
  return (
    <article className="card">
      <div className="card-foto" style={{ background: arreglo.gradiente }}>
        <span className="card-emoji">{arreglo.emoji}</span>
        <span className="card-etiqueta">{arreglo.cantidadFlores} flores</span>
      </div>
      <div className="card-cuerpo">
        <h3 className="card-nombre">{arreglo.nombre}</h3>
        <p className="card-descripcion">{arreglo.descripcion}</p>
        <div className="card-pie">
          <span className="card-precio">{formatearPrecio(arreglo.precioVenta)}</span>
          <button
            className="btn btn-primario card-boton"
            onClick={() => onSeleccionar(arreglo)}
          >
            Hacer mi pedido
          </button>
        </div>
      </div>
    </article>
  );
}

export default ArregloCard;