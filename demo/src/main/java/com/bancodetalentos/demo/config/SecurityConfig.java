package com.bancodetalentos.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        // 1. Autorização: Defina quem pode acessar o quê
                                .authorizeHttpRequests(authorize -> authorize
                                // 2. Permita acesso público a estas URLs
                                .requestMatchers(
                                                 "/",
                                                 "/login",
                                                 "/faleconosco",
                                                 "/cadastro",
                                                 "/recuperarsenha",
                                                 "/css/**",
                                                 "/js/**",
                                                  "/img/**")
                                .permitAll()
                                 // 3. Exija autenticação para todas as outras URLs
                                         .anyRequest().authenticated())

                                // 4. ATIVE e CONFIGURE o formLogin do Spring Security
                                .formLogin(form -> form
                                         .loginPage("/login") // Diz ao Spring qual é a URL da página de login
                                         .loginProcessingUrl("/login") // URL para a qual o <form> do login.html
                                          // deve enviar (o Spring
                                         // vai interceptar)
                                         .usernameParameter("email") // Diz ao Spring que o campo de username se
                                     // chama "email"
                                         .defaultSuccessUrl("/principal", true) // Para onde ir após o login com
                                          // sucesso
                                          .failureUrl("/login?error=true") // Para onde ir se o login falhar
                                          .permitAll() // Permite acesso a todas as URLs de login
                                )

                                // 5. Mantenha o CSRF desabilitado (simples, mas funcional por agora)
                                .csrf(csrf -> csrf.disable())

                                // 6. Configure o /logout
                                .logout(logout -> logout
                                        .logoutUrl("/logout") // A URL que aciona o logout
                                         .logoutSuccessUrl("/login?logout") // Para onde ir após o logout
                                         .invalidateHttpSession(true) // Invalida a sessão
                                         .deleteCookies("JSESSIONID") // Limpa os cookies
                                          .permitAll());

                return http.build();
        }
}