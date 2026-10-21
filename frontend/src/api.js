const API_BASE = '/api';

async function request(path, opciones = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: opciones.body instanceof FormData ? undefined : { 'Content-Type': 'application/json' },
    ...opciones,
  });

  if (!res.ok) {
    let mensaje = `Error ${res.status}`;
    try {
      const data = await res.json();
      mensaje = data.message || JSON.stringify(data);
    } catch {
      mensaje = await res.text().catch(() => mensaje);
    }
    throw new Error(mensaje);
  }

  if (res.status === 204) return null;
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

export function crearPedido(pedido) {
  return request('/pedidos', {
    method: 'POST',
    body: JSON.stringify(pedido),
  });
}

export function eliminarPedido(id) {
  return request(`/pedidos/${id}`, { method: 'DELETE' });
}

export function listarMeses() {
  return request('/meses');
}

export function obtenerMes(id) {
  return request(`/meses/${id}`);
}

export function listarPedidosDelMes(id) {
  return request(`/meses/${id}/pedidos`);
}

export function sincronizarMeses() {
  return request('/meses/sync', { method: 'POST' });
}

export function listarViajes() {
  return request('/viajes');
}

export function obtenerViaje(id) {
  return request(`/viajes/${id}`);
}

export function crearViaje(viaje) {
  return request('/viajes', {
    method: 'POST',
    body: JSON.stringify(viaje),
  });
}

export function actualizarViaje(id, viaje) {
  return request(`/viajes/${id}`, {
    method: 'PUT',
    body: JSON.stringify(viaje),
  });
}

export function eliminarViaje(id) {
  return request(`/viajes/${id}`, { method: 'DELETE' });
}

export function listarProductosDeViaje(viajeId) {
  return request(`/viajes/${viajeId}/productos`);
}

export function agregarProducto(viajeId, producto) {
  return request(`/viajes/${viajeId}/productos`, {
    method: 'POST',
    body: JSON.stringify(producto),
  });
}

export function actualizarProducto(productoId, producto) {
  return request(`/viajes/productos/${productoId}`, {
    method: 'PUT',
    body: JSON.stringify(producto),
  });
}

export function eliminarProducto(productoId) {
  return request(`/viajes/productos/${productoId}`, { method: 'DELETE' });
}

export function listarArreglos() {
  return request('/arreglos');
}

export function listarAccesorios() {
  return request('/accesorios');
}

export function crearAccesorio(accesorio) {
  return request('/accesorios', {
    method: 'POST',
    body: JSON.stringify(accesorio),
  });
}

export function actualizarAccesorio(id, accesorio) {
  return request(`/accesorios/${id}`, {
    method: 'PUT',
    body: JSON.stringify(accesorio),
  });
}

export function eliminarAccesorio(id) {
  return request(`/accesorios/${id}`, { method: 'DELETE' });
}

export function listarArreglosActivos() {
  return request('/arreglos/activos');
}

export function crearArreglo(arreglo) {
  return request('/arreglos', {
    method: 'POST',
    body: JSON.stringify(arreglo),
  });
}

export function actualizarArreglo(id, arreglo) {
  return request(`/arreglos/${id}`, {
    method: 'PUT',
    body: JSON.stringify(arreglo),
  });
}

export function eliminarArreglo(id) {
  return request(`/arreglos/${id}`, { method: 'DELETE' });
}

export function obtenerInversionTotal() {
  return request('/inversion-total');
}

export function actualizarInversionTotal(inversionTotal) {
  return request('/inversion-total', {
    method: 'PUT',
    body: JSON.stringify(inversionTotal),
  });
}

export function subirImagen(archivo) {
  const form = new FormData();
  form.append('archivo', archivo);
  return request('/imagenes', { method: 'POST', body: form });
}

export function eliminarImagen(url) {
  const nombre = nombreImagen(url);
  if (!nombre) return Promise.resolve(null);
  return request(`/imagenes/${nombre}`, { method: 'DELETE' });
}

function nombreImagen(url) {
  if (!url || !url.startsWith('/media/')) return null;
  return url.slice('/media/'.length);
}

export function formatearPrecio(valor) {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    maximumFractionDigits: 0,
  }).format(valor);
}

export function formatearFecha(fecha) {
  if (!fecha) return '—';
  const [anio, mes, dia] = fecha.split('-');
  return `${dia}/${mes}/${anio}`;
}