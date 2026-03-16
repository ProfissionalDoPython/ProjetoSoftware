/*package com.bancodetalentos.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void enviarEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("E-mail enviado com sucesso para: " + to);
        } catch (Exception e) {
            System.err.println("ERRO ao enviar e-mail: " + e.getMessage());
            e.printStackTrace();
            // Lançar exceção ou tratar erro conforme a política da aplicação
            throw new RuntimeException("Falha no envio de e-mail.", e);
        }
    }
}
*/