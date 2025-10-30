package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Perfis; // Importa a classe de modelo Perfis.
import com.google.gson.Gson; // Importa a biblioteca Gson.
import com.google.gson.GsonBuilder; // Importa GsonBuilder para configurar a instância Gson.
import com.google.gson.reflect.TypeToken; // Importa TypeToken para lidar com tipos genéricos.

import org.springframework.beans.factory.annotation.Value; // Importa @Value para injetar valores de propriedades.
import org.springframework.stereotype.Service; // Anotação do Spring que marca esta classe como um serviço.

import java.io.File; // Para manipulação de arquivos no sistema de arquivos.
import java.io.FileReader; // Para ler o conteúdo de arquivos.
import java.io.FileWriter; // Para escrever conteúdo em arquivos.
import java.io.IOException; // Para tratamento de exceções de I/O.
import java.lang.reflect.Type; // Para obter o tipo genérico necessário para a desserialização do Gson.
import java.util.ArrayList; // Para usar ArrayLists.
import java.util.List; // Para usar Lists.
import java.util.Optional; // Para retornar valores que podem ou não estar presentes.

/**
 * Este serviço lida com a lógica de negócios relacionada aos perfis de talento.
 * Inclui operações de carregamento, salvamento, busca e atualização de perfis
 * a partir de um arquivo JSON.
 */
@Service // Indica ao Spring que esta é uma classe de serviço.
public class PerfilService {

    // Define o caminho para o arquivo JSON onde os perfis de talento estão armazenados.
    // O valor é injetado do `application.properties` (ex: app.json.perfis-path=./jsonpasta/perfis.json).
    @Value("${app.json.perfis-path}")
    private String JSON_FILE_PATH;

    // Instância da biblioteca Gson, configurada para formatar o JSON de saída de forma "bonita".
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Carrega a lista de perfis de talento a partir do arquivo JSON especificado.
     * Este método é privado, sendo um auxiliar interno para as operações do serviço.
     *
     * @return Uma lista de objetos {@link Perfis} carregados do JSON,
     * ou uma lista vazia se o arquivo não for encontrado, estiver vazio ou houver erro na leitura.
     */
    private List<Perfis> carregarPerfisDoJson() {
        try {
            // Cria um objeto File representando o caminho do arquivo JSON.
            File file = new File(JSON_FILE_PATH);
            // Verifica se o arquivo existe.
            if (!file.exists()) {
                System.err.println("Arquivo de perfis não encontrado no caminho: " + JSON_FILE_PATH);
                return new ArrayList<>(); // Retorna lista vazia se não encontrar.
            }
            // Cria um FileReader para ler o conteúdo do arquivo.
            FileReader reader = new FileReader(file);
            // Define o tipo genérico para a desserialização (lista de Perfis).
            Type listType = new TypeToken<ArrayList<Perfis>>(){}.getType();
            // Desserializa o JSON para uma lista de objetos Perfis.
            List<Perfis> perfis = gson.fromJson(reader, listType);
            reader.close(); // Fecha o leitor.
            return perfis != null ? perfis : new ArrayList<>(); // Retorna a lista ou uma nova lista vazia.
        } catch (IOException e) {
            // Captura exceções de I/O.
            System.err.println("Erro ao carregar perfis do JSON: " + e.getMessage());
            e.printStackTrace(); // Imprime stack trace.
            return new ArrayList<>(); // Retorna lista vazia em caso de erro.
        }
    }

    /**
     * Salva a lista atual de perfis de talento no arquivo JSON.
     * Este método é privado e auxiliar, chamado após uma alteração na lista de perfis.
     *
     * @param perfis A lista de objetos {@link Perfis} a ser salva no arquivo JSON.
     */
    private void salvarPerfisNoJson(List<Perfis> perfis) {
        try {
            // Cria um objeto File representando o caminho do arquivo JSON para escrita.
            File file = new File(JSON_FILE_PATH);
            // Cria um FileWriter para escrever no arquivo (sobrescreve se já existir).
            FileWriter writer = new FileWriter(file);
            // Serializa a lista de objetos Perfis para JSON e escreve no arquivo.
            gson.toJson(perfis, writer);
            writer.close(); // Fecha o escritor.
            System.out.println("Perfis salvos com sucesso em: " + file.getAbsolutePath());
        } catch (IOException e) {
            // Captura exceções de I/O durante o processo de salvamento.
            System.err.println("Erro ao salvar perfis no JSON: " + e.getMessage());
            e.printStackTrace(); // Imprime stack trace.
        }
    }

    /**
     * Busca um perfil de talento específico usando o CPF como critério.
     *
     * @param cpf O CPF do perfil a ser buscado.
     * @return Um {@link Optional} contendo o objeto {@link Perfis} se um perfil com o CPF correspondente for encontrado,
     * ou um {@link Optional#empty()} caso contrário.
     */
    public Optional<Perfis> buscarPerfilPorCpf(String cpf) {
        List<Perfis> perfis = carregarPerfisDoJson(); // Carrega todos os perfis.
        return perfis.stream() // Usa Streams para filtrar.
                     .filter(perfil -> perfil.getCpf().equals(cpf)) // Filtra pelo CPF.
                     .findFirst(); // Retorna o primeiro correspondente.
    }

    /**
     * Atualiza um perfil existente na lista ou adiciona um novo perfil se ele não existir.
     * Após a atualização/adição, a lista completa é salva de volta no arquivo JSON.
     *
     * @param perfilAtualizado O objeto {@link Perfis} contendo os dados do perfil a ser atualizado ou adicionado.
     * @return O objeto {@link Perfis} que foi atualizado ou adicionado.
     */
    public Perfis atualizarPerfil(Perfis perfilAtualizado) {
        List<Perfis> perfis = carregarPerfisDoJson(); // Carrega os perfis.
        boolean encontrado = false; // Flag para verificar se o perfil foi encontrado.

        for (int i = 0; i < perfis.size(); i++) { // Itera sobre a lista.
            if (perfis.get(i).getCpf().equals(perfilAtualizado.getCpf())) { // Compara CPFs.
                perfis.set(i, perfilAtualizado); // Atualiza o perfil.
                encontrado = true; // Marca como encontrado.
                break; // Sai do loop.
            }
        }

        if (!encontrado) { // Se não encontrado, adiciona como novo perfil.
            perfis.add(perfilAtualizado);
        }

        salvarPerfisNoJson(perfis); // Salva a lista atualizada no JSON.
        return perfilAtualizado; // Retorna o perfil processado.
    }
}