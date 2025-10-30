package com.bancodetalentos.demo.model;

/**
 * Classe de modelo que representa uma Oportunidade de Estágio.
 * Usada para mapear os dados do arquivo JSON e para a comunicação entre o back-end e o front-end.
 */
public class Estagio {
    private String id; // Um identificador único para o estágio.
    private String titulo; // Título da vaga de estágio.
    private String local; // Localização do estágio (cidade, estado).
    private String duracaoMeses; // Duração do estágio em meses.
    private String modalidade; // Modalidade (Presencial, Hibrido, Online).
    private String descricao; // Descrição detalhada da vaga.
    private String bolsa; // Valor da bolsa auxílio.
    private String cargaHoraria; // Carga horária semanal.
    private String beneficios; // Outros benefícios (ex: Vale-transporte, refeição).
    private String area; // Área do conhecimento (ex: "Engenharia", "Marketing", "Tecnologia", "Design").
    private String mesInicio; // Mês de início do estágio (para fins de filtro).
    private String linkInscricao; // URL para o formulário de inscrição externo ou interno.

    public Estagio() {
    }

    public Estagio(String id, String titulo, String local, String duracaoMeses, String modalidade, 
    String descricao, String bolsa, String cargaHoraria, String beneficios, String area, String mesInicio, String linkInscricao) {
        this.id = id;
        this.titulo = titulo;
        this.local = local;
        this.duracaoMeses = duracaoMeses;
        this.modalidade = modalidade;
        this.descricao = descricao;
        this.bolsa = bolsa;
        this.cargaHoraria = cargaHoraria;
        this.beneficios = beneficios;
        this.area = area;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(String duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getBolsa() {
        return bolsa;
    }

    public void setBolsa(String bolsa) {
        this.bolsa = bolsa;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
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