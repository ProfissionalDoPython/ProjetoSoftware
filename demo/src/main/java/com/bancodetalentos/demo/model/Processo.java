package com.bancodetalentos.demo.model;

/**
 * Classe de modelo que representa um Processo Seletivo.
 * Usada para mapear os dados do arquivo JSON e para a comunicação entre o back-end e o front-end.
 */
public class Processo {
    private String id; // Um identificador único para o processo seletivo.
    private String titulo; // Título do processo seletivo (ex: "Seleção para Mestrado em Computação").
    private String inscricoesAte; // Data limite para inscrições (ex: "15 de julho de 2025").
    private String etapas; // Descrição das etapas do processo (ex: "Prova escrita, análise de currículo...").
    private String area; // Área do conhecimento (ex: "Tecnologia", "Matemática").
    private String modalidade; // Modalidade (ex: "Presencial", "Online", "Hibrido").
    private String mes; // Mês do evento (ex: "julho").
    private String linkCandidatura; // URL para o formulário de candidatura/inscrição.
    // Você pode adicionar mais campos conforme a necessidade (ex: requisitos, contato, etc.)

    public Processo() {
    }

    public Processo(String id, String titulo, String inscricoesAte, String etapas, String area, String modalidade, String mes, String linkCandidatura) {
        this.id = id;
        this.titulo = titulo;
        this.inscricoesAte = inscricoesAte;
        this.etapas = etapas;
        this.area = area;
        this.modalidade = modalidade;
        this.mes = mes;
        this.linkCandidatura = linkCandidatura;
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

    public String getInscricoesAte() {
        return inscricoesAte;
    }

    public void setInscricoesAte(String inscricoesAte) {
        this.inscricoesAte = inscricoesAte;
    }

    public String getEtapas() {
        return etapas;
    }

    public void setEtapas(String etapas) {
        this.etapas = etapas;
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getLinkCandidatura() {
        return linkCandidatura;
    }

    public void setLinkCandidatura(String linkCandidatura) {
        this.linkCandidatura = linkCandidatura;
    }
}