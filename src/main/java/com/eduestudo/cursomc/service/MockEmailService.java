package com.eduestudo.cursomc.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);
	@Override
	public void enviaEmail(SimpleMailMessage msg) {
		LOG.info("simulando envio de email");
		LOG.info(msg.toString());
		LOG.info("email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		// TODO Auto-generated method stub
		LOG.info("simulando envio de email");
		LOG.info(msg.toString());
		LOG.info("email enviado");
	}
}
