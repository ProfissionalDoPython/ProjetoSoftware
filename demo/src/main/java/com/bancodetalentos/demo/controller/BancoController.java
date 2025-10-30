// Define o pacote onde este arquivo está localizado.
package com.bancodetalentos.demo.controller;

// Importa os serviços que contêm a lógica de negócio principal da aplicação.
// O Controller delega tarefas complexas a esses serviços.
import com.bancodetalentos.demo.Service.BolsaService;
import com.bancodetalentos.demo.Service.ContasService;
import com.bancodetalentos.demo.Service.PerfilService;
import com.bancodetalentos.demo.Service.EstagioService;
import com.bancodetalentos.demo.Service.EventsService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Importa os modelos de dados que representam a estrutura das informações.
import com.bancodetalentos.demo.model.Contas; // Representa as informações de login do usuário.
import com.bancodetalentos.demo.model.Perfis; // Representa os detalhes do perfil do usuário.

// Importa classes do Jakarta Servlet API para lidar com sessões HTTP.
import jakarta.servlet.http.HttpSession;

// Importa anotações e classes do Spring Framework para construir a aplicação web.
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência.
import org.springframework.http.HttpStatus; // Para definir códigos de status HTTP (ex: 200 OK, 401 Unauthorized).
import org.springframework.http.ResponseEntity; // Para construir respostas HTTP personalizadas.
import org.springframework.stereotype.Controller; // Marca esta classe como um Controller Spring MVC.
import org.springframework.ui.Model; // Usado para passar dados do Controller para as views (templates HTML).
import org.springframework.web.bind.annotation.*; // Importa todas as anotações para mapeamento de requisições web.

// Importa classes utilitárias do Java.
import java.util.HashMap; // Para criar mapas (estruturas chave-valor).
import java.util.Map; // Interface para mapas.
import java.util.Optional; // Para lidar com a possibilidade de um valor ser nulo.

/**
 * Controller principal da aplicação "Banco de Talentos".
 * Esta classe é responsável por receber as requisições HTTP dos usuários,
 * processá-las e retornar as respostas adequadas (páginas HTML ou dados JSON).
 * Ele delega a lógica de negócio mais complexa para as classes de serviço.
 */

// Anotação que indica que esta classe é um componente de controle no Spring
// MVC

@Controller
public class BancoController {

    // Declaração das instâncias dos serviços que este Controller irá utilizar.
    // 'final' garante que essas referências não sejam alteradas após a
    // inicialização.
    private final ContasService contasService;
    private final PerfilService perfilService;

    /**
     * Construtor do BancoController.
     * O Spring, através da anotação @Autowired, injetará automaticamente as
     * instâncias
     * dos serviços necessários quando criar um objeto BancoController.
     * Isso é conhecido como Injeção de Dependência via Construtor, uma boa prática
     * no Spring.
     */
    @Autowired
    public BancoController(ContasService contasService, PerfilService perfilService) {
        this.contasService = contasService;
        this.perfilService = perfilService;
    }

    /**
     * Handles requests to the root URL ("/").
     * Redirects to the main page if the user is logged in,
     * otherwise, redirects to the login page.
     */
    @GetMapping("/")
    public String handleRoot(HttpSession session) {
        // Check if there is a user logged in the session.
        if (session.getAttribute("usuarioLogadoCpf") != null) {
            // If yes, redirect to the main page.
            return "redirect:/principal";
        } else {
            // If no, redirect to the login page.
            return "redirect:/login";
        }
    }

    /**
     * Mapeia requisições GET para "/login".
     * Exibe o formulário de login.
     * 
     * @param model Objeto Model para passar dados para a view.
     * @return O nome do template HTML a ser renderizado (login.html).
     */
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        // Adiciona um objeto 'Contas' vazio ao modelo.
        // Isso é útil para vincular os campos do formulário HTML a este objeto.
        model.addAttribute("conta", new Contas());
        return "login"; // Retorna o nome da view "login" (login.html).
    }

    @PostMapping("/login")
    public String logar(@ModelAttribute Contas conta, HttpSession session, RedirectAttributes redirectAttributes) {

        // 1. Call the simplified service method to validate the user.
        // It now only needs the email and password from the form submission.
        String errorMessage = contasService.validarLogin(conta.getEmail(), conta.getPassword());

        // 2. Check if the login was successful (if errorMessage is null).
        if (errorMessage == null) {
            // SUCCESS: Credentials are correct.

            // Get the full account details to store in the session.
            Optional<Contas> contaValidada = contasService.getValidAccount(conta.getEmail(), conta.getPassword());

            if (contaValidada.isPresent()) {
                Contas usuarioLogado = contaValidada.get();

                // Store user info in the session so they stay logged in.
                session.setAttribute("usuarioLogadoCpf", usuarioLogado.getCpf()); // We can still store CPF for other
                                                                                  // uses
                session.setAttribute("usuarioLogadoEmail", usuarioLogado.getEmail());
                session.setAttribute("status", usuarioLogado.getStatus());

                // Find the user's profile to get their name.
                Optional<Perfis> perfilUsuario = perfilService.buscarPerfilPorCpf(usuarioLogado.getCpf());
                if (perfilUsuario.isPresent()) {
                    session.setAttribute("usuarioNome", perfilUsuario.get().getNome());
                } else {
                    session.setAttribute("usuarioNome", "Usuário"); // Fallback name
                }

                // Redirect the user's browser to the main page.
                return "redirect:/principal";
            }
        }

        // 3. If the login failed, prepare the error message.
        // 'addFlashAttribute' makes the error message available only for the very next
        // request.
        redirectAttributes.addFlashAttribute("error", errorMessage);

        // Redirect the user's browser back to the login page to show the error.
        return "redirect:/login";
    }

    /**
     * Mapeia requisições GET para "/principal".
     * Exibe a página principal da aplicação após o login.
     * 
     * @param session Objeto HttpSession para verificar o status de login.
     * @param model   Objeto Model para passar dados para a view.
     * @return O nome do template HTML a ser renderizado (principal.html) ou um
     *         redirecionamento para o login.
     */
    @GetMapping("/principal")
    public String mostrarPaginaPrincipal(HttpSession session, Model model) {
        // Obtém informações do usuário da sessão.
        Integer status = (Integer) session.getAttribute("status");
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String usuarioNome = (String) session.getAttribute("usuarioNome");
        String usuarioMatricula = (String) session.getAttribute("usuarioMatricula");

        // Verifica se o usuário está logado. Se não, redireciona para a página de
        // login.
        if (status == null || usuarioCpf == null) { // <-- Condição atualizada
        return "redirect:/login";
        }

        // Adiciona os atributos do usuário ao modelo para que possam ser exibidos na
        // página principal.
        model.addAttribute("status", status);
        model.addAttribute("usuarioCpf", usuarioCpf);
        model.addAttribute("usuarioNome", usuarioNome != null ? usuarioNome : "Nome do Usuário");
        model.addAttribute("usuarioMatricula", usuarioMatricula != null ? usuarioMatricula : "Matrícula");
        return "principal"; // Retorna o nome da view "principal" (principal.html).
    }

    /**
     * Mapeia requisições GET para "/logout".
     * Realiza o processo de logout do usuário.
     * 
     * @param session Objeto HttpSession a ser invalidado.
     * @return Um redirecionamento para a página de login.
     */
    @GetMapping("/logout")
    public String fazerLogout(HttpSession session) {
        session.invalidate(); // Invalida (encerra) a sessão HTTP, removendo todos os atributos do usuário.
        return "redirect:/login"; // Redireciona o usuário para a página de login.
    }
}