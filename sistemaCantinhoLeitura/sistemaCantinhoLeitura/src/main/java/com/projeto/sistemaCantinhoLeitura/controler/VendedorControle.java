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

import com.projeto.sistemaCantinhoLeitura.modelos.Vendedor;
import com.projeto.sistemaCantinhoLeitura.repositorios.VendedorRepositorio;
import com.projeto.sistemaCantinhoLeitura.repositorios.CidadeRepositorio;

import jakarta.annotation.PostConstruct;

@Controller
public class VendedorControle {
	
	@Autowired
	private VendedorRepositorio vendedorRepositorio;
	@Autowired
	private CidadeRepositorio cidadeRepositorio;

	
	
	@GetMapping("/registroVendedor")
	public ModelAndView cadastrar(Vendedor vendedor) {
		ModelAndView mv = new ModelAndView("gestao/vendedores/registro");
		
	    mv.addObject("vendedor",vendedor);
	    mv.addObject("listaCidades",cidadeRepositorio.findAll());
		return mv;
	}
	
	@PostMapping("/vendedor/salvar")
	public ModelAndView salvar(Vendedor vendedor, BindingResult result) { 
		if(result.hasErrors()) {
			return cadastrar(vendedor);
		}
		
		vendedorRepositorio.saveAndFlush(vendedor);
		return cadastrar(new Vendedor());
	}
	
	@GetMapping("/vendedores/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("gestao/vendedores/lista");
		mv.addObject("listaVendedores", vendedorRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/vendedor/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Vendedor> vendedor = vendedorRepositorio.findById(id);
		return cadastrar(vendedor.get());
	}
	
	@GetMapping("/vendedor/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Vendedor> vendedor = vendedorRepositorio.findById(id);
		vendedorRepositorio.delete(vendedor.get());
		return listar();
		
	}
	
	


}

