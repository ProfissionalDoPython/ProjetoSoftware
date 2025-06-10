package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.ContasService;
import com.bancodetalentos.demo.model.Contas;
import jakarta.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// meu compontende de spring
@Controller
public class BancoController {

    private final ContasService contasService;

    // injeção de dependência do ContasService
    @Autowired
    public BancoController(ContasService contasService) {
        this.contasService = contasService;
    }

    // mapeamento para a página de login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("conta", new Contas());
        return "login";
    }

    // mapeamento para o login
    @PostMapping("/login")
    @ResponseBody
    // usado para receber os dados do formulurio de login
    public ResponseEntity<Map<String, Object>> logar(@RequestBody Contas conta, HttpSession session) { 
        System.out.println("Tentativa de login: CPF=" + conta.getCpf() + ", Email=" + conta.getEmail() + ", Senha="
                + conta.getSenha());

        // Valida o login usando o serviço de contas
        Optional<Contas> contaValidada = contasService.validarLogin(conta.getCpf(), conta.getEmail(), conta.getSenha());

        // cria um mapa para armazenar a resposta
        Map<String, Object> response = new HashMap<>();

        if (contaValidada.isPresent()) {
            // se deu bom vai fazer o login
            Contas usuarioLogado = contaValidada.get();

            // armazena informações do usuário na sessão
            session.setAttribute("usuarioLogadoCpf", usuarioLogado.getCpf());
            session.setAttribute("usuarioLogadoEmail", usuarioLogado.getEmail());
            session.setAttribute("tipoPerfil", usuarioLogado.getTipoPerfil()); // Armazena o tipo de perfil na sessão
            response.put("sucesso", true);
            response.put("mensagem", "Login realizado com sucesso!");
            response.put("tipoPerfil", usuarioLogado.getTipoPerfil()); // envia o tipo de perfil para o front-end
            System.out.println("Login bem-sucedido para: " + usuarioLogado.getEmail() + " (Tipo: " + usuarioLogado.getTipoPerfil() + ")");
            // retorna a resposta com status 200 OK
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // se dar ruim vai retornar erro
            response.put("sucesso", false);
            response.put("mensagem", "E-mail, senha ou CPF incorretos.");
            System.out.println("Falha no login para: " + conta.getEmail());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // mapeamento para a pagina principal, que so deve ser acessivel apos o login
    @GetMapping("/principal")
    public String mostrarPaginaPrincipal(HttpSession session, Model model) {
        // verifica se o usuario esta logado verificando o tipo de perfil na sessão
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");
        if (tipoPerfil == null) {
            // se não ha tipo de perfil na sessao, o usuario não está logado, então redireciona para a página de login
            return "redirect:/login";
        }
        model.addAttribute("tipoPerfil", tipoPerfil); // passa o tipo de perfil para o Thymeleaf
        return "principal";
    }

    // mapeamento para o logout
    @GetMapping("/logout")
    public String fazerLogout(HttpSession session) {
        session.invalidate(); // invalida a sessao
        return "redirect:/login"; // Redireciona para a página de login
    }
}