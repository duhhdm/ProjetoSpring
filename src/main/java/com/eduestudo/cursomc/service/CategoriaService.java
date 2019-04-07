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

import com.eduestudo.cursomc.domain.Categoria;
import com.eduestudo.cursomc.dto.CategoriaDTO;
import com.eduestudo.cursomc.repositories.CategoriaRepository;
import com.eduestudo.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> aux = repo.findById(id);
		return aux.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", Tipo:"+Categoria.class.getName(), null));
	}
	
	public List<Categoria> buscarTudo(){
		return repo.findAll();
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		Categoria newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new com.eduestudo.cursomc.service.exceptions.DataIntegrityViolationException("Não é possivel excluir uma categoria que possui produtos");
		}
		
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		
	}
	
	public Categoria fromDTO(CategoriaDTO obj) {
		return new Categoria(obj.getId(), obj.getNome());
	}
	
private void updateData(Categoria newObj, Categoria Obj) {
		
		newObj.setNome(Obj.getNome());	
	}
}
