package com.example.sprintboot.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.entity.Accesorio;
import com.example.sprintboot.repository.AccesorioRepository;

@Component
@Order(1)
public class AccesorioSeed implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AccesorioSeed.class);

    private final AccesorioRepository accesorioRepository;

    public AccesorioSeed(AccesorioRepository accesorioRepository) {
        this.accesorioRepository = accesorioRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (accesorioRepository.count() > 0) {
            return;
        }
        List<Accesorio> accesorios = Arrays.asList(
                new Accesorio(null, "Lazo de terciopelo", 1500.0),
                new Accesorio(null, "Tarjeta personalizada", 1000.0),
                new Accesorio(null, "Moño", 800.0),
                new Accesorio(null, "Sobres de papel kraft", 500.0),
                new Accesorio(null, "Caja coleccionable", 2500.0)
        );
        accesorioRepository.saveAll(accesorios);
        LOG.info("Accesorios iniciales cargados: {}", accesorioRepository.count());
    }
}
