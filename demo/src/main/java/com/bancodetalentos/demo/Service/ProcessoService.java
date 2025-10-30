package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Processo; // Importa o modelo Processo (Não 'ProcessoSeletivo')
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para usar streams e filtros

/**
 * Serviço responsável por carregar e gerenciar os dados de Processos Seletivos
 * a partir de um arquivo JSON externo.
 */
@Service
public class ProcessoService { // Renomeado para ProcessoService para ser mais genérico

    @Value("${app.json.processos-path}")
    private String JSON_FILE_PATH;

    private final Gson gson = new Gson();

    /**
     * Carrega a lista de processos seletivos a partir do arquivo JSON configurado.
     * @return Uma lista de objetos Processo, ou uma lista vazia em caso de erro ou arquivo não encontrado.
     */
    private List<Processo> carregarProcessosDoJson() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de processos seletivos não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            // Corrigido para usar o tipo Processo, não ProcessoSeletivo
            Type listType = new TypeToken<ArrayList<Processo>>(){}.getType();
            List<Processo> processos = gson.fromJson(reader, listType);
            reader.close();
            return processos != null ? processos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar processos seletivos do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retorna todos os processos seletivos disponíveis.
     * @return Uma lista com todos os processos seletivos.
     */
    public List<Processo> buscarTodosProcessos() {
        return carregarProcessosDoJson();
    }

    /**
     * Busca processos seletivos com base em filtros opcionais.
     * @param area Área do conhecimento (opcional).
     * @param modalidade Modalidade (opcional).
     * @param mes Mês (opcional).
     * @return Uma lista de processos seletivos filtrados.
     */
    public List<Processo> buscarProcessosFiltrados(String area, String modalidade, String mes) {
        List<Processo> todosProcessos = carregarProcessosDoJson();

        return todosProcessos.stream()
                .filter(p -> area == null || area.isEmpty() || p.getArea().equalsIgnoreCase(area))
                .filter(p -> modalidade == null || modalidade.isEmpty() || p.getModalidade().equalsIgnoreCase(modalidade))
                .filter(p -> mes == null || mes.isEmpty() || p.getMes().equalsIgnoreCase(mes))
                .collect(Collectors.toList());
    }

    /**
     * Busca um processo seletivo específico pelo seu ID.
     * @param id O ID do processo seletivo a ser buscado.
     * @return Um Optional contendo o Processo se encontrado, ou Optional.empty() caso contrário.
     */
    public Optional<Processo> buscarProcessoPorId(String id) {
        List<Processo> processos = carregarProcessosDoJson();
        return processos.stream()
                      .filter(processo -> processo.getId().equals(id))
                      .findFirst();
    }

    // Futuramente, pode-se adicionar métodos para gerenciar inscrições ou adicionar/editar processos.
}