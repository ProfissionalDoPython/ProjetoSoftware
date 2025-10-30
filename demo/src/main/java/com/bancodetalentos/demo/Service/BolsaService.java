package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Bolsa; // Importa o modelo Bolsa
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
 * Serviço responsável por carregar e gerenciar os dados de Oportunidades de Bolsas Acadêmicas
 * a partir de um arquivo JSON externo.
 */
@Service
public class BolsaService {

    @Value("${app.json.bolsas-path}")
    private String JSON_FILE_PATH;

    private final Gson gson = new Gson();

    /**
     * Carrega a lista de bolsas a partir do arquivo JSON configurado.
     * @return Uma lista de objetos Bolsa, ou uma lista vazia em caso de erro ou arquivo não encontrado.
     */
    private List<Bolsa> carregarBolsasDoJson() {
        try {
            File file = new File(JSON_FILE_PATH);
            if (!file.exists()) {
                System.err.println("Arquivo de bolsas não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Bolsa>>(){}.getType();
            List<Bolsa> bolsas = gson.fromJson(reader, listType);
            reader.close();
            return bolsas != null ? bolsas : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar bolsas do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retorna todas as oportunidades de bolsa disponíveis.
     * @return Uma lista com todas as oportunidades de bolsa.
     */
    public List<Bolsa> buscarTodasBolsas() {
        return carregarBolsasDoJson();
    }

    /**
     * Busca bolsas com base em filtros opcionais.
     * @param area Área do conhecimento (opcional).
     * @param modalidade Modalidade (opcional).
     * @param mesInicio Mês de início (opcional).
     * @return Uma lista de bolsas filtradas.
     */
    public List<Bolsa> buscarBolsasFiltradas(String area, String modalidade, String mesInicio) {
        List<Bolsa> todasBolsas = carregarBolsasDoJson();

        return todasBolsas.stream()
                .filter(b -> area == null || area.isEmpty() || b.getArea().equalsIgnoreCase(area))
                .filter(b -> modalidade == null || modalidade.isEmpty() || b.getModalidade().equalsIgnoreCase(modalidade))
                .filter(b -> mesInicio == null || mesInicio.isEmpty() || b.getMesInicio().equalsIgnoreCase(mesInicio))
                .collect(Collectors.toList());
    }

    /**
     * Busca uma bolsa específica pelo seu ID.
     * @param id O ID da bolsa a ser buscada.
     * @return Um Optional contendo a Bolsa se encontrada, ou Optional.empty() caso contrário.
     */
    public Optional<Bolsa> buscarBolsaPorId(String id) {
        List<Bolsa> bolsas = carregarBolsasDoJson();
        return bolsas.stream()
                      .filter(bolsa -> bolsa.getId().equals(id))
                      .findFirst();
    }
}