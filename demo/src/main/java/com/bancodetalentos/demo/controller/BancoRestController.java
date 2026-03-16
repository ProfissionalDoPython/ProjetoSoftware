package com.bancodetalentos.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.bancodetalentos.demo.model.Contas;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BancoRestController {

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Contas conta) {
        Map<String, Object> response = new HashMap<>();

        
        if ("teste@ueg.br".equals(conta.getEmail()) &&
            "123456".equals(conta.getPassword()) &&
            "00011122233".equals(conta.getCpf())) {
            response.put("sucesso", true);
        } else {
            response.put("sucesso", false);
        }
        return ResponseEntity.ok(response);
    }
}
