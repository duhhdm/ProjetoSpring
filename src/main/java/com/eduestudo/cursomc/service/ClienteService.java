package com.eduestudo.cursomc.service;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eduestudo.cursomc.domain.Cidade;
import com.eduestudo.cursomc.domain.Cliente;
import com.eduestudo.cursomc.domain.Endereco;
import com.eduestudo.cursomc.domain.enums.Perfil;
import com.eduestudo.cursomc.domain.enums.TipoCliente;
import com.eduestudo.cursomc.dto.ClienteDTO;
import com.eduestudo.cursomc.dto.ClienteNewDTO;
import com.eduestudo.cursomc.repositories.ClienteRepository;
import com.eduestudo.cursomc.repositories.EnderecoRepository;
import com.eduestudo.cursomc.security.UserSS;
import com.eduestudo.cursomc.service.exceptions.AuthorizationException;
import com.eduestudo.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	ClienteRepository repo;
	
	@Autowired
	EnderecoRepository repoE;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	String prefix;
	
	public Cliente buscar(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId()))
			throw new AuthorizationException("Acesso negado!");
			
		
		Optional<Cliente> aux = repo.findById(id);
		return aux.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", Tipo:"+Cliente.class.getName(), null));
	}
	
	public List<Cliente> buscarTudo(){
		return repo.findAll();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = repo.findByEmail(email);
		if(obj==null)
			throw new ObjectNotFoundException("Objeto não encontrado! ID: "+ user.getId() + 
					", Tipo: " + Cliente.class.getName());
		return obj;
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
		return new Cliente(obj.getId(),obj.getNome(),obj.getEmail(),null,null,null);
	}

public Cliente fromDTO(ClienteNewDTO obj) {
		
		Cliente cli=new Cliente(null,obj.getNome(),obj.getEmail(),obj.getCpfOuCnpj(),TipoCliente.toEnum(obj.getTipo()),pe.encode(obj.getSenha()));
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
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		
		if(user==null)
			throw new AuthorizationException("Acesso negado!");
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = prefix + user.getId() + ".jpg";
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"),fileName,"image");
	}
}
