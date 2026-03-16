package com.bancodetalentos.demo.repository;

import com.bancodetalentos.demo.model.Perfis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfis, String> {
    // By extending JpaRepository, we get .save(), .findById(), etc. for free.
    // Note: The ID type is <Perfis, String> because the @Id (CPF) is a String.
}