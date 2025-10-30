package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Inscricao; // Importa o modelo Inscricao
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime; // Para data e hora da inscrição
import java.time.format.DateTimeFormatter; // Para formatar data e hora
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // Para gerar IDs únicos
import java.util.stream.Collectors;

/**
 * Serviço responsável por carregar, adicionar e remover dados de Inscrições
 * a partir de um arquivo JSON externo.
 */
@Service
public class InscricaoService {

    @Value("${app.json.inscricoes-path}") // Caminho do arquivo JSON, a ser definido no application.properties
    private String JSON_FILE_PATH;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Gson com formatação bonita

    /**
     * Carrega a lista de inscrições a partir do arquivo JSON configurado.
     * @return Uma lista de objetos Inscricao, ou uma lista vazia em caso de erro ou arquivo não encontrado.
     */
    private List<Inscricao> carregarInscricoesDoJson() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de inscrições não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>(); // Retorna uma lista vazia se o arquivo não existir
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Inscricao>>(){}.getType();
            List<Inscricao> inscricoes = gson.fromJson(reader, listType);
            reader.close();
            return inscricoes != null ? inscricoes : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar inscrições do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Salva a lista atual de inscrições no arquivo JSON.
     * Este método é privado e auxiliar, chamado após uma alteração na lista de inscrições.
     * @param inscricoes A lista de objetos Inscricao a ser salva no arquivo JSON.
     */
    private void salvarInscricoesNoJson(List<Inscricao> inscricoes) {
        try {
            File file = new File(JSON_FILE_PATH);
            FileWriter writer = new FileWriter(file);
            gson.toJson(inscricoes, writer);
            writer.close();
            System.out.println("Inscrições salvas com sucesso em: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar inscrições no JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna todas as inscrições disponíveis.
     * @return Uma lista com todas as inscrições.
     */
    public List<Inscricao> buscarTodasInscricoes() {
        return carregarInscricoesDoJson();
    }

    /**
     * Adiciona uma nova inscrição à lista e salva no JSON.
     * Gera um ID único e a data/hora da inscrição.
     * @param novaInscricao O objeto Inscricao com os dados preenchidos (sem ID/data).
     * @return A inscrição adicionada com ID e data/hora.
     */
    public Inscricao adicionarInscricao(Inscricao novaInscricao) {
        List<Inscricao> inscricoes = carregarInscricoesDoJson();
        novaInscricao.setId(UUID.randomUUID().toString()); // Gera um ID único
        novaInscricao.setDataInscricao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))); // Adiciona data/hora
        inscricoes.add(novaInscricao);
        salvarInscricoesNoJson(inscricoes);
        return novaInscricao;
    }

    /**
     * Remove uma inscrição da lista pelo seu ID.
     * @param id O ID da inscrição a ser removida.
     * @return true se a inscrição foi removida, false caso contrário.
     */
    public boolean removerInscricao(String id) {
        List<Inscricao> inscricoes = carregarInscricoesDoJson();
        boolean removido = inscricoes.removeIf(inscricao -> inscricao.getId().equals(id));
        if (removido) {
            salvarInscricoesNoJson(inscricoes);
        }
        return removido;
    }

    /**
     * Busca inscrições com base em filtros opcionais (título, nome).
     * @param titulo Título da oportunidade (opcional).
     * @param nome Nome do inscrito (opcional).
     * @return Uma lista de inscrições filtradas.
     */
    public List<Inscricao> buscarInscricoesFiltradas(String titulo, String nome) {
        List<Inscricao> todasInscricoes = carregarInscricoesDoJson();

        return todasInscricoes.stream()
                .filter(i -> titulo == null || titulo.isEmpty() || (i.getTitulo() != null && i.getTitulo().toLowerCase().contains(titulo.toLowerCase())))
                .filter(i -> nome == null || nome.isEmpty() || (i.getNome() != null && i.getNome().toLowerCase().contains(nome.toLowerCase())))
                .collect(Collectors.toList());
    }
}