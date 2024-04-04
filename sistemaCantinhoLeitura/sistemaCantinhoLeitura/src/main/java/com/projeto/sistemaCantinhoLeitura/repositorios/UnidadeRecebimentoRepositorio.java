package com.projeto.sistemaCantinhoLeitura.repositorios;

import java.util.List;	

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.sistemaCantinhoLeitura.modelos.UnidadeRecebimento;



public interface UnidadeRecebimentoRepositorio extends JpaRepository<UnidadeRecebimento, Long>{
	
	@Query("SELECT e FROM UnidadeRecebimento e WHERE e.recebimento.id = ?1")
	List<UnidadeRecebimento>buscarPorRecebimento(long id);


}
