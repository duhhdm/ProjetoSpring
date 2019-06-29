package com.eduestudo.cursomc.service;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduestudo.cursomc.domain.Estado;
import com.eduestudo.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	EstadoRepository repo;
	
	public ArrayList<Estado> buscarTudo(){
		return repo.findAllByOrderByNome();
	}
	
}
