package com.bancodetalentos.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bancodetalentos.demo.repository.MySqlRepository;
import com.bancodetalentos.demo.model.contas;

@RestController
public class BancoController {

    @Autowired
    MySqlRepository mySqlRepository;

    @GetMapping("/get-all-contas")
    public List<contas> getAllContas() {

        return mySqlRepository.findAll();
    }
}
