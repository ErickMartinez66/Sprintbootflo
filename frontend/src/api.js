const API_BASE = '/api';

export async function crearPedido(pedido) {
  const res = await fetch(`${API_BASE}/pedidos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(pedido),
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
  return res.json();
}

export function formatearPrecio(valor) {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    maximumFractionDigits: 0,
  }).format(valor);
}