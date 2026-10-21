package com.example.sprintboot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.sprintboot.dto.ImagenDTO;
import com.example.sprintboot.service.ImagenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/imagenes")
@Tag(name = "Imagenes", description = "Subida y gestión de las imágenes locales de las cards del catálogo")
public class ImagenController {

	private final ImagenService imagenService;

	public ImagenController(ImagenService imagenService) {
		this.imagenService = imagenService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Subir imagen de una card",
			description = "Recibe el archivo elegido (computador o celular), lo guarda en la carpeta ./media y devuelve la URL /media/....")
	@ApiResponse(responseCode = "200", description = "Imagen guardada",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImagenDTO.class)))
	@ApiResponse(responseCode = "400", description = "El archivo no es una imagen válida")
	public ImagenDTO subir(@RequestParam("archivo") MultipartFile archivo) {
		String nombre = imagenService.subir(archivo);
		ImagenDTO dto = new ImagenDTO();
		dto.setNombre(nombre);
		dto.setUrl(imagenService.urlDe(nombre));
		return dto;
	}

	@DeleteMapping("/{nombre}")
	@Operation(summary = "Eliminar imagen", description = "Elimina el archivo de la carpeta ./media.")
	@ApiResponse(responseCode = "204", description = "Imagen eliminada")
	@ApiResponse(responseCode = "404", description = "Imagen no encontrada")
	public void eliminar(@PathVariable String nombre) {
		imagenService.eliminar(nombre);
	}
}
