package com.bancodetalentos.demo.model;

import java.util.List; // Importa a interface List para coleções ordenadas de elementos.
import java.util.Map;  // Importa a interface Map para coleções de pares chave-valor.

/**
 * Classe de modelo que representa o perfil de um talento (usuário).
 * Contém informações detalhadas sobre a formação, experiência, habilidades, etc.
 * É usada para mapear os dados do arquivo JSON e para a comunicação entre o front-end e o back-end.
 */
public class Perfis {
    private String cpf; // O CPF do talento, usado como identificador único.
    private String nome; // Nome completo do talento.
    private String profissao; // Profissão ou área de atuação atual do talento.
    private List<String> formacaoAcademica; // Lista de strings para formações acadêmicas (ex: "Graduação em Ciência da Computação").
    private List<String> experienciaProfissional; // Lista de strings para experiências profissionais (ex: "Estágio em TI - Empresa X").
    private Map<String, Integer> habilidades; // Mapa para habilidades (chave: nome da habilidade, valor: nível de proficiência em porcentagem, ex: {"Java": 80}).
    private List<String> areasInteresse; // Lista de áreas de interesse do talento.
    private String objetivosProfissionais; // Descrição dos objetivos de carreira do talento.
    private String experienciaAcademica; // Descrição de experiências acadêmicas relevantes (ex: "Monitoria em cálculo").
    private List<String> idiomas; // Lista de idiomas falados (ex: "Inglês - Fluente").
    private List<String> participacoes; // Lista de participações em eventos, projetos, etc.
    private List<String> projetosDestacados; // Lista de projetos relevantes que o talento deseja destacar.
    private String linkedin; // URL do perfil do LinkedIn.
    private String github; // URL do perfil do GitHub.
    private String portfolio; // URL de um portfólio online.

    /**
     * Construtor padrão vazio.
     * Essencial para que a biblioteca Gson (e frameworks como Spring) consiga criar instâncias
     * desta classe ao desserializar JSON, mesmo que nenhum valor inicial seja fornecido.
     */
    public Perfis() {
    }

    /**
     * Construtor completo para inicializar todos os campos da classe Perfis.
     *
     * @param cpf O CPF do talento.
     * @param nome Nome completo.
     * @param profissao Profissão ou área.
     * @param formacaoAcademica Lista de formações acadêmicas.
     * @param experienciaProfissional Lista de experiências profissionais.
     * @param habilidades Mapa de habilidades e seus níveis.
     * @param areasInteresse Lista de áreas de interesse.
     * @param objetivosProfissionais Objetivos profissionais.
     * @param experienciaAcademica Experiência acadêmica.
     * @param idiomas Lista de idiomas.
     * @param participacoes Lista de participações.
     * @param projetosDestacados Lista de projetos.
     * @param linkedin URL do LinkedIn.
     * @param github URL do GitHub.
     * @param portfolio URL do portfólio.
     */
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

    // A seguir, os métodos Getters e Setters para cada atributo da classe.
    // Getters permitem acessar o valor de um atributo.
    // Setters permitem modificar o valor de um atributo.

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