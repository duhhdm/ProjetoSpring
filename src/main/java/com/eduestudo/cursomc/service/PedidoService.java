package com.eduestudo.cursomc.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduestudo.cursomc.domain.Cliente;
import com.eduestudo.cursomc.domain.ItemPedido;
import com.eduestudo.cursomc.domain.PagamentoComBoleto;
import com.eduestudo.cursomc.domain.Pedido;
import com.eduestudo.cursomc.domain.enums.EstadoPagamento;
import com.eduestudo.cursomc.repositories.ClienteRepository;
import com.eduestudo.cursomc.repositories.ItemPedidoRepository;
import com.eduestudo.cursomc.repositories.PagamentoRepository;
import com.eduestudo.cursomc.repositories.PedidoRepository;
import com.eduestudo.cursomc.security.UserSS;
import com.eduestudo.cursomc.service.exceptions.AuthorizationException;
import com.eduestudo.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class PedidoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired //esta referencia sera automaticamente instanciada pelo ispring
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagRepo;
	
	@Autowired
	private ProdutoService proRepo;
	
	@Autowired
	private ItemPedidoRepository itemPedRepo;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> aux = repo.findById(id);
		return aux.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", Tipo:"+Pedido.class.getName(), null));
	}
	
	@Transactional
	public Pedido Insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.buscar(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj=repo.save(obj);
		pagRepo.save(obj.getPagamento());
		for(ItemPedido ip:obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(proRepo.buscar(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		itemPedRepo.saveAll(obj.getItens());
		emailService.emailConfirmacao(obj);
		//emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		
		if(user==null)
			throw new AuthorizationException("Acesso negado!");
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.buscar(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}