# Plan para cambiar modal de éxito con botón de WhatsApp

## Objetivo
En el modal de agradecimiento tras crear un pedido, eliminar el botón 'Hacer otro pedido' y reemplazarlo por un botón 'Confirmar en WhatsApp' que lleve al chat de WhatsApp (+5354912292) con un mensaje pre-generado que incluya el ID del pedido creado y el precio del pedido creado.

## Análisis actual
- En FormularioPedido.jsx:
  - crearPedido es llamado y si tiene éxito se hace setEstado('exito') (línea ~115-122)
  - La función crearPedido en pi.js retorna el resultado de la petición (probablemente el pedido creado con su ID)
  - En el modal de éxito (línea ~166-183), se muestra info del pedido pero no se guarda el ID/respuesta
  - No hay estado para guardar la respuesta del pedido

## Necesario
1. Guardar la respuesta de crearPedido cuando sea exitosa (necesita incluir id y precioVenta al menos)
2. Modificar el modal de éxito para mostrar botón de WhatsApp en lugar de "Hacer otro pedido"
3. Generar URL de WhatsApp con mensaje: Hola quiero (id del pedido creado) y precio del pedido creado - el formato exacto dice "hola quiero (id del pedido creado) y precio del pedido creado" (aunque un poco ambiguo, probablemente algo como "Hola, quiero confirmar el pedido #ID - Precio: X")

## Cambios

### 1. Guardar respuesta del pedido
Agregar estado pedidoCreado para almacenar la respuesta:
`jsx
const [pedidoCreado, setPedidoCreado] = useState(null);
// ...
const res = await crearPedido(pedido);
setPedidoCreado(res);
setEstado('exito');
`

### 2. Modificar modal de éxito
Reemplazar el botón:
`jsx
<button
  className="btn btn-primario"
  onClick={() => {
    const mensaje = Hola quiero  y precio del pedido creado ;
    const url = https://wa.me/5354912292?text=;
    window.open(url, '_blank');
  }}
>
  Confirmar en WhatsApp
</button>
`

O ajustar el mensaje según lo que se entienda - dice "hola quiero (id del pedido creado) y precio del pedido creado". Podría ser "Hola, quiero el pedido ID X, precio Y" o similar. Seguir lo más literal posible pero siendo claro.

## Archivos a modificar
- src/components/FormularioPedido.jsx

## Verificación
1. Crear un pedido - debe llegar al modal de éxito
2. Debe aparecer botón "Confirmar en WhatsApp" (sin "Hacer otro pedido")
3. Al hacer click debe abrir WhatsApp con número +5354912292 y mensaje con ID y precio
