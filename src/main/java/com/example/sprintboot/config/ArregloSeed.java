package com.example.sprintboot.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.entity.Accesorio;
import com.example.sprintboot.entity.Arreglo;
import com.example.sprintboot.repository.AccesorioRepository;
import com.example.sprintboot.repository.ArregloRepository;

@Component
@Order(2)
public class ArregloSeed implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ArregloSeed.class);

    private final ArregloRepository arregloRepository;
    private final AccesorioRepository accesorioRepository;

    public ArregloSeed(ArregloRepository arregloRepository, AccesorioRepository accesorioRepository) {
        this.arregloRepository = arregloRepository;
        this.accesorioRepository = accesorioRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (arregloRepository.count() > 0) {
            return;
        }
        List<Accesorio> accesorios = accesorioRepository.findAll();
        Accesorio lazo = findByNombre(accesorios, "Lazo de terciopelo");
        Accesorio tarjeta = findByNombre(accesorios, "Tarjeta personalizada");
        Accesorio mono = findByNombre(accesorios, "Moño");
        Accesorio sobres = findByNombre(accesorios, "Sobres de papel kraft");
        Accesorio caja = findByNombre(accesorios, "Caja coleccionable");

        Arreglo rosas = new Arreglo(null, "Rosas de Terciopelo",
                "Clásicas rosas en tonos rosados con lazo de terciopelo.", 110000.0, 24, "rosa", set(lazo),
                "\ud83d\udca5", "linear-gradient(160deg, #f7e2bd, #e5b989)", null, true, 1);
        Arreglo tulipanes = new Arreglo(null, "Capricho de Tulipanes",
                "Tulipanes frescos en tonos pastel con tarjeta personalizada.", 95000.0, 18, "pastel", set(tarjeta),
                "\ud83d\udca5", "linear-gradient(160deg, #fdecd7, #f0cfa6)", null, true, 2);
        Arreglo bouquet = new Arreglo(null, "Bouquet Eternidad",
                "Arreglo eterno que combina flores rosadas y blancas.", 135000.0, 30, "blanco", set(tarjeta, mono),
                "\ud83d\udca5", "linear-gradient(160deg, #f8ecd9, #e7c584)", null, true, 3);
        Arreglo atardecer = new Arreglo(null, "Atardecer Dorado",
                "Mezcla cálida de girasoles y flores silvestres soleadas.", 88000.0, 16, "amarillo", set(sobres),
                "\ud83d\udca5", "linear-gradient(160deg, #fff3d6, #f4c97e)", null, true, 4);
        Arreglo jardin = new Arreglo(null, "Jardín de Primavera",
                "Caja de mezcla floral vibrante, perfecta para celebrar.", 150000.0, 36, "rosa", set(caja, mono),
                "\ud83d\udca5", "linear-gradient(160deg, #faead6, #efbe8f)", null, true, 5);
        Arreglo susurro = new Arreglo(null, "Susurro Elegante",
                "Arreglo sobrio de orquídeas y hojas verdes de impacto.", 125000.0, 12, "blanco", set(tarjeta),
                "\ud83d\udca5", "linear-gradient(160deg, #eef7f2, #b9e0ce)", null, true, 6);
        arregloRepository.saveAll(List.of(rosas, tulipanes, bouquet, atardecer, jardin, susurro));
        LOG.info("Catálogo inicial cargado con {} cards", arregloRepository.count());
    }

    private Accesorio findByNombre(List<Accesorio> accesorios, String nombre) {
        return accesorios.stream().filter(a -> nombre.equals(a.getNombre())).findFirst().orElse(null);
    }

    private Set<Accesorio> set(Accesorio... items) {
        Set<Accesorio> result = new HashSet<>();
        if (items != null) {
            for (Accesorio a : items) {
                if (a != null) {
                    result.add(a);
                }
            }
        }
        return result;
    }
}
