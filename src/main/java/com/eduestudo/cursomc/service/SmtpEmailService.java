package com.eduestudo.cursomc.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	private MailSender mailSander;
	
	@Autowired
	private JavaMailSender javaMailSender;
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	@Override
	public void enviaEmail(SimpleMailMessage msg) {
		LOG.info("simulando envio de email");
		mailSander.send(msg);
		LOG.info("email enviado");
	}
	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("simulando envio de email");
		javaMailSender.send(msg);
		LOG.info("email enviado");
		
	}
	
}
