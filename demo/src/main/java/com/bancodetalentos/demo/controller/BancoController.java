// Define o pacote onde este arquivo está localizado.
package com.bancodetalentos.demo.controller;

// Importa os serviços que contêm a lógica de negócio principal da aplicação.
// O Controller delega tarefas complexas a esses serviços.
import com.bancodetalentos.demo.Service.ContasService;
import com.bancodetalentos.demo.Service.PerfilService;
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
import org.springframework.security.core.Authentication;

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

    /**
     * Mapeia requisições GET para "/principal".
     * Exibe a página principal da aplicação.
     * Ela agora também é responsável por popular (preencher) a HttpSession
     * na primeira vez que o usuário acessa a página após o login.
     * * @param session Objeto HttpSession para guardar dados do usuário.
     * 
     * @param model          Objeto Model para passar dados para a view.
     * @param authentication Objeto injetado pelo Spring Security que contém os
     *                       dados do usuário logado.
     * @return O nome do template HTML.
     */
    @GetMapping("/principal")
    public String mostrarPaginaPrincipal(HttpSession session, Model model, Authentication authentication) {

        // 1. Tenta pegar o CPF da sessão. Se ele existir, a sessão já foi populada.
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        // 2. Se o CPF na sessão for NULO, mas o usuário ESTIVER autenticado
        // (significa que ele acabou de fazer o login)
        if (usuarioCpf == null && authentication != null && authentication.isAuthenticated()) {

            System.out.println("--- BancoController: Sessão vazia, populando dados do usuário.");

            // 3. Pega o "username" que o Spring Security guardou.
            // (No nosso CustomUserDetailsService, definimos que o "username" é o E-MAIL)
            String email = authentication.getName();

            // 4. Busca os dados completos da conta e do perfil no banco
            Optional<Contas> contaOpt = contasService.findByEmail(email); // Usando o método do Passo 3

            if (contaOpt.isPresent()) {
                Contas usuarioLogado = contaOpt.get();
                Optional<Perfis> perfilOpt = perfilService.buscarPerfilPorCpf(usuarioLogado.getCpf());

                // 5. AGORA SIM, POPULAMOS (PREENCHEMOS) A SESSÃO
                session.setAttribute("usuarioLogadoCpf", usuarioLogado.getCpf());
                session.setAttribute("usuarioLogadoEmail", usuarioLogado.getEmail());
                session.setAttribute("status", usuarioLogado.getStatus());

                // Pega o nome do perfil, ou usa "Usuário" como padrão
                String nome = perfilOpt.map(Perfis::getNome).orElse("Usuário");
                session.setAttribute("usuarioNome", nome);

                System.out.println("--- BancoController: Sessão populada para: " + nome);

            } else {
                // Isso é muito improvável de acontecer se o login funcionou,
                // mas é uma verificação de segurança.
                System.out.println(
                        "--- BancoController: ERRO! Autenticado, mas não encontrou conta por e-mail: " + email);
                return "redirect:/login?error=true";
            }
        }
        // Se a sessão está vazia E o usuário não está autenticado, manda para o login
        else if (usuarioCpf == null) {
            return "redirect:/login";
        }

        // 6. Neste ponto, a sessão está preenchida (seja de agora ou de antes).
        // Lê os dados da sessão e os envia para a página HTML.
        model.addAttribute("status", session.getAttribute("status"));
        model.addAttribute("usuarioCpf", session.getAttribute("usuarioLogadoCpf"));
        model.addAttribute("usuarioNome", session.getAttribute("usuarioNome"));
        model.addAttribute("usuarioMatricula", session.getAttribute("usuarioMatricula")); // (Mantendo este, caso use)

        return "principal"; // Retorna o nome da view "principal" (principal.html).
    }

    /**
     * Mapeia requisições GET para "/fale-conosco".
     * Exibe a página Fale Conosco.
     */
    @GetMapping("/faleconosco")
    public String mostrarFaleConosco(HttpSession session, Model model) {
        return "faleconosco"; // Retorna o nome da view (Fale-Conosco.html).
    }

    /**
     * Mapeia requisições GET para "/recuperarsenha".
     * Exibe a página Recuperar Senha
     */
    @GetMapping("/recuperarsenha")
    public String mostrarRecuperarSenha() {
        return "recuperarsenha"; // Retorna o nome da view (recuperarsenha.html).
    }

    /**
     * Mapeia requisições GET para "/cadastro".
     * Exibe a página de cadastro de novo usuário.
     * 
     * @return O nome do template HTML a ser renderizado (cadastros.html).
     */
    @GetMapping("/cadastro")
    public String mostrarCadastro() {
        return "cadastro"; // Retorna o nome da view (cadastro.html).
    }

    /**
     * Lida com a submissão do formulário de cadastro (POST request).
     */
    @PostMapping("/cadastro")
    public String processarCadastro(
            // 1. Pega todos os dados do formulário
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("telefone") String telefone,
            @RequestParam("cpf") String cpf,
            @RequestParam("instituicao") String instituicao,
            @RequestParam("cargo") String cargo,
            @RequestParam("sobre") String sobre,
            @RequestParam("senha") String senha,
            @RequestParam("confSenha") String confSenha,
            @RequestParam("tipoConta") String tipoConta,
            // 2. Objeto para enviar mensagens de volta (ex: "Sucesso!")
            RedirectAttributes redirectAttributes) {

        // 3. Validação básica no servidor
        if (!senha.equals(confSenha)) {
            redirectAttributes.addFlashAttribute("error", "As senhas não coincidem.");
            return "redirect:/cadastro"; // Retorna para o formulário
        }
        if (senha.length() < 8) {
            redirectAttributes.addFlashAttribute("error", "A senha deve ter no mínimo 8 caracteres.");
            return "redirect:/cadastro"; // Retorna para o formulário
        }

        // 4. Tenta chamar o serviço para registrar a conta
        try {
            contasService.registrarNovaConta(nome, email, telefone, cpf,
                    instituicao, cargo, sobre,
                    senha, tipoConta);

        } catch (Exception e) {
            // 5. Se o serviço der um erro (ex: "CPF já existe")
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cadastro"; // Retorna para o formulário
        }

        // 6. Se tudo der certo!
        redirectAttributes.addFlashAttribute("success", "Conta criada com sucesso! Por favor, faça o login.");
        return "redirect:/login"; // Envia o usuário para a página de login
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