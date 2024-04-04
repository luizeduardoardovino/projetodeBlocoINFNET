package com.projeto.sistemaCantinhoLeitura.repositorios;

import java.util.List;	

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.sistemaCantinhoLeitura.modelos.UnidadeVenda;



public interface UnidadeVendaRepositorio extends JpaRepository<UnidadeVenda, Long>{
	
	@Query("SELECT e FROM UnidadeVenda e WHERE e.venda.id = ?1")
	List<UnidadeVenda>buscarPorVenda(long id);


}
