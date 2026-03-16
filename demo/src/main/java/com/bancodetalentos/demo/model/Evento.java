package com.bancodetalentos.demo.model;

/**
 * Classe de modelo que representa um Evento Profissional ou Acadêmico.
 * Usada para mapear os dados do arquivo JSON e para a comunicação entre o back-end e o front-end.
 */
public class Evento {
    private String id; // Um identificador único para o evento.
    private String titulo; // Título do evento (ex: "Workshop de Desenvolvimento Web").
    private String data; // Data do evento (ex: "15 de Julho de 2025").
    private String local; // Local do evento (ex: "Auditório Central, Campus Principal").
    private String descricao; // Descrição detalhada do evento.
    private String linkInscricao; // URL para inscrição no evento.
    private boolean destaque; // Indica se o evento é um destaque na página.
    private String categoria; // Categoria do evento (ex: "Workshop", "Feira", "Palestra").
    // Adicione mais campos conforme a necessidade (ex: palestrantes, pré-requisitos, etc.)

    /**
     * Construtor padrão vazio. Essencial para que o Gson (e Spring) possa criar instâncias desta classe.
     */
    public Evento() {
    }

    /**
     * Construtor completo para inicializar todos os campos.
     */
    public Evento(String id, String titulo, String data, String local, String descricao, String linkInscricao, boolean destaque, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.local = local;
        this.descricao = descricao;
        this.linkInscricao = linkInscricao;
        this.destaque = destaque;
        this.categoria = categoria;
    }

    // Getters e Setters para todos os atributos

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
}