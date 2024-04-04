package com.projeto.sistemaCantinhoLeitura.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;	
import org.springframework.data.jpa.repository.Query;

import com.projeto.sistemaCantinhoLeitura.modelos.Recebimento;



public interface RecebimentoRepositorio extends JpaRepository<Recebimento, Long>{
	


}
