package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Perfis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder; 
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// componento do spring
@Service 
public class PerfilService {

    // caminho onde ta o conteudo do JSON
    private final String JSON_FILE_PATH = "classpath:jsonpasta/perfis.json"; 
    // instancia do GSON
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
    

    /**
     * isso daqui carrega a lista de perfis do arquivo JSON.
     * e qualquer coisa da @return de uma lista de objetos Perfis, ou uma lista vazia em caso de erro
     */
    private List<Perfis> carregarPerfisDoJson() {
        try {
            File file = ResourceUtils.getFile(JSON_FILE_PATH);
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<ArrayList<Perfis>>(){}.getType();
            List<Perfis> perfis = gson.fromJson(reader, listType);
            reader.close();
            return perfis != null ? perfis : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar perfis do JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * isso daqui salva a lista de perfis no arquivo JSON
     * vai funcionar toda vez que o perfil for atualizado
     */
    private void salvarPerfisNoJson(List<Perfis> perfis) {
        try {
            // pega o  arquivo a partir do caminho do classpath
            File file = ResourceUtils.getFile(JSON_FILE_PATH);
            FileWriter writer = new FileWriter(file);
            // serializar a lista de objetos Perfis para JSON e escrever no arquivo
            gson.toJson(perfis, writer);
            writer.close(); 
            System.out.println("Perfis salvos com sucesso em: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar perfis no JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * to usando isso para buscar um perfil pelo CPF
     * da retorno Optional contendo o objeto Perfis se encontrado, ou Optional.empty() caso contrário
     */
    public Optional<Perfis> buscarPerfilPorCpf(String cpf) {
        List<Perfis> perfis = carregarPerfisDoJson();
        return perfis.stream()
                     .filter(perfil -> perfil.getCpf().equals(cpf))
                     .findFirst(); // retorna o primeiro perfil que corresponder ao CPF
    }

    /**
     * atualiza um perfil existente ou adiciona um novo se não existir
     * da retorno do objeto Perfis atualizado, ou null se houver um erro
     */
    public Perfis atualizarPerfil(Perfis perfilAtualizado) {
        List<Perfis> perfis = carregarPerfisDoJson();
        boolean encontrado = false;

        for (int i = 0; i < perfis.size(); i++) {
            if (perfis.get(i).getCpf().equals(perfilAtualizado.getCpf())) {
                perfis.set(i, perfilAtualizado); // substitui o perfil existente
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            perfis.add(perfilAtualizado); // adiciona o novo perfil se nao for encontrado
        }

        salvarPerfisNoJson(perfis); // salva a lista atualizada no JSON
        return perfilAtualizado;
    }
}