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

import com.projeto.sistemaCantinhoLeitura.modelos.Fregues;
import com.projeto.sistemaCantinhoLeitura.repositorios.FreguesRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.CidadeRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class FreguesControle {
	
	@Autowired
	private FreguesRepositorio freguesRepositorio;
	@Autowired
	private CidadeRepositorio cidadeRepositorio;

	
	
	@GetMapping("/registroFregues")
	public ModelAndView cadastrar(Fregues fregues) {
		ModelAndView mv = new ModelAndView("gestao/fregueses/registro");
		
	    mv.addObject("fregues",fregues);
	    mv.addObject("listaCidades",cidadeRepositorio.findAll());
		return mv;
	}
	
	@PostMapping("/fregues/salvar")
	public ModelAndView salvar(Fregues fregues, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(fregues);
		}
		
		freguesRepositorio.saveAndFlush(fregues);
		return cadastrar(new Fregues());
	}
	
	@GetMapping("/listarFregueses")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/fregueses/lista");
		mv.addObject("listaFregueses", freguesRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/fregues/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Fregues> fregues = freguesRepositorio.findById(id);
		return cadastrar(fregues.get());
	}
	
	@GetMapping("fregues/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Fregues> fregues = freguesRepositorio.findById(id);
		freguesRepositorio.delete(fregues.get());
		return listar();
		
	}
	
	


}

