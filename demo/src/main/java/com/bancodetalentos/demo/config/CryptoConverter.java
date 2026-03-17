package com.bancodetalentos.demo.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    // ALERTA: Esta é a chave secreta usada para trancar/destrancar os dados.
    // Ela DEVE ter exatamente 16 caracteres (para AES-128 bits).
    // Em um sistema real, essa chave deve vir do application.properties ou variáveis de ambiente!
    private static final String SECRET_KEY = "UegChaveSecreta!"; 
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding"; // Modo determinístico (necessário para buscas)

    @Override
    public String convertToDatabaseColumn(String cpfAberto) {
        if (cpfAberto == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            // Criptografa e transforma em texto Base64 para salvar no banco
            return Base64.getEncoder().encodeToString(cipher.doFinal(cpfAberto.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar o CPF", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String cpfCriptografadoNoBanco) {
        if (cpfCriptografadoNoBanco == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            // Pega o texto do banco, decodifica do Base64 e descriptografa
            return new String(cipher.doFinal(Base64.getDecoder().decode(cpfCriptografadoNoBanco)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar o CPF", e);
        }
    }
}