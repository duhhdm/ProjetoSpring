package com.eduestudo.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoErro extends StandardError {
	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> erro = new ArrayList<>();
	
	public ValidacaoErro(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
		// TODO Auto-generated constructor stub
	}

	public List<FieldMessage> getErro() {
		return erro;
	}

	public void addError(String fieldName,String message) {
		erro.add(new FieldMessage(fieldName,message));
	}
	
	
}
