package com.projeto.sistemaCantinhoLeitura.controler;

import java.util.Optional;		
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistemaCantinhoLeitura.modelos.Venda;
import com.projeto.sistemaCantinhoLeitura.modelos.UnidadeVenda;
import com.projeto.sistemaCantinhoLeitura.modelos.Livro;
import com.projeto.sistemaCantinhoLeitura.repositorios.VendaRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.FreguesRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.VendedorRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.LivroRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.UnidadeVendaRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class VendaControle {
	
	@Autowired
	private VendaRepositorio vendaRepositorio;
	@Autowired
	private UnidadeVendaRepositorio unidadeVendaRepositorio;
	@Autowired
	private LivroRepositorio livroRepositorio;
	@Autowired
	private VendedorRepositorio vendedorRepositorio;	
	@Autowired
	private FreguesRepositorio freguesRepositorio;	
	
	private List<UnidadeVenda> listaUnidadeVenda = new ArrayList<UnidadeVenda>();
	
	
	@GetMapping("/registroVenda")
	public ModelAndView cadastrar(Venda venda, UnidadeVenda unidadeVenda) {
		ModelAndView mv = new ModelAndView("gestao/vendas/registro");
		
	    mv.addObject("venda",venda);
	    mv.addObject("unidadeVenda",unidadeVenda);
	    mv.addObject("listaUnidadeVenda",this.listaUnidadeVenda);
	    mv.addObject("listaVendedores", vendedorRepositorio.findAll());
	    mv.addObject("listaFregueses", freguesRepositorio.findAll());
	    mv.addObject("listaLivros", livroRepositorio.findAll());	    
	    
		return mv;
	}
	
	@PostMapping("/venda/salvar")
	public ModelAndView salvar(String acao, Venda venda, UnidadeVenda unidadeVenda, BindingResult result) { 
		if(result.hasErrors()) {
			
			return cadastrar(venda,unidadeVenda);
		}
		
		if(acao.equals("itens")) {
		
			unidadeVenda.setSubtotal(unidadeVenda.getValor()*unidadeVenda.getQuantidade());
			System.out.println(unidadeVenda.getQuantidade() + " " + unidadeVenda.getValor() + " subtotal: " + unidadeVenda.getSubtotal() );
			this.listaUnidadeVenda.add(unidadeVenda);
			System.out.println("Valor unid e Quantidade: " + unidadeVenda.getQuantidade() + " " + unidadeVenda.getValor() );
			venda.setValorTotal(venda.getValorTotal()+unidadeVenda.getSubtotal());
			venda.setQuantidadeTotal(venda.getQuantidadeTotal()+unidadeVenda.getQuantidade());
			
		}else if(acao.equals("salvar")) {
			vendaRepositorio.saveAndFlush(venda);
			
			for(UnidadeVenda it : listaUnidadeVenda) {
				it.setVenda(venda);
				unidadeVendaRepositorio.saveAndFlush(it);
				
				Optional<Livro> liv= livroRepositorio.findById(it.getLivro().getId());
				Livro livro = liv.get();
				livro.setEstoque(livro.getEstoque()-it.getQuantidade());
				livro.setPrecoVenda(it.getValor());
//				produto.setPrecoCusto(it.getValorCusto());
				livroRepositorio.saveAndFlush(livro);
				
				this.listaUnidadeVenda = new ArrayList<>();
				
			}
			
			return cadastrar(new Venda(), new UnidadeVenda());
			
		}
		
		
		
		return cadastrar(venda, new UnidadeVenda());
		
	
	}
	
	
	
	
	@GetMapping("/vendas/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/vendas/lista");
		mv.addObject("listaVendas", vendaRepositorio.findAll());
		return mv;
	}
	
	
	
	@GetMapping("/venda/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Venda> venda = vendaRepositorio.findById(id);
		this.listaUnidadeVenda= unidadeVendaRepositorio.buscarPorVenda(id);
		
		return cadastrar(venda.get(), new UnidadeVenda());
	} 
	

	
/* 	@GetMapping("/administrativo/vendas/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Venda> venda = vendaRepositorio.findById(id);
		vendaRepositorio.delete(venda.get());
		return listar();
		
	}

*/

	public List<UnidadeVenda> getListaItemVenda() {
		return listaUnidadeVenda;
	}

	public void setListaItemVenda(List<UnidadeVenda> listaUnidadeVenda) {
		this.listaUnidadeVenda = listaUnidadeVenda;
	}
	
	


}
