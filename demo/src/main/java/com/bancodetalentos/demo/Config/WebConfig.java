package com.bancodetalentos.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Make sure this path matches UPLOAD_DIR in InscricaoController.java
    // and that the 'uploads' folder actually exists at this location: C:/Codigo/projetodepesquisa/uploads/
    private static final String UPLOAD_DIRECTORY = "file:///C:/Codigo/projetodepesquisa/uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(UPLOAD_DIRECTORY);
        // This is important to continue serving your other static assets (CSS, JS, images)
        registry.addResourceHandler("/css/**", "/js/**", "/images/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", "classpath:/static/images/");
    }
}