package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO con la referencia de una imagen local subida para una card.")
public class ImagenDTO {

	@Schema(description = "Nombre del archivo guardado en el servidor", example = "arreglo-6f1b1f9e-9d1c-4f6a-9c3e-2f0f8b6d1a55.jpg")
	private String nombre;

	@Schema(description = "URL relativa con la que el frontend muestra la imagen", example = "/media/arreglo-6f1b1f9e-9d1c-4f6a-9c3e-2f0f8b6d1a55.jpg")
	private String url;

	public ImagenDTO() {
	}

	public ImagenDTO(String nombre, String url) {
		this.nombre = nombre;
		this.url = url;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
