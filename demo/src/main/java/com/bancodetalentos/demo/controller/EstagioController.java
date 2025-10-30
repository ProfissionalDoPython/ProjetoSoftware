// In EstagioController.java
package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.EstagioService;
import com.bancodetalentos.demo.model.Estagio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; // Ensure this is @RestController
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // <--- THIS IS CRUCIAL AND MUST BE PRESENT
@RequestMapping("/api/estagios") // <--- Base path for this controller
public class EstagioController {

    private final EstagioService estagioService;

    @Autowired
    public EstagioController(EstagioService estagioService) {
        this.estagioService = estagioService;
    }

    @GetMapping("/todos") // <--- This maps to /api/estagios/todos
    public ResponseEntity<Map<String, Object>> buscarTodosEstagios(
            HttpSession session,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String modalidade,
            @RequestParam(required = false) String mesInicio) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Estagio> estagios = estagioService.buscarEstagiosFiltrados(area, modalidade, mesInicio);
        
        response.put("sucesso", true);
        response.put("estagios", estagios);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}