package com.eduestudo.cursomc.service.validacao;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.eduestudo.cursomc.domain.Cliente;
import com.eduestudo.cursomc.domain.enums.TipoCliente;
import com.eduestudo.cursomc.dto.ClienteNewDTO;
import com.eduestudo.cursomc.repositories.ClienteRepository;
import com.eduestudo.cursomc.resources.exception.FieldMessage;
import com.eduestudo.cursomc.service.validacao.utils.ValidaCpfCnpj;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipo()==null) {
			list.add(new FieldMessage("tipo","Tipo não pode ser nulo"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !ValidaCpfCnpj.isValidSsn(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj","CPF invalido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !ValidaCpfCnpj.isValidTin(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj","CNPJ invalido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		
		if(aux!=null) {
			list.add(new FieldMessage("email","E-Mail ja existe"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
