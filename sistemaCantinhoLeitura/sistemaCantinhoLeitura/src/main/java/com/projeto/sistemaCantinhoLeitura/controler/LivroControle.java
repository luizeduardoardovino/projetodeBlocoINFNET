package com.projeto.sistemaCantinhoLeitura.controler;


import java.util.Optional;		
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistemaCantinhoLeitura.modelos.Livro;
import com.projeto.sistemaCantinhoLeitura.repositorios.LivroRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class LivroControle {
	
	@Autowired
	private LivroRepositorio livroRepositorio;
	
	@GetMapping("/registroLivro")
	public ModelAndView cadastrar(Livro livro) {
		ModelAndView mv = new ModelAndView("gestao/livros/registro");

	    mv.addObject("livro",livro);
		return mv;
	}
	
	@PostMapping("livro/salvar")
	public ModelAndView salvar(Livro livro, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(livro);
		}
		
		livroRepositorio.saveAndFlush(livro);
		return cadastrar(new Livro());
	}
	
	@GetMapping("/livros/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/livros/lista");
		mv.addObject("listaLivros", livroRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/livro/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Livro> livro = livroRepositorio.findById(id);
		return cadastrar(livro.get());
	}
	
	@GetMapping("/livro/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Livro> livro = livroRepositorio.findById(id);
		livroRepositorio.delete(livro.get());
		return listar();
		
	}
	
	


}