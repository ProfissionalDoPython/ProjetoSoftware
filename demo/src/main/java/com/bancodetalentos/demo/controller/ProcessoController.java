package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.ProcessoService; // Importa o serviço de processos (nome corrigido)
import com.bancodetalentos.demo.model.Processo; // Importa o modelo Processo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Para parâmetros de requisição (filtros)
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador RESTful para operações relacionadas a Processos Seletivos.
 * Fornece endpoints para buscar processos.
 */
@RestController // Indica que é um controlador REST que retorna dados no corpo da resposta (JSON/XML).
@RequestMapping("/api/processos") // Define o caminho base para os endpoints deste controlador.
public class ProcessoController {

    private final ProcessoService processoService; // Injeta o serviço de processos

    @Autowired
    public ProcessoController(ProcessoService processoService) {
        this.processoService = processoService;
    }

    /**
     * Endpoint para buscar todos os processos seletivos ou processos filtrados.
     * Requer que o usuário esteja logado.
     * Mapeia para GET /api/processos/todos ou /api/processos/todos?area=X&modalidade=Y&mes=Z
     * @param session A sessão HTTP para verificar o status de login.
     * @param area Parâmetro opcional para filtrar por área do conhecimento.
     * @param modalidade Parâmetro opcional para filtrar por modalidade.
     * @param mes Parâmetro opcional para filtrar por mês.
     * @return ResponseEntity contendo a lista de processos ou um erro.
     */
    @GetMapping("/todos")
    public ResponseEntity<Map<String, Object>> buscarTodosProcessos(
            HttpSession session,
            @RequestParam(required = false) String area, // @RequestParam para pegar os filtros da URL
            @RequestParam(required = false) String modalidade,
            @RequestParam(required = false) String mes) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");

        if (usuarioCpf == null) {
            response.put("erro", "Usuário não logado. Acesso restrito.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
        }

        // Usa o serviço para buscar processos, aplicando os filtros se estiverem presentes
        List<Processo> processos = processoService.buscarProcessosFiltrados(area, modalidade, mes);
        
        response.put("sucesso", true);
        response.put("processos", processos);
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    // Futuramente, outros endpoints podem ser adicionados, como para buscar um processo por ID
    // @GetMapping("/{id}") para buscar um processo específico
    // @PostMapping("/inscricao") para lidar com o formulário de inscrição
}