package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.EventsService; 
import com.bancodetalentos.demo.model.Events; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; 
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador RESTful para operações relacionadas a Eventos.
 * Fornece endpoints para buscar eventos.
 */
@RestController // CRUCIAL: Must be present
@RequestMapping("/api/events") // Changed mapping from /api/evento to /api/events
public class EventsController { // Changed class name

    private final EventsService eventsService; // Changed service name

    @Autowired
    public EventsController(EventsService eventsService) { // Changed service name in constructor
        this.eventsService = eventsService;
    }

    /**
     * Endpoint para buscar todos os eventos ou eventos filtrados.
     * Requer que o usuário esteja logado.
     * Mapeia para GET /api/events/todos ou /api/events/todos?area=X&modalidade=Y&mes=Z
     * @param session A sessão HTTP para verificar o status de login.
     * @param area Parâmetro opcional para filtrar por área do conhecimento.
     * @param modalidade Parâmetro opcional para filtrar por modalidade.
     * @param mes Parâmetro opcional para filtrar por mês.
     * @return ResponseEntity contendo a lista de eventos ou um erro.
     */
    @GetMapping("/todos") // Changed mapping
    public ResponseEntity<Map<String, Object>> buscarTodosEventos( // Changed method name
            HttpSession session,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String modalidade,
            @RequestParam(required = false) String mes) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
        }

        List<Events> events = eventsService.buscarEventosFiltrados(area, modalidade, mes); // Changed variable and method
        
        response.put("sucesso", true);
        response.put("events", events); // Changed key in response map
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    /**
     * Endpoint para buscar um evento específico pelo seu ID.
     * Requer que o usuário esteja logado.
     * @param id O ID do evento.
     * @param session A sessão HTTP para verificar o status de login do usuário.
     * @return ResponseEntity contendo o evento encontrado, ou um erro se não encontrado/não autorizado.
     */
    @GetMapping("/{id}") // Changed mapping slightly if it was different
    public ResponseEntity<Map<String, Object>> buscarEventoPorId(@PathVariable String id, HttpSession session) { // Changed method name
        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return eventsService.buscarEventoPorId(id) // Changed service name
                .map(event -> { // Changed variable name
                    response.put("sucesso", true);
                    response.put("event", event); // Changed key in response map
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    response.put("erro", "Evento não encontrado com o ID: " + id);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }
}