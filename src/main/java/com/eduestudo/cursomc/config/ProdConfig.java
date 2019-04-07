package com.eduestudo.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.eduestudo.cursomc.service.DBService;

@Configuration
@Profile("prod")
public class ProdConfig {
	@Autowired
	DBService dbservice;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instatiateDataBase() throws ParseException {
		if(!strategy.equals("create"))
			return false;
		else {
			dbservice.instatiateTestDataBase();
			return true;
		}
	}
}
