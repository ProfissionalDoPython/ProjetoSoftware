package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.EventoService; // Importa o serviço de eventos
import com.bancodetalentos.demo.model.Evento; // Importa o modelo de Evento
import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência
import org.springframework.http.HttpStatus; // Para retornar códigos de status HTTP
import org.springframework.http.ResponseEntity; // Para construir respostas HTTP
import org.springframework.web.bind.annotation.*; // Para anotações como @GetMapping, @ResponseBody
import jakarta.servlet.http.HttpSession; // Para acessar a sessão do usuário
import org.springframework.web.bind.annotation.RestController; // Para definir este controlador como um controlador RESTful
import java.util.HashMap; // Para Mapas na resposta JSON
import java.util.List; // Para lista de eventos
import java.util.Map; // Para Mapas

/**
 * Controlador RESTful para operações relacionadas a Eventos.
 * Fornece endpoints para buscar e, futuramente, gerenciar eventos.
 */
@RestController // @RestController é uma especialização de @Controller que já inclui @ResponseBody
@RequestMapping("/api/eventos") // Prefixo para todos os endpoints deste controlador (ex: /api/eventos/todos)
public class EventoController {

    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * Endpoint para buscar todos os eventos.
     * Requer que o usuário esteja logado.
     * @param session A sessão HTTP para verificar o status de login do usuário.
     * @return ResponseEntity contendo a lista de eventos ou um erro se o usuário não estiver logado.
     */
    @GetMapping("/todos") // Mapeia para GET /api/eventos/todos
    public ResponseEntity<Map<String, Object>> buscarTodosEventos(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
        }

        List<Evento> eventos = eventoService.buscarTodosEventos();
        response.put("sucesso", true);
        response.put("eventos", eventos);
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    /**
     * Endpoint para buscar um evento específico pelo ID.
     * Requer que o usuário esteja logado.
     * @param id O ID do evento.
     * @param session A sessão HTTP para verificar o status de login do usuário.
     * @return ResponseEntity contendo o evento encontrado, ou um erro se não encontrado/não autorizado.
     */
    @GetMapping("/{id}") // Mapeia para GET /api/eventos/{id} (ex: /api/eventos/e001)
    public ResponseEntity<Map<String, Object>> buscarEventoPorId(@PathVariable String id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return eventoService.buscarEventoPorId(id)
                .map(evento -> {
                    response.put("sucesso", true);
                    response.put("evento", evento);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    response.put("erro", "Evento não encontrado com o ID: " + id);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 Não Encontrado
                });
    }

    // Futuramente, você pode adicionar endpoints para:
    // @PostMapping("/inscrever/{eventoId}") para lidar com inscrições
    // @PostMapping para criar novos eventos (para admins)
    // @PutMapping para atualizar eventos (para admins)
    // @DeleteMapping para deletar eventos (para admins)
}