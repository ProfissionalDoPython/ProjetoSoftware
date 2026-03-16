package com.bancodetalentos.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe de modelo que representa o perfil detalhado de um usuário.
 * Mapeada para a tabela 'perfis' no banco de dados.
 */
@Entity
@Table(name = "perfis")
public class Perfis {

    /**
     * O CPF é a Chave Primária (@Id) e o elo de ligação
     * com a tabela 'students' (a tabela de Contas).
     */
    @Id
    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Column(name = "nome_completo", nullable = false)
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "instituicao")
    private String instituicao;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "sobre", length = 500) // Corresponde ao maxlength do formulário
    private String sobre;

    @Column(name = "tipo_conta")
    private String tipoConta;

    /**
     * Construtor padrão vazio.
     * Essencial para o funcionamento do JPA.
     */
    public Perfis() {
    }

    // --- Getters and Setters for all fields ---

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSobre() {
        return sobre;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }
}