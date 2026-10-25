# Plan para agregar ID a la tabla de pedidos en admin

## Objetivo
Agregar la columna ID en la tabla que muestra los pedidos en la sección de admin (vista de detalle de mes).

## Ubicación
Archivo: src/admin/MesDetallePage.jsx

## Cambios necesarios
1. Agregar encabezado <th>ID</th> en el <thead> de la tabla
2. Agregar celda con el ID del pedido en el <tbody>, mostrando pedido.id

## Cambios propuestos
En el thead:
`jsx
<tr>
  <th>ID</th>
  <th>Fecha entrega</th>
  ...
</tr>
`

En el tbody:
`jsx
<tr key={pedido.id}>
  <td>{pedido.id}</td>
  <td>{formatearFecha(pedido.fechaEntrega)}</td>
  ...
</tr>
`

## Verificación
- Verificar que el ID se muestre correctamente en la tabla
