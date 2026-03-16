package com.bancodetalentos.demo.Service;

import com.bancodetalentos.demo.model.Perfis;
import com.bancodetalentos.demo.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Este serviço lida com a lógica de negócios relacionada aos perfis de talento.
 *
 * VERSÃO CORRIGIDA:
 * Este serviço agora usa o PerfilRepository (Spring Data JPA) para se comunicar
 * com o banco de dados PostgreSQL, em vez de ler de um arquivo JSON.
 */
@Service
public class PerfilService {

    // Injeta o repositório JPA para perfis
    private final PerfilRepository perfilRepository;

    /**
     * Construtor para injeção de dependência.
     */
    @Autowired
    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    /**
     * Busca um perfil de talento específico usando o CPF como critério.
     * O CPF é a Chave Primária (@Id) da entidade Perfis.
     *
     * @param cpf O CPF do perfil a ser buscado.
     * @return Um {@link Optional} contendo o objeto {@link Perfis} se encontrado
     * no banco de dados.
     */
    public Optional<Perfis> buscarPerfilPorCpf(String cpf) {
        // Usa o método .findById() do JpaRepository.
        // Isso executa um "SELECT * FROM perfis WHERE cpf = ?" no PostgreSQL.
        System.out.println("--- PerfilService (JPA): Buscando perfil por CPF: " + cpf);
        return perfilRepository.findById(cpf);
    }

    /**
     * Salva ou atualiza um perfil no banco de dados.
     *
     * @param perfil O objeto {@link Perfis} a ser salvo.
     * @return O objeto {@link Perfis} salvo.
     */
    public Perfis atualizarPerfil(Perfis perfil) {
        // Usa o método .save() do JpaRepository.
        // Ele funciona como INSERT (se for novo) ou UPDATE (se já existir).
        return perfilRepository.save(perfil);
    }
}