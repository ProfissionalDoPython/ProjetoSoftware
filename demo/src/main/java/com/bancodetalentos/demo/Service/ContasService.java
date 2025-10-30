package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Contas;
import com.bancodetalentos.demo.repository.ContasRepository; // <-- Uses the new REPOSITORY
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Este serviço lida com a lógica de negócios relacionada às contas dos
 * usuários.
 * Agora, ele usa o ContasRepository para interagir com o banco de dados
 * PostgreSQL.
 */
@Service
public class ContasService {

    private final ContasRepository contasRepository;

    /**
     * Construtor para injeção de dependência do ContasRepository.
     */
    @Autowired
    public ContasService(ContasRepository contasRepository) {
        this.contasRepository = contasRepository;

        System.out.println("=======================================");
        System.out.println("DIAGNÓSTICO DE BANCO DE DADOS (INICIALIZAÇÃO)");
        System.out.println("Procurando todas as contas no repositório...");

        // Tenta buscar todas as contas
        var contas = contasRepository.findAll();

        if (contas.isEmpty()) {
            System.out.println("RESULTADO: A APLICAÇÃO NÃO ENCONTROU CONTAS!");
            System.out.println("ISSO CONFIRMA O PROBLEMA (Provavelmente falta o COMMIT).");
        } else {
            System.out.println("RESULTADO: A aplicação encontrou " + contas.size() + " contas:");
            for (Contas c : contas) {
                System.out.println("  -> ID: " + c.getId() + ", Email: " + c.getEmail());
            }
        }
        System.out.println("=======================================");
        // --- FIM DO TESTE DE DIAGNÓSTICO ---
    }

    /**
     * Tenta encontrar uma conta por e-mail OU por CPF.
     */
    private Optional<Contas> findAccountByEmailOrCpf(String emailOuCpf) {
        // Primeiro, tenta encontrar por e-mail
        Optional<Contas> contaPorEmail = contasRepository.findByEmail(emailOuCpf);

        if (contaPorEmail.isPresent()) {
            return contaPorEmail; // Encontrou por e-mail
        }

        // Se não encontrou por e-mail, tenta por CPF
        return contasRepository.findByCpf(emailOuCpf);
    }

    /**
     * Valida as credenciais de login (e-mail e senha) fornecidas pelo usuário
     * consultando o banco de dados.
     */
    public String validarLogin(String emailOuCpf, String senha) {
        System.out.println("Tentando encontrar usuário: [" + emailOuCpf + "]");

        // 1. Usa o novo método para encontrar por e-mail ou CPF
        Optional<Contas> contaOptional = findAccountByEmailOrCpf(emailOuCpf);

        // 2. Verifica se o usuário foi encontrado
        if (contaOptional.isPresent()) {
            Contas conta = contaOptional.get();
            // 3. Verifica se a senha está correta
            // (Veja a Seção 3 abaixo para uma melhoria de segurança URGENTE)
            if (conta.verificarPassword(senha)) {
                return null; // Sucesso!
            } else {
                return "Senha incorreta.";
            }
        } else {
            // 4. Se o Optional estiver vazio, o usuário não existe
            return "Usuário (E-mail ou CPF) não encontrado.";
        }
    }

    /**
     * Retorna o objeto Contas validado, se o login for bem-sucedido.
     */
    public Optional<Contas> getValidAccount(String emailOuCpf, String senha) {
        // 1. Usa o novo método para encontrar por e-mail ou CPF
        Optional<Contas> contaOptional = findAccountByEmailOrCpf(emailOuCpf);

        // 2. Retorna a conta apenas se ela existir E a senha estiver correta
        if (contaOptional.isPresent() && contaOptional.get().verificarPassword(senha)) {
            return contaOptional;
        }

        return Optional.empty(); // Retorna vazio em todos os outros casos
    }
}