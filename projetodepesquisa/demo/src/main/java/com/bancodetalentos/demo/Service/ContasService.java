package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Contas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// componente do spring, basico do basico
@Service 
public class ContasService {

    private final String JSON_FILE_PATH = "classpath:jsonpasta/usuario.json"; // caminho do arquivo JSON
    private final Gson gson = new Gson(); // essa e a instancia do Gson para serialização/desserialização

    /**
     * esse codigo ta carregando a lista de contas do arquivo JSON.
     * e da um @return de uma lista de objetos Contas, ou uma lista vazia em caso de erro
     */
    private List<Contas> carregarContasDoJson() {
        try {
            // pega o arquivo a partir do caminho do classpath
            File file = ResourceUtils.getFile(JSON_FILE_PATH);
            // cria um FileReader para ler o arquivo
            FileReader reader = new FileReader(file);
            // defini o tipo para a desserialização, como lista de contas
            Type listType = new TypeToken<ArrayList<Contas>>(){}.getType();
            // isso desserializa o JSON para uma lista de objetos Contas
            List<Contas> contas = gson.fromJson(reader, listType);
            reader.close(); 
            // se der ruim, retorna a lista ou uma lista vazia se for nulo
            return contas != null ? contas : new ArrayList<>(); 
        } catch (IOException e) {
            // essa parte aqui e para ajudar em identificar os erros quando for passar para JSON
            System.err.println("Erro ao carregar contas do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); 
        }
    }

    public Optional<Contas> validarLogin(String cpf, String email, String senha) {
        // to carregando todas as contas do JSON
        List<Contas> contas = carregarContasDoJson();
        // faz a iteração sobre a lista de contas para encontrar uma correspondência
        for (Contas conta : contas) {
            // parte da verificação (alias, to ignorando letras maiúsculas/minúsculas no email)
            if (conta.getCpf().equals(cpf) &&
                conta.getEmail().equalsIgnoreCase(email) &&
                conta.verificarSenha(senha)) { // 
                return Optional.of(conta); // retorna a conta encontrada
            }
        }
        return Optional.empty(); // qualquer coisa, retorna vazio se nenhuma conta corresponder
    }
}