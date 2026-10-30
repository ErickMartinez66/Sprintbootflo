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

import com.example.sprintboot.dto.MesDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.entity.Pedido;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MesServiceTest {

	@Autowired
	private MesService mesService;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private long siguienteId = 1;

	@Test
	void cuentaTodosLosPedidosYSumaSoloLaGananciaEntregada() {
		guardarPedido(LocalDate.of(2026, 10, 22), Pedido.ESTADO_ENTREGADO, 105000.0);
		guardarPedido(LocalDate.of(2026, 10, 30), Pedido.ESTADO_ENTREGADO, 120000.0);
		guardarPedido(LocalDate.of(2026, 10, 30), Pedido.ESTADO_ENTREGADO, 5100.0);
		guardarPedido(LocalDate.of(2026, 10, 30), Pedido.ESTADO_PENDIENTE, 9250.0);

		MesDTO mes = mesService.calcular(202610L);

		assertThat(mes.getCantidadPedidos()).isEqualTo(4);
		assertThat(mes.getGananciaTotal()).isEqualTo(230100.0);
	}

	@Test
	void noIncluyePedidosDelPrimerDiaDelMesSiguiente() {
		long pedidoOctubre = guardarPedido(LocalDate.of(2026, 10, 31), Pedido.ESTADO_ENTREGADO, 1000.0);
		guardarPedido(LocalDate.of(2026, 11, 1), Pedido.ESTADO_PENDIENTE, 2000.0);

		MesDTO octubre = mesService.calcular(202610L);
		MesDTO noviembre = mesService.calcular(202611L);
		List<PedidoDTO> pedidosOctubre = pedidoService.listarPorMes(202610L);

		assertThat(octubre.getCantidadPedidos()).isEqualTo(1);
		assertThat(noviembre.getCantidadPedidos()).isEqualTo(1);
		assertThat(pedidosOctubre).extracting(PedidoDTO::getId).containsExactly(pedidoOctubre);
	}

	private long guardarPedido(LocalDate fecha, String estado, Double ganancia) {
		long id = siguienteId++;
		long fechaEntrega = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		jdbcTemplate.update(
				"insert into pedido (id, cantidad_flores, fecha_entrega, estado, precio_venta, precio_ganancia) "
						+ "values (?, ?, ?, ?, ?, ?)",
				id, 1, fechaEntrega, estado, ganancia + 1000.0, ganancia);
		return id;
	}
}
