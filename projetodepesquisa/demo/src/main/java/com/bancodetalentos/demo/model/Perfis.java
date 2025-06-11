package com.bancodetalentos.demo.model;

import java.util.List;
import java.util.Map;

public class Perfis {
    private String cpf; 
    private String nome;
    private String profissao;
    private List<String> formacaoAcademica; // lista de strings para formações
    private List<String> experienciaProfissional; // lista de strings para experiências
    private Map<String, Integer> habilidades; // mapa para habilidades 
    private List<String> areasInteresse;
    private String objetivosProfissionais;
    private String experienciaAcademica;
    private List<String> idiomas; // lista de idiomas falados
    private List<String> participacoes; // lista de participações em eventos, projetos, etc...
    private List<String> projetosDestacados; // lista de projetos destacados dos meus perfis
    private String linkedin;
    private String github;
    private String portfolio;

    // construtor padrão vazio, essencial para o Gson
    public Perfis() {
    }

    public Perfis(String cpf, String nome, String profissao, List<String> formacaoAcademica,
                  List<String> experienciaProfissional, Map<String, Integer> habilidades,
                  List<String> areasInteresse, String objetivosProfissionais,
                  String experienciaAcademica, List<String> idiomas,
                  List<String> participacoes, List<String> projetosDestacados,
                  String linkedin, String github, String portfolio) {
        this.cpf = cpf;
        this.nome = nome;
        this.profissao = profissao;
        this.formacaoAcademica = formacaoAcademica;
        this.experienciaProfissional = experienciaProfissional;
        this.habilidades = habilidades;
        this.areasInteresse = areasInteresse;
        this.objetivosProfissionais = objetivosProfissionais;
        this.experienciaAcademica = experienciaAcademica;
        this.idiomas = idiomas;
        this.participacoes = participacoes;
        this.projetosDestacados = projetosDestacados;
        this.linkedin = linkedin;
        this.github = github;
        this.portfolio = portfolio;
    }

    // getters e setters
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

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public List<String> getFormacaoAcademica() {
        return formacaoAcademica;
    }

    public void setFormacaoAcademica(List<String> formacaoAcademica) {
        this.formacaoAcademica = formacaoAcademica;
    }

    public List<String> getExperienciaProfissional() {
        return experienciaProfissional;
    }

    public void setExperienciaProfissional(List<String> experienciaProfissional) {
        this.experienciaProfissional = experienciaProfissional;
    }

    public Map<String, Integer> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(Map<String, Integer> habilidades) {
        this.habilidades = habilidades;
    }

    public List<String> getAreasInteresse() {
        return areasInteresse;
    }

    public void setAreasInteresse(List<String> areasInteresse) {
        this.areasInteresse = areasInteresse;
    }

    public String getObjetivosProfissionais() {
        return objetivosProfissionais;
    }

    public void setObjetivosProfissionais(String objetivosProfissionais) {
        this.objetivosProfissionais = objetivosProfissionais;
    }

    public String getExperienciaAcademica() {
        return experienciaAcademica;
    }

    public void setExperienciaAcademica(String experienciaAcademica) {
        this.experienciaAcademica = experienciaAcademica;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<String> getParticipacoes() {
        return participacoes;
    }

    public void setParticipacoes(List<String> participacoes) {
        this.participacoes = participacoes;
    }

    public List<String> getProjetosDestacados() {
        return projetosDestacados;
    }

    public void setProjetosDestacados(List<String> projetosDestacados) {
        this.projetosDestacados = projetosDestacados;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }
}