package com.example.sprintboot.config;

import java.util.ArrayList;
import java.util.List;

import com.example.sprintboot.entity.DetalleColor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ColoresConverter implements AttributeConverter<List<DetalleColor>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<DetalleColor> colores) {
        if (colores == null || colores.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(colores);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo serializar la lista de colores", e);
        }
    }

    @Override
    public List<DetalleColor> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return MAPPER.readValue(json, new TypeReference<List<DetalleColor>>() {
            });
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer la lista de colores guardada", e);
        }
    }
}
