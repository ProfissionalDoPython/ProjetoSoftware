package com.bancodetalentos.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * Classe de modelo que representa uma conta de estudante no sistema.
 * Mapeada para a tabela 'Users' no banco de dados.
 */
@Entity
@Table(name = "students") // This matches your existing file
public class Contas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Makes the ID auto-increment
    @Column(name = "id") // Assumes your column is named 'id'
    private Long id;

    @Column(name = "cpf", unique = true) // Assumes column is 'cpf', marking as unique
    private String cpf;

    @Column(name = "status") // Assumes column is 'status'
    private Integer status;

    @Column(name = "password") // Renamed from 'senha', assumes column is 'password'
    private String password;

    @Column(name = "email", unique = true) // Assumes column is 'email', marking as unique
    private String email;

    /**
     * Construtor padrão vazio.
     * Essencial para JPA.
     */
    public Contas() {
        // Construtor padrão vazio, importante para JPA.
    }

    /**
     * Construtor completo para inicializar os campos da classe Contas.
     */
    public Contas(String cpf, Integer status, String password, String email) {
        this.cpf = cpf;
        this.status = status;
        this.password = password;
        this.email = email;
    }

    // --- Getters and Setters for all fields ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método para verificar se a senha fornecida corresponde à senha armazenada.
     *
     * @param senhaDigitada A senha digitada pelo usuário.
     * @return {@code true} se as senhas coincidirem, {@code false} caso contrário.
     */
    public boolean verificarPassword(String senhaDigitada) {
        // NOTE: For production, you should use Spring Security's PasswordEncoder.
        // For now, this simple check will work.
        return this.password != null && this.password.equals(senhaDigitada);
    }
}