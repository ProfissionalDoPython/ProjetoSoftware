package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.BolsaService; // Importa o serviço de bolsas
import com.bancodetalentos.demo.model.Bolsa; // Importa o modelo Bolsa
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador RESTful para operações relacionadas a Oportunidades de Bolsas Acadêmicas.
 * Fornece endpoints para buscar bolsas.
 */
@RestController // Indica que é um controlador REST que retorna dados no corpo da resposta (JSON).
@RequestMapping("/api/bolsas") // Define o caminho base para os endpoints deste controlador.
public class BolsaController {

    private final BolsaService bolsaService;

    @Autowired
    public BolsaController(BolsaService bolsaService) {
        this.bolsaService = bolsaService;
    }

    /**
     * Endpoint para buscar todas as oportunidades de bolsa ou bolsas filtradas.
     * Requer que o usuário esteja logado.
     * Mapeia para GET /api/bolsas/todos ou /api/bolsas/todos?area=X&modalidade=Y&mesInicio=Z
     * @param session A sessão HTTP para verificar o status de login.
     * @param area Parâmetro opcional para filtrar por área do conhecimento.
     * @param modalidade Parâmetro opcional para filtrar por modalidade.
     * @param mesInicio Parâmetro opcional para filtrar por mês de início.
     * @return ResponseEntity contendo a lista de bolsas ou um erro.
     */
    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> buscarTodasBolsas(
            HttpSession session,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String modalidade,
            @RequestParam(required = false) String mesInicio) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
        }

        List<Bolsa> bolsas = bolsaService.buscarBolsasFiltradas(area, modalidade, mesInicio);
        
        response.put("sucesso", true);
        response.put("bolsas", bolsas);
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }
}