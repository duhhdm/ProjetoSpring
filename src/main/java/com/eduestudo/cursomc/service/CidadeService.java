package com.eduestudo.cursomc.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduestudo.cursomc.domain.Cidade;
import com.eduestudo.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	CidadeRepository repo;
	
	public List<Cidade> findByEstado(Integer id){
		return repo.findCidades(id);
	}
}
