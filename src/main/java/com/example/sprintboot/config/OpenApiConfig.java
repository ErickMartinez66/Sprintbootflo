package com.example.sprintboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI floreriaOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("API Florería")
						.description("API para gestionar pedidos de flores, sus clientes y los costos de creación.")
						.version("v1.0.0")
						.contact(new Contact()
								.name("Sprint Boot")
								.email("floreria@example.com")));
	}
}