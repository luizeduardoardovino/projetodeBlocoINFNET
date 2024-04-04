package com.projeto.sistemaCantinhoLeitura.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.data.jpa.repository.Query;

import com.projeto.sistemaCantinhoLeitura.modelos.Cidade;



public interface CidadeRepositorio extends JpaRepository<Cidade, Long>{
	



}