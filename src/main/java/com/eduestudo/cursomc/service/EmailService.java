package com.eduestudo.cursomc.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.eduestudo.cursomc.domain.Cliente;
import com.eduestudo.cursomc.domain.Pedido;

public interface EmailService {
	void emailConfirmacao(Pedido obj);
	
	void enviaEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);
	
	void sendPasswordEmail(Cliente cliente, String newPass) ;
}
