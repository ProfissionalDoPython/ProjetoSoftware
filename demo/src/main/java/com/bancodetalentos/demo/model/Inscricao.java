package com.bancodetalentos.demo.model;

import java.util.List; // Para a lista de certificações, se necessário

/**
 * Classe de modelo que representa uma Inscrição de Usuário em uma Oportunidade (Estágio, Bolsa, Processo, Evento).
 * Usada para mapear os dados do arquivo JSON de inscrições.
 */
public class Inscricao {
    private String id; // ID único da inscrição (gerado no backend para garantir unicidade)
    private String titulo; // Título da oportunidade a qual o usuário se inscreveu (ex: "Estágio em Engenharia Civil")
    private String nome; // Nome completo do inscrito
    private String email; // E-mail do inscrito
    private String cpf; // CPF do inscrito (opcional, pode ser vazio)
    private String telefone; // Telefone do inscrito (opcional, pode ser vazio)
    private String mensagem; // Mensagem/dúvidas do inscrito (opcional)
    private String curriculo; // Nome do arquivo do currículo (apenas o nome do arquivo, ex: "curriculo_joao.pdf")
    private List<String> certificacoes; // Nomes dos arquivos das certificações (ex: ["cert1.pdf", "cert2.jpg"])
    private String dataInscricao; // Data e hora da inscrição (gerado no backend)

    public Inscricao() {
    }

    public Inscricao(String id, String titulo, String nome, String email, String cpf, String telefone, String mensagem, String curriculo, List<String> certificacoes, String dataInscricao) {
        this.id = id;
        this.titulo = titulo;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.mensagem = mensagem;
        this.curriculo = curriculo;
        this.certificacoes = certificacoes;
        this.dataInscricao = dataInscricao;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }

    public List<String> getCertificacoes() {
        return certificacoes;
    }

    public void setCertificacoes(List<String> certificacoes) {
        this.certificacoes = certificacoes;
    }

    public String getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(String dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
}