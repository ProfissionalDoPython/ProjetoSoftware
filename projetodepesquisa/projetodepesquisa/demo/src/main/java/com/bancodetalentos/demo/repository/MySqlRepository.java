package com.bancodetalentos.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancodetalentos.demo.model.contas;

// Esse metodo aqui permiti a edição dos elementos nas tabelas do banco de dados
public interface MySqlRepository extends JpaRepository<contas, Integer> {
}
