package com.bancodetalentos.demo.model;

/**
 * Classe de modelo que representa um Evento Profissional ou Acadêmico.
 * Usada para mapear os dados do arquivo JSON e para a comunicação entre o back-end e o front-end.
 */
public class Events { // Changed from Evento to Events
    private String id;
    private String titulo;
    private String data;
    private String local;
    private String descricao;
    private String linkInscricao;
    private boolean destaque;
    private String categoria;
    private String area;
    private String modalidade;
    private String mes;


    public Events() { // Changed constructor name
    }

    public Events(String id, String titulo, String data, String local, String descricao, String linkInscricao, boolean destaque, String categoria, String area, String modalidade, String mes) { // Changed constructor name
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.local = local;
        this.descricao = descricao;
        this.linkInscricao = linkInscricao;
        this.destaque = destaque;
        this.categoria = categoria;
        this.area = area;
        this.modalidade = modalidade;
        this.mes = mes;
    }

    // Getters e Setters (Generated for all fields)

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLinkInscricao() {
        return linkInscricao;
    }

    public void setLinkInscricao(String linkInscricao) {
        this.linkInscricao = linkInscricao;
    }

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
}