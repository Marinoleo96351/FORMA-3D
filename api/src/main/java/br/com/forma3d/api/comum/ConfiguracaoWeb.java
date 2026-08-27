package br.com.forma3d.api.comum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Libera o acesso de origem cruzada para o endereco da interface. */
@Configuration
public class ConfiguracaoWeb implements WebMvcConfigurer {

    private final String[] origens;

    public ConfiguracaoWeb(@Value("${app.cors.origens:http://localhost:5173}") String[] origens) {
        this.origens = origens;
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registro) {
        registro.addMapping("/api/**")
            .allowedOrigins(origens)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*");
    }
}
