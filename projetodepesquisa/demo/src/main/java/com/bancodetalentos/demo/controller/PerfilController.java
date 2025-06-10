package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.PerfilService; 
import com.bancodetalentos.demo.model.Perfis; 
import jakarta.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// classico componente do spring
@Controller
@RequestMapping("/perfil") // prefixo para todos os endpoints deste controlador
public class PerfilController {

    private final PerfilService perfilService; 

    // to injetando a dependencia do PerfilService
    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    // endpoint para exibir a pagina do perfil
    @GetMapping 
    public String mostrarPaginaPerfil(HttpSession session) {
        // faço uma validação para ser se o usuario ta logado no sistema
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        if (usuarioCpf == null) {
            return "redirect:/login"; // se não manda o cara de volta para tela do login
        }
        return "perfil"; 
    }

    /**
     * uso isso para carregar os dados do perfil do usuário logado
     * e to retornando os dados do perfil como JSON
     */
    @GetMapping("/dados") 
    @ResponseBody 
    public ResponseEntity<Map<String, Object>> getDadosPerfil(HttpSession session) {
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");

        Map<String, Object> response = new HashMap<>();

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
        }

        // busco o perfil do usuario logado pelo CPF
        Optional<Perfis> perfil = perfilService.buscarPerfilPorCpf(usuarioCpf);

        if (perfil.isPresent()) {
            response.put("sucesso", true);
            response.put("perfil", perfil.get()); // retorna o objeto Perfil completo
            response.put("tipoPerfil", tipoPerfil); // retorna tambem o tipo de perfil para o javascripts
            return new ResponseEntity<>(response, HttpStatus.OK); // deu bom, da 200 OK
        } else {
            response.put("erro", "Perfil não encontrado para o CPF: " + usuarioCpf);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // qualquer coisa tem uma mensagem de erro 404
        }
    }

    /**
     * isso e para salvar (ou atualizar) os dados do perfil
     * no caso vem tudo como JSON
     */
    @PostMapping("/salvar") 
    @ResponseBody
    public ResponseEntity<Map<String, Object>> salvarPerfil(@RequestBody Perfis perfilAtualizado, HttpSession session) {
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        Map<String, Object> response = new HashMap<>();

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // garante que o CPF do perfil que está sendo salvo corresponde ao usuario logado
        if (!usuarioCpf.equals(perfilAtualizado.getCpf())) {
            response.put("erro", "Tentativa de salvar perfil de outro usuário.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // mensagem de erro show para 402
        }

        try {
            Perfis perfilSalvo = perfilService.atualizarPerfil(perfilAtualizado);
            response.put("sucesso", true);
            response.put("mensagem", "Perfil atualizado com sucesso!");
            response.put("perfil", perfilSalvo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("sucesso", false);
            response.put("mensagem", "Erro ao salvar perfil: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // da uma mensagem de erro, mas do tipo 500
        }
    }
}