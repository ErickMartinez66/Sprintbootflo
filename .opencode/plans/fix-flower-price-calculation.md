# Plan para corregir el cálculo del precio en FormularioPedido

## Problema
En el formulario de la página principal, al cambiar la cantidad de flores el precio no se actualiza correctamente. El precio de los accesorios sí se suma, pero el precio correspondiente al número de flores no se suma cuando se modifica la cantidad.

## Causa
En src/components/FormularioPedido.jsx líneas ~35-40:
`js
const floresActuales = Math.max(1, Number(cantidadFlores) || 1);
const precioPorFlorActual = (Number(config.precioVenta) || 0) / floresActuales;
const precioVenta = Math.round(
  precioPorFlorActual * floresActuales +
    accesoriosSeleccionados.reduce((sum, a) => sum + (Number(a.precio) || 0), 0)
);
`
config.precioVenta es el precio TOTAL del arreglo para la cantidad base (config.cantidadFlores). Al dividir por loresActuales y multiplicar de nuevo por loresActuales se obtiene el mismo valor sin importar cuántas flores se pongan. Por eso el precio de las flores no se actualiza al cambiar la cantidad.

## Solución
Calcular el precio por flor con base en la cantidad base del arreglo (config.cantidadFlores), multiplicarlo por la cantidad actual de flores, y sumar los accesorios.

Cambio propuesto:
`js
const floresActuales = Math.max(1, Number(cantidadFlores) || 1);
const floresBase = Math.max(1, Number(config.cantidadFlores) || floresActuales);
const precioPorFlorBase = (Number(config.precioVenta) || 0) / floresBase;
const precioVenta = Math.round(
  precioPorFlorBase * floresActuales +
    accesoriosSeleccionados.reduce((sum, a) => sum + (Number(a.precio) || 0), 0)
);
`

## Archivos a modificar
- src/components/FormularioPedido.jsx (líneas 34-40)

## Verificación
1. Abrir el formulario con un arreglo que tenga precio base y cantidad base definida
2. Cambiar la cantidad de flores (aumentar/disminuir)
3. Verificar que el precio mostrado se actualice proporcionalmente al precio por flor base + accesorios
4. Confirmar que el pedido enviado incluya el precio calculado correctamente
