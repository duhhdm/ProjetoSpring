package com.eduestudo.cursomc.resources;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eduestudo.cursomc.domain.Produto;
import com.eduestudo.cursomc.dto.ProdutoDTO;
import com.eduestudo.cursomc.resources.utils.URL;
import com.eduestudo.cursomc.service.ProdutoService;

@RestController //Significa que está classe é um controlador REST
@RequestMapping(value="/produtos") //aqui você determina em que url será aberta no servidor
public class ProdutoResource {
	
	@Autowired
	private ProdutoService servico ;
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> find (@PathVariable Integer id){
		Produto obj = servico.buscar(id);
		return ResponseEntity.ok().body(obj);
		
	}
	
	@RequestMapping( method=RequestMethod.GET)
	public ResponseEntity<?> findPage (
			@RequestParam(value="nome", defaultValue="0") String nome,
			@RequestParam(value="categorias", defaultValue="0") String categorias,
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		String nomeDecode = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> list = servico.search(nomeDecode, ids, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> objDTO = list.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(objDTO);
	}
	
}
