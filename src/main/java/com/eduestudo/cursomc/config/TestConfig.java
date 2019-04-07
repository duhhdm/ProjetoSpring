package com.eduestudo.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.eduestudo.cursomc.service.DBService;

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
}
