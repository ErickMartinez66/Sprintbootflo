package com.example.sprintboot.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final String ASUNTO = "Su pedido de flores está listo";
	private static final String NOMBRE_MARCA = "Ady Bloom";
	private static final String LOGO_CID = "logo";
	private static final String TEMPLATE_PATH = "email/pedido-listado.html";
	private static final String LOGO_PATH = "email/logo.jpg";

	private final JavaMailSender mailSender;
	private final String remitente;

	public EmailService(JavaMailSender mailSender,
			@Value("${spring.mail.username}") String remitente) {
		this.mailSender = mailSender;
		this.remitente = remitente;
	}

	public void enviarPedidoListo(String destinatario) {
		MimeMessagePreparator preparator = mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
			helper.setTo(destinatario);
			helper.setFrom(remitente, NOMBRE_MARCA);
			helper.setSubject(ASUNTO);

			String html = new ClassPathResource(TEMPLATE_PATH).getContentAsString(StandardCharsets.UTF_8);
			helper.setText(html, true);
			helper.addInline(LOGO_CID, new ClassPathResource(LOGO_PATH));
		};
		mailSender.send(preparator);
	}
}