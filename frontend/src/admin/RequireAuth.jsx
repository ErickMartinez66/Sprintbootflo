import { Navigate, useLocation } from 'react-router-dom';
import { estaAutenticado } from '../auth.js';

function RequireAuth({ children }) {
  const location = useLocation();

  if (!estaAutenticado()) {
    return <Navigate to="/admin/login" replace state={{ from: location }} />;
  }

  return children;
}

export default RequireAuth;
