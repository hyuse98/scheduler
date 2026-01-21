package com.hyuse98.scheduler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Title")
                        .version("1.0.0")
                        .description("Description")
                        .contact(new Contact()
                                .name("Equipe de Arquitetura")
                                .email("arquitetura@xyz.com")));
    }
}
