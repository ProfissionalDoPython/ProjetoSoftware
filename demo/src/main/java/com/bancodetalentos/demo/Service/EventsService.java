package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Events; // Changed from Evento to Events
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
import java.util.stream.Collectors;

/**
 * Serviço responsável por carregar e gerenciar os dados de Eventos
 * a partir de um arquivo JSON externo.
 */
@Service // CRUCIAL: Must be present
public class EventsService { // Changed class name

    @Value("${app.json.events-path}") // Caminho do arquivo JSON, a ser definido no application.properties
    private String JSON_FILE_PATH;

    private final Gson gson = new Gson();

    /**
     * Carrega a lista de eventos a partir do arquivo JSON configurado.
     * @return Uma lista de objetos Events, ou uma lista vazia em caso de erro ou arquivo não encontrado.
     */
    private List<Events> carregarEventosDoJson() { // Changed method name slightly for clarity
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de eventos não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Events>>(){}.getType(); // Changed type
            List<Events> events = gson.fromJson(reader, listType); // Changed variable name
            reader.close();
            return events != null ? events : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar eventos do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retorna todos os eventos disponíveis.
     * @return Uma lista com todos os eventos.
     */
    public List<Events> buscarTodosEventos() { // Changed method name
        return carregarEventosDoJson();
    }

    /**
     * Busca um evento específico pelo seu ID.
     * @param id O ID do evento a ser buscado.
     * @return Um Optional contendo o Events se encontrado, ou Optional.empty() caso contrário.
     */
    public Optional<Events> buscarEventoPorId(String id) { // Changed method name
        List<Events> events = carregarEventosDoJson();
        return events.stream()
                      .filter(event -> event.getId().equals(id)) // Changed variable name
                      .findFirst();
    }

    /**
     * Busca eventos com base em filtros opcionais.
     * @param area Área do conhecimento (opcional).
     * @param modalidade Modalidade (opcional).
     * @param mes Mês (opcional).
     * @return Uma lista de eventos filtrados.
     */
    public List<Events> buscarEventosFiltrados(String area, String modalidade, String mes) { // Changed method name
        List<Events> allEvents = carregarEventosDoJson(); // Changed variable name

        return allEvents.stream()
                .filter(e -> area == null || area.isEmpty() || e.getArea().equalsIgnoreCase(area))
                .filter(e -> modalidade == null || modalidade.isEmpty() || e.getModalidade().equalsIgnoreCase(modalidade))
                .filter(e -> mes == null || mes.isEmpty() || e.getMes().equalsIgnoreCase(mes))
                .collect(Collectors.toList());
    }
}