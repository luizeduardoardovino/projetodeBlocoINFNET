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

import com.projeto.sistemaCantinhoLeitura.modelos.Cidade;
import com.projeto.sistemaCantinhoLeitura.repositorios.CidadeRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.EstadoRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class CidadeControle {
	
	@Autowired
	private CidadeRepositorio cidadeRepositorio;
	@Autowired
	private EstadoRepositorio estadoRepositorio;

	
	
	@GetMapping("/registroCidade")
	public ModelAndView cadastrar(Cidade cidade) {
		ModelAndView mv = new ModelAndView("gestao/cidades/registro");
		
	    mv.addObject("cidade",cidade);
	    mv.addObject("listaEstados",estadoRepositorio.findAll());
		return mv;
	}
	
	@PostMapping("/cidades/salvar")
	public ModelAndView salvar(Cidade cidade, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(cidade);
		}
		
		cidadeRepositorio.saveAndFlush(cidade);
		return cadastrar(new Cidade());
	}
	
	@GetMapping("/cidades/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/cidades/lista");
		mv.addObject("listaCidades", cidadeRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/cidades/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Cidade> cidade = cidadeRepositorio.findById(id);
		return cadastrar(cidade.get());
	}
	
	@GetMapping("/cidades/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Cidade> cidade = cidadeRepositorio.findById(id);
		cidadeRepositorio.delete(cidade.get());
		return listar();
		
	}
	
	


}
