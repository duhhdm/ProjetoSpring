package com.eduestudo.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.eduestudo.cursomc.service.DBService;
import com.eduestudo.cursomc.service.EmailService;
import com.eduestudo.cursomc.service.MockEmailService;
import com.eduestudo.cursomc.service.SmtpEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	@Autowired
	DBService dbservice;
	
	@Bean
	public boolean instatiateDataBase() throws ParseException {
		
		dbservice.instatiateTestDataBase();
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
	
	/*@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}*/
}
