package com.eduestudo.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eduestudo.cursomc.domain.Cidade;
import com.eduestudo.cursomc.domain.Estado;
import com.eduestudo.cursomc.dto.CidadeDTO;
import com.eduestudo.cursomc.dto.EstadoDTO;
import com.eduestudo.cursomc.service.CidadeService;
import com.eduestudo.cursomc.service.EstadoService;

@RestController //Significa que está classe é um controlador REST
@RequestMapping(value="/estados") //aqui você determina em que url será aberta no servidor
public class EstadoResource {
	
	@Autowired
	private EstadoService servico ;
	
	@Autowired
	private CidadeService cidadeServico ;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<Estado> list = servico.buscarTudo();
		List<EstadoDTO> objDTO = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(objDTO);
	}
	
	@RequestMapping(value="/{estadoId}/cidades",method=RequestMethod.GET)
	public ResponseEntity<?> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> list = cidadeServico.findByEstado(estadoId);
		List<CidadeDTO> objDTO = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(objDTO);
	}
}
