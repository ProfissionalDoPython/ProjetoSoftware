package com.bancodetalentos.demo.model;

//  As minhas importações
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

// Importações que o JPA usa no nosso projeto
@Entity
@Table(name = "contas")
public class contas {

    // A classe "contas" e o que representa uma entidade JPA que será mapeada para a tabela "contas" no banco de dado
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String Senha;
    private String Email;
    private Integer CPF;

    public contas() {
        // Esse construtor vazio aqui e para não dar exceção quando o JPA tentar instanciar a classe
    }

    //Aquele construtor padrão mesmo
    public contas(Integer Id, String Senha, String Email, Integer CPF) {
        this.Id = Id;
        this.Senha = Senha;
        this.Email = Email;
        this.CPF = CPF;
    }

    //Meus gets and sets
    public Integer getId() {
        return Id;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String Senha) {
        this.Senha = Senha;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public Integer getCpf() {
        return CPF;
    }

    public void setCpf(Integer CPF) {
        this.CPF = CPF;
    }
}
