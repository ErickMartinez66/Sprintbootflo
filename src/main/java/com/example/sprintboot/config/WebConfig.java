package com.example.sprintboot.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${app.media.dir:./media}")
	private String mediaDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path carpeta = Paths.get(mediaDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(carpeta);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo crear la carpeta de medios: " + e.getMessage(), e);
		}
		registry.addResourceHandler("/media/**")
				.addResourceLocations("file:" + carpeta + "/")
				.setCacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic());
	}
}
