package com.bancodetalentos.demo.controller;

import com.bancodetalentos.demo.Service.InscricaoService;
import com.bancodetalentos.demo.model.Inscricao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class InscricaoController {

    private static final String UPLOAD_DIR = "C:/Codigo/projetodepesquisa/uploads/";

    private final InscricaoService inscricaoService;

    @Autowired
    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de uploads: " + UPLOAD_DIR + " - " + e.getMessage());
        }
    }

    @PostMapping("/inscricao/submit")
    public ResponseEntity<Map<String, Object>> submitInscricao(
            @RequestParam("titulo") String titulo,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "telefone", required = false) String telefone,
            @RequestParam(value = "mensagem", required = false) String mensagem,
            @RequestParam(value = "curriculo", required = false) MultipartFile curriculo,
            @RequestParam(value = "certificacoes", required = false) MultipartFile[] certificacoes) {

        Map<String, Object> response = new HashMap<>();

        try {
            String curriculoFileName = null;
            if (curriculo != null && !curriculo.isEmpty()) {
                curriculoFileName = saveFile(curriculo);
            }

            List<String> certificacoesFileNames = new ArrayList<>();
            if (certificacoes != null) {
                for (MultipartFile file : certificacoes) {
                    if (!file.isEmpty()) {
                        certificacoesFileNames.add(saveFile(file));
                    }
                }
            }

            Inscricao novaInscricao = new Inscricao();
            novaInscricao.setTitulo(titulo);
            novaInscricao.setNome(nome);
            novaInscricao.setEmail(email);
            novaInscricao.setCpf(cpf);
            novaInscricao.setTelefone(telefone);
            novaInscricao.setMensagem(mensagem);
            novaInscricao.setCurriculo(curriculoFileName);
            novaInscricao.setCertificacoes(certificacoesFileNames);

            inscricaoService.adicionarInscricao(novaInscricao);

            response.put("sucesso", true);
            response.put("mensagem", "Inscrição recebida com sucesso!");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo de inscrição: " + e.getMessage());
            response.put("sucesso", false);
            response.put("mensagem", "Erro ao processar o upload do arquivo.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar inscrição: " + e.getMessage());
            e.printStackTrace();
            response.put("sucesso", false);
            response.put("mensagem", "Erro interno ao salvar a inscrição.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/inscricoes/dados")
    public ResponseEntity<Map<String, Object>> getDadosInscricoes(
            HttpSession session,
            // REMOVED: @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nome", required = false) String nome) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");

        // ONLY rely on session-based admin authentication
        if (usuarioCpf == null || !"administrador".equals(tipoPerfil)) {
            response.put("sucesso", false);
            response.put("mensagem", "Acesso não autorizado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesFiltradas(titulo, nome);

        response.put("sucesso", true);
        response.put("inscricoes", inscricoes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/inscricoes/{id}")
    public ResponseEntity<Map<String, Object>> removerInscricao(
            @PathVariable String id,
            HttpSession session
            // REMOVED: @RequestParam(value = "password", required = false) String password
            ) {

        Map<String, Object> response = new HashMap<>();
        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");

        // ONLY rely on session-based admin authentication
        if (usuarioCpf == null || !"administrador".equals(tipoPerfil)) {
            response.put("sucesso", false);
            response.put("mensagem", "Acesso não autorizado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        boolean removido = inscricaoService.removerInscricao(id);

        if (removido) {
            response.put("sucesso", true);
            response.put("mensagem", "Inscrição removida com sucesso!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("sucesso", false);
            response.put("mensagem", "Inscrição não encontrada ou erro ao remover.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.copy(file.getInputStream(), filePath);
        System.out.println("Arquivo salvo: " + filePath.toAbsolutePath());
        return uniqueFileName;
    }

    @GetMapping("/exportar-inscricoes")
    public void exportarInscricoesCsv(
            HttpSession session,
            // REMOVED: @RequestParam(value = "password", required = false) String password,
            jakarta.servlet.http.HttpServletResponse response) throws IOException {

        String usuarioCpf = (String) session.getAttribute("usuarioLogadoCpf");
        String tipoPerfil = (String) session.getAttribute("tipoPerfil");

        // ONLY rely on session-based admin authentication
        if (usuarioCpf == null || !"administrador".equals(tipoPerfil)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Acesso não autorizado.");
            return;
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"inscricoes.csv\"");

        List<Inscricao> inscricoes = inscricaoService.buscarTodasInscricoes();

        try (java.io.PrintWriter writer = response.getWriter()) {
            writer.println("ID,Titulo,Nome,Email,CPF,Telefone,Mensagem,Curriculo,Certificacoes,DataInscricao");
            for (Inscricao inscricao : inscricoes) {
                String certs = inscricao.getCertificacoes() != null ? String.join(";", inscricao.getCertificacoes()) : "";
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        inscricao.getId(),
                        escapeCsv(inscricao.getTitulo()),
                        escapeCsv(inscricao.getNome()),
                        escapeCsv(inscricao.getEmail()),
                        escapeCsv(inscricao.getCpf()),
                        escapeCsv(inscricao.getTelefone()),
                        escapeCsv(inscricao.getMensagem()),
                        escapeCsv(inscricao.getCurriculo()),
                        escapeCsv(certs),
                        escapeCsv(inscricao.getDataInscricao())
                );
            }
        }
    }

    private String escapeCsv(String field) {
        if (field == null) {
            return "";
        }
        String escaped = field.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}