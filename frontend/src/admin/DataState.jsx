function DataState({ cargando, error, vacio, mensajeVacio = 'No hay datos para mostrar.', children }) {
  if (cargando) return <p className="admin-estado">Cargando…</p>;
  if (error) return <p className="admin-error">{error}</p>;
  if (vacio) return <p className="admin-vacio">{mensajeVacio}</p>;
  return children;
}

export default DataState;