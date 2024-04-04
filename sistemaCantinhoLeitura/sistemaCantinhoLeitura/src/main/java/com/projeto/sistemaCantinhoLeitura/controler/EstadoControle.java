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

import com.projeto.sistemaCantinhoLeitura.modelos.Estado;
import com.projeto.sistemaCantinhoLeitura.repositorios.EstadoRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class EstadoControle {
	
	@Autowired
	private EstadoRepositorio estadoRepositorio;
	
	@GetMapping("/registroEstado")
	public ModelAndView cadastrar(Estado estado) {
		ModelAndView mv = new ModelAndView("gestao/estados/registro");
		
	    mv.addObject("estado",estado);
		return mv;
	}
	
	@PostMapping("/estados/salvar")
	public ModelAndView salvar(Estado estado, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(estado);
		}
		
		estadoRepositorio.saveAndFlush(estado);
		return cadastrar(new Estado());
	}
	
	@GetMapping("/estados/listar")
	public ModelAndView listar() {
		System.out.println("Passei por listar estados");
		ModelAndView mv = new ModelAndView("gestao/estados/lista");
		mv.addObject("listaEstados", estadoRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/administrativo/estados/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Estado> estado = estadoRepositorio.findById(id);
		return cadastrar(estado.get());
	}
	
	@GetMapping("/administrativo/estados/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Estado> estado = estadoRepositorio.findById(id);
		estadoRepositorio.delete(estado.get());
		return listar();
		
	}
	
	


}
