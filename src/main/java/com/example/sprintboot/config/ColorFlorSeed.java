package com.example.sprintboot.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.entity.ColorFlor;
import com.example.sprintboot.repository.ColorFlorRepository;

@Component
@Order(0)
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class ColorFlorSeed implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory.getLogger(ColorFlorSeed.class);

	private final ColorFlorRepository colorFlorRepository;

	public ColorFlorSeed(ColorFlorRepository colorFlorRepository) {
		this.colorFlorRepository = colorFlorRepository;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (colorFlorRepository.count() > 0) {
			return;
		}
		List<ColorFlor> colores = Arrays.asList(
				new ColorFlor(null, "rosa", "Rosa", "#e8a0b4", true, 1),
				new ColorFlor(null, "blanco", "Blanco", "#f5f0e6", true, 2),
				new ColorFlor(null, "amarillo", "Amarillo", "#f4c542", true, 3),
				new ColorFlor(null, "verde", "Verde", "#6fae7a", true, 4),
				new ColorFlor(null, "rojo", "Rojo", "#c0392b", true, 5),
				new ColorFlor(null, "lila", "Lila", "#b39ddb", true, 6));
		colorFlorRepository.saveAll(colores);
		LOG.info("Colores iniciales cargados: {}", colorFlorRepository.count());
	}
}
