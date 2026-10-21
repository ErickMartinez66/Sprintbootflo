package com.example.sprintboot.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenService {

	public static final String RUTA_BASE = "/media";

	private static final String PREFIJO = "arreglo-";
	private static final Pattern NOMBRE_VALIDO = Pattern.compile("^[a-zA-Z0-9-]+\\.[a-zA-Z0-9]+$");
	private static final Map<String, String> EXTENSIONES = Map.of(
			"image/jpeg", ".jpg",
			"image/jpg", ".jpg",
			"image/png", ".png",
			"image/webp", ".webp",
			"image/gif", ".gif",
			"image/avif", ".avif");

	private final Path carpeta;

	public ImagenService(@Value("${app.media.dir:./media}") String directorio) {
		this.carpeta = Paths.get(directorio).toAbsolutePath().normalize();
		try {
			Files.createDirectories(carpeta);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo preparar la carpeta de imágenes: " + e.getMessage(), e);
		}
	}

	public String subir(MultipartFile archivo) {
		if (archivo == null || archivo.isEmpty()) {
			throw new RuntimeException("No se recibió ningún archivo de imagen");
		}
		String tipo = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase();
		String extension = EXTENSIONES.get(tipo);
		if (extension == null) {
			throw new RuntimeException("Formato no permitido. Sube una imagen JPG, PNG, WEBP, GIF o AVIF");
		}
		String nombre = PREFIJO + UUID.randomUUID() + extension;
		try (InputStream entrada = archivo.getInputStream()) {
			Files.copy(entrada, carpeta.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo guardar la imagen: " + e.getMessage(), e);
		}
		return nombre;
	}

	public Path resolver(String nombre) {
		if (nombre == null || !NOMBRE_VALIDO.matcher(nombre).matches() || nombre.contains("..")) {
			throw new RuntimeException("Nombre de imagen inválido");
		}
		Path ruta = carpeta.resolve(nombre).normalize();
		if (!ruta.startsWith(carpeta)) {
			throw new RuntimeException("Nombre de imagen inválido");
		}
		return ruta;
	}

	public void eliminar(String nombre) {
		try {
			Files.deleteIfExists(resolver(nombre));
		} catch (IOException e) {
			throw new RuntimeException("No se pudo eliminar la imagen: " + e.getMessage(), e);
		}
	}

	public void eliminarDesdeUrl(String url) {
		String nombre = nombreDesdeUrl(url);
		if (nombre != null) {
			eliminar(nombre);
		}
	}

	public String nombreDesdeUrl(String url) {
		if (url == null || !url.startsWith(RUTA_BASE + "/")) {
			return null;
		}
		String nombre = url.substring(RUTA_BASE.length() + 1);
		return NOMBRE_VALIDO.matcher(nombre).matches() && !nombre.contains("..") ? nombre : null;
	}

	public String urlDe(String nombre) {
		return RUTA_BASE + "/" + nombre;
	}
}
