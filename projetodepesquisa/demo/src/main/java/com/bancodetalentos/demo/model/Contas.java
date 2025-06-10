package com.bancodetalentos.demo.model;

public class Contas {
    private String senha;
    private String email;
    private String cpf;
    private String tipoPerfil; // NOVO CAMPO ADICIONADO

    public Contas() {
        // e o meu construtor padrão vazio, importante para o Spring e Gson
    }

    public Contas(String senha, String email, String cpf, String tipoPerfil) { 
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.tipoPerfil = tipoPerfil; 
    }

    // meus getters and setter do sistema
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    // uso esse  metodo para verificar a senha
    public boolean verificarSenha(String senhaDigitada) {
        return this.senha != null && this.senha.equals(senhaDigitada);
    }
}