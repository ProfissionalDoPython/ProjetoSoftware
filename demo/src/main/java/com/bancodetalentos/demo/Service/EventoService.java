package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Evento; // Importa o modelo de Evento
import com.google.gson.Gson; // Para manipulação de JSON
import com.google.gson.reflect.TypeToken; // Para desserializar listas genéricas

import org.springframework.beans.factory.annotation.Value; // Para injetar o caminho do arquivo do application.properties
import org.springframework.stereotype.Service; // Marca como um serviço Spring

import java.io.File; // Para manipular o arquivo
import java.io.FileReader; // Para ler o conteúdo do arquivo
import java.io.IOException; // Para tratamento de erros de I/O
import java.lang.reflect.Type; // Para usar TypeToken
import java.util.ArrayList; // Para a lista de eventos
import java.util.List; // Para a interface List
import java.util.Optional; // Para retornar um evento que pode ou não existir

/**
 * Serviço responsável por carregar e gerenciar os dados de eventos
 * a partir de um arquivo JSON.
 */
@Service
public class EventoService {

    // O caminho para o arquivo JSON de eventos, injetado de application.properties
    @Value("${app.json.eventos-path}")
    private String JSON_FILE_PATH;

    private final Gson gson = new Gson(); // Instância do Gson para (de)serialização

    /**
     * Carrega a lista de eventos a partir do arquivo JSON configurado.
     * @return Uma lista de objetos Evento, ou uma lista vazia em caso de erro ou arquivo não encontrado.
     */
    private List<Evento> carregarEventosDoJson() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de eventos não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>(); // Retorna uma lista vazia se o arquivo não existir
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Evento>>(){}.getType();
            List<Evento> eventos = gson.fromJson(reader, listType);
            reader.close();
            return eventos != null ? eventos : new ArrayList<>();
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
    public List<Evento> buscarTodosEventos() {
        return carregarEventosDoJson();
    }

    /**
     * Busca um evento específico pelo seu ID.
     * @param id O ID do evento a ser buscado.
     * @return Um Optional contendo o Evento se encontrado, ou Optional.empty() caso contrário.
     */
    public Optional<Evento> buscarEventoPorId(String id) {
        List<Evento> eventos = carregarEventosDoJson();
        return eventos.stream()
                      .filter(evento -> evento.getId().equals(id))
                      .findFirst();
    }

    // Futuramente, você pode adicionar métodos para:
    // - Inscrever usuário em evento (se for salvar inscrições no JSON ou DB)
    // - Adicionar novo evento (para um painel administrativo, salvando no JSON)
    // - Atualizar evento
    // - Deletar evento
}