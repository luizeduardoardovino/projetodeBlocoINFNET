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

import com.projeto.sistemaCantinhoLeitura.modelos.Editora;
import com.projeto.sistemaCantinhoLeitura.repositorios.EditoraRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.CidadeRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class EditoraControle {
	
	@Autowired
	private EditoraRepositorio editoraRepositorio;
	@Autowired
	private CidadeRepositorio cidadeRepositorio;

	
	
	@GetMapping("/registroEditora")
	public ModelAndView cadastrar(Editora editora) {
		ModelAndView mv = new ModelAndView("gestao/editoras/registro");
		
	    mv.addObject("editora",editora);
	    mv.addObject("listaCidades",cidadeRepositorio.findAll());
		return mv;
	}
	
	@PostMapping("/editora/salvar")
	public ModelAndView salvar(Editora editora, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(editora);
		}
		
		editoraRepositorio.saveAndFlush(editora);
		return cadastrar(new Editora());
	}
	
	@GetMapping("/editoras/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/editoras/lista");
		mv.addObject("listaEditoras", editoraRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/editora/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Editora> editora = editoraRepositorio.findById(id);
		return cadastrar(editora.get());
	}
	
	@GetMapping("/editora/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Editora> editora = editoraRepositorio.findById(id);
		editoraRepositorio.delete(editora.get());
		return listar();
		
	}
	
	


}
