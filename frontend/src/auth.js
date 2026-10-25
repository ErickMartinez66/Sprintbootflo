const TOKEN_KEY = 'adybloom_admin_token';

export function obtenerToken() {
  return sessionStorage.getItem(TOKEN_KEY);
}

export function guardarToken(token) {
  if (token) {
    sessionStorage.setItem(TOKEN_KEY, token);
  }
}

export function limpiarToken() {
  sessionStorage.removeItem(TOKEN_KEY);
}

export function estaAutenticado() {
  return Boolean(obtenerToken());
}
