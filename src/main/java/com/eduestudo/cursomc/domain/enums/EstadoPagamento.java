package com.eduestudo.cursomc.domain.enums;

public enum EstadoPagamento {
	PENDENTE(1,"pendente"),
	QUITADO(2,"quitado"),
	CANCELADO(3,"cancelado");
	
	private int cod;
	private String desc;
	
	private EstadoPagamento(int cod, String desc) {
		this.cod=cod;
		this.desc=desc;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static EstadoPagamento toEnum(Integer cod) {
		if(cod==null)
			return null;
		for(EstadoPagamento x : EstadoPagamento.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		throw new IllegalArgumentException("ID inválido: "+ cod);
	}
}
