package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Contas;
import com.bancodetalentos.demo.model.Perfis;
import com.bancodetalentos.demo.repository.ContasRepository;
import com.bancodetalentos.demo.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContasService {

    private final ContasRepository contasRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor para injeção de dependência.
     * Atualizado para incluir PerfilRepository e PasswordEncoder.
     */
    @Autowired
    public ContasService(ContasRepository contasRepository, PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder) {
        this.contasRepository = contasRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;

        System.out.println("=======================================");
        System.out.println("ContasService initialized with dependencies:");
        System.out.println("ContasRepository: " + contasRepository);
        System.out.println("PerfilRepository: " + perfilRepository);
        System.out.println("=======================================");
    }

    /**
     * Tenta encontrar uma conta por e-mail OU por CPF.
     */
    private Optional<Contas> findAccountByEmailOrCpf(String emailOuCpf) {
        Optional<Contas> contaPorEmail = contasRepository.findByEmail(emailOuCpf);
        if (contaPorEmail.isPresent()) {
            return contaPorEmail;
        }
        return contasRepository.findByCpf(emailOuCpf);
    }

    /**
     * Busca uma conta apenas pelo e-mail.
     * Usado pelo BancoController para popular a sessão após o login.
     */
    public Optional<Contas> findByEmail(String email) {
        return contasRepository.findByEmail(email);
    }

    /**
     * Valida as credenciais de login (e-mail e senha) fornecidas pelo usuário
     * consultando o banco de dados.
     */
    public String validarLogin(String emailOuCpf, String senha) {
        System.out.println("Tentando encontrar usuário: [" + emailOuCpf + "]");

        // 1. Encontra a conta (sem alteração aqui)
        Optional<Contas> contaOptional = findAccountByEmailOrCpf(emailOuCpf);

        // 2. Verifica se o usuário foi encontrado
        if (contaOptional.isPresent()) {
            Contas conta = contaOptional.get();

            // --- LÓGICA ATUALIZADA ---
            // Compara a senha digitada (senha) com o hash salvo no banco
            // (conta.getPassword())
            if (passwordEncoder.matches(senha, conta.getPassword())) {
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
        // 1. Encontra a conta (sem alteração aqui)
        Optional<Contas> contaOptional = findAccountByEmailOrCpf(emailOuCpf);

        // --- LÓGICA ATUALIZADA ---
        // Retorna a conta APENAS se ela existir E a senha digitada corresponder ao hash
        if (contaOptional.isPresent() && passwordEncoder.matches(senha, contaOptional.get().getPassword())) {
            return contaOptional;
        }

        return Optional.empty(); // Retorna vazio em todos os outros casos
    }

    /**
     * Registra um novo usuário no sistema.
     * 
     * @Transactional garante que ambas as operações (salvar conta, salvar perfil)
     *                funcionem, ou nenhuma delas. Se salvar o perfil falhar, a
     *                conta
     *                também não será salva (rollback).
     */
    @Transactional
    public void registrarNovaConta(String nome, String email, String telefone,
            String cpf, String instituicao, String cargo,
            String sobre, String senha, String tipoConta) throws Exception {

        // 1. Verificar se o usuário já existe (por CPF ou E-mail)
        if (contasRepository.findByCpf(cpf).isPresent() || contasRepository.findByEmail(email).isPresent()) {
            throw new Exception("CPF ou E-mail já cadastrado no sistema.");
        }

        // 2. Criar o objeto Contas (para login)
        Contas novaConta = new Contas();
        novaConta.setCpf(cpf);
        novaConta.setEmail(email);
        novaConta.setStatus(1); // 1 = Conta Ativa (padrão)

        // 3. HASH a senha antes de salvar
        novaConta.setPassword(passwordEncoder.encode(senha));

        // 4. Criar o objeto Perfis (para detalhes)
        Perfis novoPerfil = new Perfis();
        novoPerfil.setCpf(cpf); // O CPF é a "cola" que liga as duas tabelas
        novoPerfil.setNome(nome);
        novoPerfil.setTelefone(telefone);
        novoPerfil.setInstituicao(instituicao);
        novoPerfil.setCargo(cargo);
        novoPerfil.setSobre(sobre);
        novoPerfil.setTipoConta(tipoConta);

        // 5. Salvar ambas as entidades no banco de dados
        contasRepository.save(novaConta);
        perfilRepository.save(novoPerfil);
    }
}