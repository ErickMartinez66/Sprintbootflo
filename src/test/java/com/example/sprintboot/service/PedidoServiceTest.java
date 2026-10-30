package com.example.sprintboot.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.DetalleColorDTO;
import com.example.sprintboot.dto.InversionTotalDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.entity.InversionTotal;
import com.example.sprintboot.entity.Pedido;
import com.example.sprintboot.repository.PedidoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PedidoServiceTest {

	private static final LocalDate FECHA = LocalDate.of(2026, 10, 15);

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private InversionTotalService inversionTotalService;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private long siguienteId = 1;

	@Test
	void sumaLaInversionFijaAlPasarDePendienteAEntregadoSinDuplicar() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_PENDIENTE, 500.0, false);

		pedidoService.actualizarEstado(pedidoId, Pedido.ESTADO_ENTREGADO);

		assertThat(saldoFijo()).isEqualTo(1500.0);
		assertThat(inversionFijaAplicada(pedidoId)).isTrue();

		pedidoService.actualizarEstado(pedidoId, Pedido.ESTADO_ENTREGADO);

		assertThat(saldoFijo()).isEqualTo(1500.0);
	}

	@Test
	void restaLaInversionFijaAlVolverDeEntregadoAPendienteSinDuplicar() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_ENTREGADO, 500.0, true);

		pedidoService.actualizarEstado(pedidoId, Pedido.ESTADO_PENDIENTE);

		assertThat(saldoFijo()).isEqualTo(500.0);
		assertThat(inversionFijaAplicada(pedidoId)).isFalse();

		pedidoService.actualizarEstado(pedidoId, Pedido.ESTADO_PENDIENTE);

		assertThat(saldoFijo()).isEqualTo(500.0);
	}

	@Test
	void noRestaPedidosEntregadosAnterioresQueNuncaSeContabilizaron() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_ENTREGADO, 700.0, null);

		pedidoService.actualizarEstado(pedidoId, Pedido.ESTADO_PENDIENTE);

		assertThat(saldoFijo()).isEqualTo(1000.0);
		assertThat(inversionFijaAplicada(pedidoId)).isFalse();
	}

	@Test
	void ajustaLaInversionFijaAlEditarUnPedidoEntregadoContabilizado() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_ENTREGADO, 500.0, true);

		PedidoDTO actualizado = pedidoService.actualizar(pedidoId, pedidoDTO(4));

		assertThat(actualizado.getPrecioInversion()).isEqualTo(1000.0);
		assertThat(saldoFijo()).isEqualTo(1500.0);

		pedidoService.actualizar(pedidoId, pedidoDTO(4));

		assertThat(saldoFijo()).isEqualTo(1500.0);
	}

	@Test
	void noAjustaLaInversionFijaAlEditarUnPedidoEntregadoAnterior() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_ENTREGADO, 500.0, null);

		pedidoService.actualizar(pedidoId, pedidoDTO(4));

		assertThat(saldoFijo()).isEqualTo(1000.0);
	}

	@Test
	void descuentaLaInversionFijaAlEliminarUnPedidoEntregadoContabilizado() {
		fijarSaldoFijo(1000.0);
		long pedidoId = insertarPedido(Pedido.ESTADO_ENTREGADO, 500.0, true);

		pedidoService.eliminar(pedidoId);

		assertThat(saldoFijo()).isEqualTo(500.0);
	}

	private PedidoDTO pedidoDTO(int cantidadFlores) {
		PedidoDTO dto = new PedidoDTO();
		dto.setPrecioVenta(5000.0);
		dto.setCantidadFlores(cantidadFlores);
		dto.setFechaEntrega(FECHA);
		dto.setColores(List.of(new DetalleColorDTO(cantidadFlores, "rojo")));
		return dto;
	}

	private long insertarPedido(String estado, Double inversion, Boolean inversionFijaAplicada) {
		long id = siguienteId++;
		long fechaEntrega = FECHA.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		jdbcTemplate.update(
				"insert into pedido (id, cantidad_flores, fecha_entrega, estado, precio_venta, precio_ganancia, "
						+ "precio_inversion, inversion_fija_aplicada) values (?, ?, ?, ?, ?, ?, ?, ?)",
				id, 2, fechaEntrega, estado, inversion + 1000.0, 1000.0, inversion,
				inversionFijaAplicada == null ? null : (inversionFijaAplicada ? 1 : 0));
		return id;
	}

	private void fijarSaldoFijo(double saldo) {
		inversionTotalService.actualizar(new InversionTotalDTO(InversionTotal.ID_UNICO, 0.0, 0.0, saldo));
	}

	private double saldoFijo() {
		return inversionTotalService.obtener().getInversionFija();
	}

	private boolean inversionFijaAplicada(long pedidoId) {
		return Boolean.TRUE.equals(pedidoRepository.findById(pedidoId)
				.orElseThrow()
				.getInversionFijaAplicada());
	}
}
