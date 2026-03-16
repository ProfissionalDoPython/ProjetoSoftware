package com.bancodetalentos.demo.repository;

import com.bancodetalentos.demo.model.Contas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContasRepository extends JpaRepository<Contas, Long> {
    Optional<Contas> findByEmail(String email);
    Optional<Contas> findByCpf(String cpf);

}