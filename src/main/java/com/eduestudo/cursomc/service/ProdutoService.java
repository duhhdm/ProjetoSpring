package com.eduestudo.cursomc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.eduestudo.cursomc.domain.Categoria;
import com.eduestudo.cursomc.domain.Produto;
import com.eduestudo.cursomc.repositories.CategoriaRepository;
import com.eduestudo.cursomc.repositories.ProdutoRepository;
import com.eduestudo.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	ProdutoRepository repo;
	
	@Autowired
	CategoriaRepository repoCat;
	
	public Produto buscar(Integer id) {
		Optional<Produto> aux = repo.findById(id);
		return aux.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", Tipo:"+Produto.class.getName(), null));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categoria = repoCat.findAllById(ids);
		return repo.search(nome,categoria,pageRequest);
	}
}
