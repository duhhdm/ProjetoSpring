package com.eduestudo.cursomc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduestudo.cursomc.domain.Cidade;
import com.eduestudo.cursomc.domain.Cliente;
import com.eduestudo.cursomc.domain.Endereco;
import com.eduestudo.cursomc.domain.enums.TipoCliente;
import com.eduestudo.cursomc.dto.ClienteDTO;
import com.eduestudo.cursomc.dto.ClienteNewDTO;
import com.eduestudo.cursomc.repositories.ClienteRepository;
import com.eduestudo.cursomc.repositories.EnderecoRepository;
import com.eduestudo.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	ClienteRepository repo;
	@Autowired
	EnderecoRepository repoE;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> aux = repo.findById(id);
		return aux.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", Tipo:"+Cliente.class.getName(), null));
	}
	
	public List<Cliente> buscarTudo(){
		return repo.findAll();
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		repoE.saveAll(obj.getEndereco());
		return repo.save(obj);
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new com.eduestudo.cursomc.service.exceptions.DataIntegrityViolationException("Não é possivel excluir porque há entidades relacionadas");
		}
		
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(),obj.getNome(),obj.getEmail(),null,null);
	}

public Cliente fromDTO(ClienteNewDTO obj) {
		
		Cliente cli=new Cliente(null,obj.getNome(),obj.getEmail(),obj.getCpfOuCnpj(),TipoCliente.toEnum(obj.getTipo()));
		Cidade cid = new Cidade(obj.getCidadeId(),null,null);
		Endereco end = new Endereco(null,obj.getLogradouro(),obj.getNumero(),obj.getComplemento(),obj.getBairro(),obj.getCep(),cli,cid);
		cli.getEndereco().add(end);
		cli.getTelefones().add(obj.getTelefone());
		if(obj.getTelefone2()!=null) {
			cli.getTelefones().add(obj.getTelefone2());
		}
		if(obj.getTelefone3()!=null) {
			cli.getTelefones().add(obj.getTelefone3());
		}
		return cli;
		
	}
	
	private void updateData(Cliente newObj, Cliente Obj) {
		
		newObj.setNome(Obj.getNome());
		newObj.setEmail(Obj.getEmail());		
	}
}
