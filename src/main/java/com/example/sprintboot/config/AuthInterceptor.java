package com.example.sprintboot.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.sprintboot.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private final AuthService authService;

	public AuthInterceptor(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		if (HttpMethod.OPTIONS.matches(request.getMethod()) || esPublico(request)) {
			return true;
		}

		String token = AuthService.extraerToken(request.getHeader(HttpHeaders.AUTHORIZATION));
		if (authService.validar(token)) {
			return true;
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write("{\"message\":\"No autorizado\"}");
		return false;
	}

	private boolean esPublico(HttpServletRequest request) {
		String ruta = request.getRequestURI().substring(request.getContextPath().length());
		String metodo = request.getMethod();

		if (HttpMethod.POST.matches(metodo)) {
			return "/api/auth/login".equals(ruta)
					|| "/api/auth/logout".equals(ruta)
					|| "/api/pedidos".equals(ruta);
		}

		if (HttpMethod.GET.matches(metodo)) {
			return "/api/arreglos/activos".equals(ruta)
					|| "/api/colores/activos".equals(ruta)
					|| "/api/accesorios".equals(ruta);
		}

		return false;
	}
}
