package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Contas;
import com.bancodetalentos.demo.repository.ContasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Este serviço é a ponte entre o Spring Security e o seu banco de dados.
 * Ele implementa a interface UserDetailsService, que tem um único método:
 * loadUserByUsername().
 *
 * O Spring Security chamará este método automaticamente durante o processo de
 * login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Injeta o repositório para podermos consultar o banco de dados
    @Autowired
    private ContasRepository contasRepository;

    /**
     * Este é o método principal que o Spring Security usa.
     * O parâmetro 'username' (que vem do formulário) será, no nosso caso,
     * o e-mail ou CPF que o usuário digitou.
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrCpf) throws UsernameNotFoundException {
        System.out.println("--- CustomUserDetailsService: Tentando carregar usuário: " + emailOrCpf);

        // 1. Tenta encontrar o usuário pelo e-mail ou CPF
        Optional<Contas> contaOptional = findAccountByEmailOrCpf(emailOrCpf);

        // 2. Se o Optional estiver vazio (não encontrou),
        // é OBRIGATÓRIO lançar esta exceção.
        // O Spring Security vai interceptar isso e saber que o login falhou.
        if (contaOptional.isEmpty()) {
            System.out.println("--- CustomUserDetailsService: Usuário não encontrado.");
            throw new UsernameNotFoundException("Usuário (E-mail ou CPF) não encontrado: " + emailOrCpf);
        }

        // 3. Se encontrou, pega o objeto 'Contas'
        Contas conta = contaOptional.get();
        System.out.println("--- CustomUserDetailsService: Usuário encontrado: " + conta.getEmail());

        // 4. Cria e retorna um objeto 'User' do Spring Security.
        // Este objeto contém as informações essenciais para o Spring.
        // - 1º argumento: o "username" (vamos usar o e-mail, é único)
        // - 2º argumento: o HASH da senha (vindo direto do banco)
        // - 3º argumento: uma lista de "autoridades" ou "roles" (ex: "ROLE_ADMIN").
        //                  Vamos deixar vazia por enquanto.
        //
        // O Spring Security vai pegar a senha que o usuário digitou,
        // comparar com o hash (conta.getPassword()) usando o BCryptPasswordEncoder,
        // e decidir se o login é válido ou não.
        return new User(conta.getEmail(), conta.getPassword(), new ArrayList<>());
    }

    /**
     * Método auxiliar privado para buscar a conta por e-mail OU por CPF.
     * Reutiliza a lógica que você já tinha no ContasService.
     */
    private Optional<Contas> findAccountByEmailOrCpf(String emailOuCpf) {
        // Tenta por e-mail primeiro
        Optional<Contas> contaPorEmail = contasRepository.findByEmail(emailOuCpf);
        if (contaPorEmail.isPresent()) {
            return contaPorEmail;
        }
        // Se não achar, tenta por CPF
        return contasRepository.findByCpf(emailOuCpf);
    }
}