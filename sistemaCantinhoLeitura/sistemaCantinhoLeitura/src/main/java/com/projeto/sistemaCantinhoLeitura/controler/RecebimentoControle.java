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

import com.projeto.sistemaCantinhoLeitura.modelos.Recebimento;
import com.projeto.sistemaCantinhoLeitura.modelos.UnidadeRecebimento;
import com.projeto.sistemaCantinhoLeitura.modelos.Editora;
import com.projeto.sistemaCantinhoLeitura.modelos.Livro;
import com.projeto.sistemaCantinhoLeitura.repositorios.RecebimentoRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.EditoraRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.VendedorRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.LivroRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.UnidadeRecebimentoRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class RecebimentoControle {
	
	@Autowired
	private RecebimentoRepositorio recebimentoRepositorio;
	@Autowired
	private UnidadeRecebimentoRepositorio unidadeRecebimentoRepositorio;
	@Autowired
	private LivroRepositorio livroRepositorio;
	@Autowired
	private VendedorRepositorio vendedorRepositorio;	
	@Autowired
	private EditoraRepositorio editoraRepositorio;	
	@Autowired
	private RecebimentoRepositorio RecebimentoRepositorio;	
	
	private List<UnidadeRecebimento> listaUnidadeRecebimento = new ArrayList<UnidadeRecebimento>();
	private Long cont = 0L;
	
	
	
	
	@GetMapping("/registroRecebimento")
	public ModelAndView cadastrar(Recebimento recebimento, UnidadeRecebimento unidadeRecebimento) {
		ModelAndView mv = new ModelAndView("gestao/recebimentos/registro");
		
	    mv.addObject("recebimento",recebimento);
	    mv.addObject("unidadeRecebimento",unidadeRecebimento);
	    mv.addObject("listaunidadeRecebimento",this.listaUnidadeRecebimento);
	    mv.addObject("listaVendedores", vendedorRepositorio.findAll());
	    mv.addObject("listaEditoras", editoraRepositorio.findAll());
	    mv.addObject("listaLivros", livroRepositorio.findAll());	    
	    
		return mv;
	}
	
	@PostMapping("/recebimento/salvar")
	public ModelAndView salvar(String acao, Recebimento recebimento, UnidadeRecebimento unidadeRecebimento, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(recebimento,unidadeRecebimento);
		}
		
		if(acao.equals("itens")) {
			cont=cont+1;
			unidadeRecebimento.setId(cont);
			this.listaUnidadeRecebimento.add(unidadeRecebimento);
			recebimento.setValorTotal(recebimento.getValorTotal()+(unidadeRecebimento.getValor())*unidadeRecebimento.getQuantidade());
			recebimento.setQuantidadeTotal(recebimento.getQuantidadeTotal()+unidadeRecebimento.getQuantidade());
			
		}else if(acao.equals("salvar")) {
			recebimentoRepositorio.saveAndFlush(recebimento);
			
			for(UnidadeRecebimento it : listaUnidadeRecebimento) {
				it.setRecebimento(recebimento);
				//unidadeRecebimentoRepositorio.saveAndFlush(it);
				
				Optional<Livro> prod = livroRepositorio.findById(it.getLivro().getId());
				Livro livro = prod.get();
				livro.setEstoque(livro.getEstoque()+it.getQuantidade());
				livro.setPrecoVenda(it.getValor());
				livro.setPrecoCusto(it.getValorCusto());
				livroRepositorio.saveAndFlush(livro);
				
				this.listaUnidadeRecebimento = new ArrayList<>();
				
			}
			
			return cadastrar(new Recebimento(), new UnidadeRecebimento());
			
		}
		
		return cadastrar(recebimento, new UnidadeRecebimento());
		
	
	}
	
	
	@GetMapping("/recebimentos/listar")
	public ModelAndView listar() {
		
		ModelAndView mv = new ModelAndView("gestao/recebimentos/lista");
		mv.addObject("listaRecebimentos", recebimentoRepositorio.findAll());

		return mv;
	}
	
	
	
	@GetMapping("/recebimento/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id ) {
		Optional<Recebimento> recebimento = recebimentoRepositorio.findById(id);
		this.listaUnidadeRecebimento= unidadeRecebimentoRepositorio.buscarPorRecebimento(id);
		
		return cadastrar(recebimento.get(), new UnidadeRecebimento());
	} 
	

	
 	@GetMapping("/unidadeRecebimento/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id, Recebimento recebimento, UnidadeRecebimento unidadeRecebimento, BindingResult result) {
 		
 		listaUnidadeRecebimento.removeIf(item -> item.getId() == id);
 		System.out.println("Valor total da recebimento aqui: "+recebimento.getValorTotal());	
	
		for(UnidadeRecebimento it : listaUnidadeRecebimento) {
			recebimento.setValorTotal(recebimento.getValorTotal()+(it.getValor())*it.getQuantidade());
			recebimento.setQuantidadeTotal(recebimento.getQuantidadeTotal()+it.getQuantidade());
		}
		
		return cadastrar(recebimento, unidadeRecebimento);
	  }
 	
 	
	@GetMapping("/recebimento/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Recebimento> recebimento = RecebimentoRepositorio.findById(id);
		recebimentoRepositorio.delete(recebimento.get());
		return listar();
		
	}


	public List<UnidadeRecebimento> getListaUnidadeRecebimento() {
		return listaUnidadeRecebimento;
	}

	public void setListaUnidadeRecebimento(List<UnidadeRecebimento> listaUnidadeRecebimento) {
		this.listaUnidadeRecebimento = listaUnidadeRecebimento;
	}
	
	


}
