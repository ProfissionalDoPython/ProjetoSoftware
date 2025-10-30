package com.bancodetalentos.demo.model;

/**
 * Classe de modelo que representa uma Oportunidade de Bolsa Acadêmica (Iniciação Científica, Mestrado, Doutorado, etc.).
 * Usada para mapear os dados do arquivo JSON e para a comunicação entre o back-end e o front-end.
 */
public class Bolsa {
    private String id; // Um identificador único para a bolsa.
    private String titulo; // Título da oportunidade de bolsa (ex: "Bolsa de Iniciação Científica - CNPq").
    private String valor; // Valor mensal da bolsa (ex: "R$ 700/mês").
    private String duracaoMeses; // Duração da bolsa em meses (ex: "12 meses").
    private String publicoAlvo; // Público alvo da bolsa (ex: "Alunos de graduação", "Alunos de mestrado").
    private String descricao; // Descrição detalhada da bolsa.
    private String requisitos; // Requisitos para a candidatura (ex: "CRG > 7.0").
    private String beneficios; // Outros benefícios (ex: "Apoio a publicações").
    private String area; // Área do conhecimento (ex: "Tecnologia", "Educação", "Pesquisa").
    private String modalidade; // Modalidade (ex: "Presencial", "Online", "Hibrido").
    private String mesInicio; // Mês de início da bolsa (para fins de filtro).
    private String linkInscricao; // URL para o formulário de inscrição (será para inscricao.html).

    public Bolsa() {
    }

    public Bolsa(String id, String titulo, String valor, String duracaoMeses, String publicoAlvo, String descricao,
                 String requisitos, String beneficios, String area, String modalidade, String mesInicio, String linkInscricao) {
        this.id = id;
        this.titulo = titulo;
        this.valor = valor;
        this.duracaoMeses = duracaoMeses;
        this.publicoAlvo = publicoAlvo;
        this.descricao = descricao;
        this.requisitos = requisitos;
        this.beneficios = beneficios;
        this.area = area;
        this.modalidade = modalidade;
        this.mesInicio = mesInicio;
        this.linkInscricao = linkInscricao;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(String duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    public String getPublicoAlvo() {
        return publicoAlvo;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getLinkInscricao() {
        return linkInscricao;
    }

    public void setLinkInscricao(String linkInscricao) {
        this.linkInscricao = linkInscricao;
    }
}