package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.PerfilService; // Importa o serviço que lida com a lógica de perfis de talento.
import com.bancodetalentos.demo.model.Perfis; // Importa a classe de modelo Perfis (representa o perfil de um talento).
import org.springframework.ui.Model; // Importa Model para passar dados do controlador para o template HTML.
import jakarta.servlet.http.HttpSession; // Importa HttpSession para gerenciar sessões de usuário.
import org.springframework.beans.factory.annotation.Autowired; // Importa Autowired para injeção de dependência.
import org.springframework.http.HttpStatus; // Importa HttpStatus para definir códigos de status HTTP.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para construir respostas HTTP personalizadas.
import org.springframework.stereotype.Controller; // Anotação do Spring que marca esta classe como um controlador.
import org.springframework.web.bind.annotation.*; // Importa todas as anotações do Spring Web (GetMapping, PostMapping, RequestBody, etc.).
import java.util.HashMap; // Para usar HashMaps (estrutura de dados chave-valor).
import java.util.Map; // Para usar Maps (interface para HashMaps).
import java.util.Optional; // Para lidar com valores que podem estar ausentes (boa prática para buscas).

/**
 * Este controlador lida com as requisições HTTP relacionadas à visualização
 * e edição do perfil de um talento.
 */
@Controller // Indica ao Spring que esta classe é um controlador que lida com requisições HTTP.
@RequestMapping("/perfil") // Define um prefixo de URL para todos os endpoints neste controlador.
                           // Todas as URLs dentro desta classe começarão com "/perfil".
public class PerfilController {

    // Declara uma instância do PerfilService. Esta dependência será injetada pelo Spring.
    private final PerfilService perfilService;

    /**
     * Construtor da classe PerfilController.
     * O Spring usa a anotação @Autowired para injetar automaticamente uma instância de PerfilService
     * quando cria um objeto PerfilController. Isso é um exemplo de Injeção de Dependência.
     *
     * @param perfilService O serviço de perfil a ser injetado.
     */
    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    /**
     * Mapeia requisições HTTP GET para a URL base "/perfil" (devido ao @RequestMapping no nível da classe).
     * Este endpoint é responsável por exibir a página HTML do perfil para o usuário.
     *
     * @param session A sessão HTTP atual, usada para verificar se o usuário está logado.
     * @param model O objeto Model do Spring, usado para passar atributos para o template Thymeleaf.
     * @return O nome do template Thymeleaf (perfil.html) a ser renderizado.
     * Se o usuário não estiver logado, redireciona para a página de login.
     */
    @GetMapping // Mapeia para GET /perfil.
    public String mostrarPaginaPerfil(HttpSession session, Model model) {
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf"); // Tenta obter o CPF do usuário da sessão.
        if (usuarioCpf == null) { // Verifica se o CPF está na sessão (indicando que o usuário está logado).
            return "redirect:/login"; // Se não estiver logado, redireciona para a página de login.
        }
        model.addAttribute("usuarioLogadoCpf", usuarioCpf); // Adiciona o CPF ao modelo, pode ser útil no Thymeleaf.
        // Comentários: Você poderia passar o objeto Perfil completo para o modelo aqui,
        // mas como o JavaScript faz uma requisição AJAX separada para '/perfil/dados',
        // não é estritamente necessário renderizar o perfil inicial via Thymeleaf.
        return "perfil"; // Retorna o nome do template HTML (perfil.html).
    }

    /**
     * Mapeia requisições HTTP GET para a URL "/perfil/dados".
     * Este endpoint é usado para carregar e retornar os dados completos do perfil do usuário logado
     * em formato JSON para o front-end (usado pelo JavaScript via `fetch`).
     *
     * @param session A sessão HTTP atual, usada para obter o CPF e o tipo de perfil do usuário logado.
     * @return Um {@link ResponseEntity} contendo um mapa de String-Object, que será
     * serializado para JSON como resposta HTTP. Inclui o status HTTP.
     */
    @GetMapping("/dados") // Mapeia para GET /perfil/dados.
    @ResponseBody // Indica que o retorno do método deve ser diretamente vinculado ao corpo da resposta HTTP (JSON).
    public ResponseEntity<Map<String, Object>> getDadosPerfil(HttpSession session) {
        // Obtém o CPF e o tipo de perfil do usuário da sessão.
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");

        // Cria um mapa para armazenar a resposta que será enviada de volta ao cliente.
        Map<String, Object> response = new HashMap<>();

        // Verifica se o usuário está logado.
        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado."); // Adiciona mensagem de erro.
            // Retorna um status HTTP 401 Unauthorized (Não Autorizado) se o usuário não estiver logado.
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Busca o perfil do usuário logado usando o CPF através do PerfilService.
        Optional<Perfis> perfil = perfilService.buscarPerfilPorCpf(usuarioCpf);

        // Verifica se o perfil foi encontrado.
        if (perfil.isPresent()) {
            response.put("sucesso", true); // Indica sucesso na operação.
            response.put("perfil", perfil.get()); // Adiciona o objeto Perfil completo à resposta.
            response.put("tipoPerfil", tipoPerfil); // Retorna também o tipo de perfil para o JavaScript do front-end.
            // Retorna uma resposta HTTP 200 OK com os dados do perfil.
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Este bloco é executado se um perfil para o CPF logado NÃO for encontrado no JSON.
            response.put("erro", "Perfil não encontrado para o CPF: " + usuarioCpf); // Mensagem de erro específica.
            // Nota: Em cenários reais, um 404 pode ser mais apropriado se o perfil realmente não existir.
            // No entanto, para fins de criação de um novo perfil pelo front-end, um 200 OK com um perfil vazio (ou mensagem)
            // também pode ser usado, como foi discutido em iterações anteriores, para que o JS possa preencher.
            // Atualmente, ele retorna HttpStatus.NOT_FOUND se o perfil não for encontrado.
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Mapeia requisições HTTP POST para a URL "/perfil/salvar".
     * Este endpoint é usado para salvar (ou atualizar) os dados do perfil de um talento.
     * Os dados do perfil são recebidos no corpo da requisição em formato JSON.
     *
     * @param perfilAtualizado O objeto {@link Perfis} contendo os dados do perfil a serem salvos/atualizados,
     * recebido do corpo da requisição JSON. @RequestBody faz o bind automático.
     * @param session A sessão HTTP atual, usada para obter o CPF do usuário logado e garantir a segurança.
     * @return Um {@link ResponseEntity} contendo um mapa de String-Object, que será
     * serializado para JSON como resposta HTTP. Inclui o status HTTP.
     */
    @PostMapping("/salvar") // Mapeia para POST /perfil/salvar.
    @ResponseBody // Indica que o retorno do método deve ser diretamente vinculado ao corpo da resposta HTTP (JSON).
    public ResponseEntity<Map<String, Object>> salvarPerfil(@RequestBody Perfis perfilAtualizado, HttpSession session) {
        // Obtém o CPF do usuário logado da sessão.
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        // Cria um mapa para armazenar a resposta.
        Map<String, Object> response = new HashMap<>();

        // Verifica se o usuário está logado.
        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado."); // Mensagem de erro.
            // Retorna 401 Unauthorized se o usuário não estiver logado.
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Garante que o CPF do perfil que está sendo salvo/atualizado corresponde ao CPF do usuário logado.
        // Isso é uma medida de segurança importante para evitar que um usuário mal-intencionado
        // tente salvar o perfil de outro usuário, modificando o CPF na requisição do front-end.
        if (!usuarioCpf.equals(perfilAtualizado.getCpf())) {
            response.put("erro", "Tentativa de salvar perfil de outro usuário."); // Mensagem de erro de segurança.
            // Retorna 403 Forbidden (Proibido) se houver uma tentativa de manipular um perfil diferente.
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        try {
            // Chama o serviço para atualizar ou adicionar o perfil no armazenamento (arquivo JSON).
            Perfis perfilSalvo = perfilService.atualizarPerfil(perfilAtualizado);
            response.put("sucesso", true); // Indica que a operação foi bem-sucedida.
            response.put("mensagem", "Perfil atualizado com sucesso!"); // Mensagem de sucesso para o usuário.
            response.put("perfil", perfilSalvo); // Retorna o objeto perfil salvo/atualizado na resposta JSON.
            // Retorna um status HTTP 200 OK, indicando que a requisição foi processada com sucesso.
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Captura qualquer exceção genérica que possa ocorrer durante o processo de salvamento.
            response.put("sucesso", false); // Indica que a operação falhou.
            response.put("mensagem", "Erro ao salvar perfil: " + e.getMessage()); // Mensagem de erro detalhada.
            e.printStackTrace(); // Imprime o stack trace completo da exceção para depuração no console do servidor.
            // Retorna um status HTTP 500 Internal Server Error (Erro Interno do Servidor),
            // indicando que algo inesperado aconteceu no servidor.
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}