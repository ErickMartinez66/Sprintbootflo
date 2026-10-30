package com.example.sprintboot.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.AccesorioDTO;
import com.example.sprintboot.dto.ClienteDTO;
import com.example.sprintboot.dto.CostoCDTO;
import com.example.sprintboot.dto.DetalleColorDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.entity.Accesorio;
import com.example.sprintboot.entity.Cliente;
import com.example.sprintboot.entity.CostoC;
import com.example.sprintboot.entity.DetalleColor;
import com.example.sprintboot.entity.Pedido;
import com.example.sprintboot.repository.AccesorioRepository;
import com.example.sprintboot.repository.ClienteRepository;
import com.example.sprintboot.repository.CostoCRepository;
import com.example.sprintboot.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final CostoCRepository costoCRepository;
    private final MesService mesService;
    private final AccesorioRepository accesorioRepository;
    private final InversionTotalService inversionTotalService;

    public PedidoService(PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            CostoCRepository costoCRepository,
            MesService mesService,
            AccesorioRepository accesorioRepository,
            InversionTotalService inversionTotalService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.costoCRepository = costoCRepository;
        this.mesService = mesService;
        this.accesorioRepository = accesorioRepository;
        this.inversionTotalService = inversionTotalService;
    }

    public PedidoDTO crear(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        aplicarCampos(pedido, dto);
        pedido.setEstado(Pedido.ESTADO_PENDIENTE);
        pedido.setInversionFijaAplicada(false);

        if (dto.getCliente() != null) {
            pedido.setCliente(resolverCliente(dto.getCliente()));
        }

        Pedido guardado = pedidoRepository.save(pedido);

        if (dto.getCostoC() != null) {
            CostoC costoC = new CostoC();
            aplicarCostoC(costoC, dto.getCostoC());
            costoC.setPedido(guardado);
            costoCRepository.save(costoC);
        }
        mesService.calcularPorFecha(guardado.getFechaEntrega());
        return convertirADto(guardado);
    }

    public List<PedidoDTO> listar() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    public PedidoDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        return convertirADto(pedido);
    }

    public PedidoDTO asignarCliente(Long pedidoId, ClienteDTO clienteDTO) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + pedidoId));
        pedido.setCliente(resolverCliente(clienteDTO));
        return convertirADto(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoDTO actualizar(Long id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        LocalDate fechaEntregaAnterior = pedido.getFechaEntrega();
        Double inversionAnterior = pedido.getPrecioInversion();
        boolean estabaEntregado = Pedido.ESTADO_ENTREGADO.equals(pedido.getEstado());
        boolean inversionFijaAplicada = Boolean.TRUE.equals(pedido.getInversionFijaAplicada());
        aplicarCampos(pedido, dto);

        if (dto.getCliente() != null) {
            pedido.setCliente(resolverCliente(dto.getCliente()));
        }

        if (dto.getCostoC() != null) {
            CostoC costoC = pedido.getCostoC();
            if (costoC == null) {
                costoC = new CostoC();
                costoC.setPedido(pedido);
                pedido.setCostoC(costoC);
            }
            aplicarCostoC(costoC, dto.getCostoC());
            costoCRepository.save(costoC);
        }
        Pedido guardado = pedidoRepository.save(pedido);
        if (estabaEntregado && inversionFijaAplicada) {
            double diferencia = valorSeguro(guardado.getPrecioInversion()) - valorSeguro(inversionAnterior);
            if (diferencia > 0) {
                inversionTotalService.sumar(diferencia);
            } else if (diferencia < 0) {
                inversionTotalService.restar(-diferencia);
            }
        }
        mesService.calcularPorFecha(fechaEntregaAnterior);
        mesService.calcularPorFecha(guardado.getFechaEntrega());
        return convertirADto(guardado);
    }

    @Transactional
    public PedidoDTO actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        String normalizado = estado == null ? "" : estado.trim().toLowerCase();
        if (!Pedido.ESTADO_PENDIENTE.equals(normalizado) && !Pedido.ESTADO_ENTREGADO.equals(normalizado)) {
            throw new IllegalArgumentException(
                    "Estado inválido: '" + estado + "'. Usa '" + Pedido.ESTADO_PENDIENTE + "' o '"
                            + Pedido.ESTADO_ENTREGADO + "'.");
        }

        boolean estabaEntregado = Pedido.ESTADO_ENTREGADO.equals(pedido.getEstado());
        boolean quedaEntregado = Pedido.ESTADO_ENTREGADO.equals(normalizado);
        pedido.setEstado(normalizado);

        if (!estabaEntregado && quedaEntregado) {
            inversionTotalService.sumar(pedido.getPrecioInversion());
            pedido.setInversionFijaAplicada(true);
        } else if (estabaEntregado && !quedaEntregado) {
            if (Boolean.TRUE.equals(pedido.getInversionFijaAplicada())) {
                inversionTotalService.restar(pedido.getPrecioInversion());
            }
            pedido.setInversionFijaAplicada(false);
        }

        Pedido guardado = pedidoRepository.save(pedido);
        mesService.calcularPorFecha(guardado.getFechaEntrega());
        return convertirADto(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        if (Pedido.ESTADO_ENTREGADO.equals(pedido.getEstado())
                && Boolean.TRUE.equals(pedido.getInversionFijaAplicada())) {
            inversionTotalService.restar(pedido.getPrecioInversion());
        }
        pedidoRepository.delete(pedido);
        mesService.calcularPorFecha(pedido.getFechaEntrega());
    }

    public List<PedidoDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::convertirADto)
                .toList();
    }

    public List<PedidoDTO> listarPorColor(String color) {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getColores() != null && p.getColores().stream()
                        .anyMatch(d -> color != null && color.equalsIgnoreCase(d.getColor())))
                .map(this::convertirADto)
                .toList();
    }

    public List<PedidoDTO> listarPorFecha(LocalDate fecha) {
        return pedidoRepository.findByFechaEntrega(fecha).stream()
                .map(this::convertirADto)
                .toList();
    }

    public List<PedidoDTO> listarPorMes(Long mesId) {
        int anio = (int) (mesId / 100);
        int mes = (int) (mesId % 100);
        if (mes < 1 || mes > 12 || anio < 1) {
            throw new IllegalArgumentException("ID de mes inválido: " + mesId);
        }
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate finExclusivo = inicio.plusMonths(1);
        return pedidoRepository.findByFechaEntregaGreaterThanEqualAndFechaEntregaLessThan(inicio, finExclusivo).stream()
                .map(this::convertirADto)
                .toList();
    }

    private Cliente resolverCliente(ClienteDTO cdto) {
        Cliente cliente;
        if (cdto.getId() != null) {
            cliente = clienteRepository.findById(cdto.getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + cdto.getId()));
        } else {
            cliente = new Cliente();
            cliente.setNombre(cdto.getNombre());
            cliente.setDireccion(cdto.getDireccion());
            cliente.setNumero(cdto.getNumero());
            clienteRepository.save(cliente);
        }
        return cliente;
    }

    private void aplicarCampos(Pedido pedido, PedidoDTO dto) {
        pedido.setPrecioVenta(dto.getPrecioVenta());
        pedido.setCantidadFlores(dto.getCantidadFlores());
        pedido.setColores(validarColores(dto.getColores(), dto.getCantidadFlores()));
        pedido.setFechaEntrega(dto.getFechaEntrega());

        Set<Accesorio> accesorios = new HashSet<>();
        if (dto.getAccesorios() != null) {
            for (AccesorioDTO adto : dto.getAccesorios()) {
                if (adto != null && adto.getId() != null) {
                    accesorioRepository.findById(adto.getId()).ifPresent(accesorios::add);
                } else if (adto != null && adto.getNombre() != null && !adto.getNombre().isBlank()) {
                    accesorioRepository.findByNombre(adto.getNombre().trim()).ifPresent(accesorios::add);
                }
            }
        }
        pedido.setAccesorios(accesorios);
        calcularInversionYGanancia(pedido);
    }

    private List<DetalleColor> validarColores(List<DetalleColorDTO> colores, Integer cantidadFlores) {
        if (colores == null || colores.isEmpty()) {
            throw new IllegalArgumentException("Debes elegir al menos un color para el pedido.");
        }

        List<DetalleColor> detalle = new ArrayList<>();
        Set<String> vistos = new HashSet<>();
        int suma = 0;

        for (DetalleColorDTO item : colores) {
            if (item == null) {
                throw new IllegalArgumentException("Detalle de color inválido.");
            }
            String color = item.getColor() == null ? "" : item.getColor().trim();
            if (color.isEmpty()) {
                throw new IllegalArgumentException("Cada color debe tener un nombre.");
            }
            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new IllegalArgumentException(
                        "La cantidad de flores del color '" + color + "' debe ser al menos 1.");
            }
            if (!vistos.add(color.toLowerCase())) {
                throw new IllegalArgumentException("El color '" + color + "' está repetido.");
            }
            suma += item.getCantidad();
            detalle.add(new DetalleColor(item.getCantidad(), color));
        }

        if (cantidadFlores == null) {
            throw new IllegalArgumentException("La cantidad total de flores es obligatoria.");
        }
        if (suma != cantidadFlores) {
            throw new IllegalArgumentException(
                    "La suma de flores por color (" + suma + ") debe ser igual a la cantidad total de flores ("
                            + cantidadFlores + ").");
        }
        return detalle;
    }

    private void calcularInversionYGanancia(Pedido pedido) {
        if (pedido.getCantidadFlores() == null) {
            pedido.setPrecioInversion(null);
            pedido.setPrecioGanancia(null);
            return;
        }
        Double inversion = pedido.getCantidadFlores() * Pedido.COSTO_POR_FLOR;
        pedido.setPrecioInversion(inversion);
        if (pedido.getPrecioVenta() != null) {
            pedido.setPrecioGanancia(pedido.getPrecioVenta() - inversion);
        } else {
            pedido.setPrecioGanancia(null);
        }
    }

    private double valorSeguro(Double valor) {
        return valor == null ? 0.0 : valor;
    }

    private void aplicarCostoC(CostoC costoC, CostoCDTO dto) {
        costoC.setPalos(dto.getPalos());
        costoC.setPetalos(dto.getPetalos());
        costoC.setHoja(dto.getHoja());
        costoC.setHojam(dto.getHojam());
        costoC.setLazo(dto.getLazo());
        costoC.setMariposa(dto.getMariposa());
        costoC.setCilicona(dto.getCilicona());
        costoC.setOtro(dto.getOtro());
    }

    private PedidoDTO convertirADto(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(p.getId());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setPrecioInversion(p.getPrecioInversion());
        dto.setPrecioGanancia(p.getPrecioGanancia());
        dto.setCantidadFlores(p.getCantidadFlores());

        if (p.getColores() != null) {
            dto.setColores(p.getColores().stream()
                    .map(d -> new DetalleColorDTO(d.getCantidad(), d.getColor()))
                    .collect(Collectors.toList()));
        }
        dto.setFechaEntrega(p.getFechaEntrega());
        dto.setEstado(p.getEstado());

        if (p.getAccesorios() != null) {
            List<AccesorioDTO> lista = p.getAccesorios().stream()
                    .map(acc -> new AccesorioDTO(acc.getId(), acc.getNombre(), acc.getPrecio()))
                    .collect(Collectors.toList());
            dto.setAccesorios(lista);
        }

        if (p.getCliente() != null) {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setId(p.getCliente().getId());
            clienteDTO.setNombre(p.getCliente().getNombre());
            clienteDTO.setDireccion(p.getCliente().getDireccion());
            clienteDTO.setNumero(p.getCliente().getNumero());
            dto.setCliente(clienteDTO);
        }
        if (p.getCostoC() != null) {
            CostoC costoC = p.getCostoC();
            CostoCDTO costocDTO = new CostoCDTO();
            costocDTO.setId(costoC.getId());
            costocDTO.setPedidoId(p.getId());
            costocDTO.setPalos(costoC.getPalos());
            costocDTO.setPetalos(costoC.getPetalos());
            costocDTO.setHoja(costoC.getHoja());
            costocDTO.setHojam(costoC.getHojam());
            costocDTO.setLazo(costoC.getLazo());
            costocDTO.setMariposa(costoC.getMariposa());
            costocDTO.setCilicona(costoC.getCilicona());
            costocDTO.setOtro(costoC.getOtro());
            dto.setCostoC(costocDTO);
        }
        return dto;
    }
}
