package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Estagio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File; // Ensure this import is here
import java.io.FileReader; // Ensure this import is here
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstagioService {

    @Value("${app.json.estagios-path}")
    private String JSON_FILE_PATH;

    private final Gson gson = new Gson();

    private List<Estagio> carregarEstagiosDoJson() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de estágios não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Estagio>>(){}.getType();
            List<Estagio> estagios = gson.fromJson(reader, listType);
            reader.close();
            return estagios != null ? estagios : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar estágios do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Estagio> buscarTodosEstagios() {
        return carregarEstagiosDoJson();
    }

    public List<Estagio> buscarEstagiosFiltrados(String area, String modalidade, String mesInicio) {
        List<Estagio> todosEstagios = carregarEstagiosDoJson();

        return todosEstagios.stream()
                .filter(e -> area == null || area.isEmpty() || e.getArea().equalsIgnoreCase(area))
                .filter(e -> modalidade == null || modalidade.isEmpty() || e.getModalidade().equalsIgnoreCase(modalidade))
                .filter(e -> mesInicio == null || mesInicio.isEmpty() || e.getMesInicio().equalsIgnoreCase(mesInicio))
                .collect(Collectors.toList());
    }

    public Optional<Estagio> buscarEstagioPorId(String id) {
        List<Estagio> estagios = carregarEstagiosDoJson();
        return estagios.stream()
                      .filter(estagio -> estagio.getId().equals(id))
                      .findFirst();
    }
}