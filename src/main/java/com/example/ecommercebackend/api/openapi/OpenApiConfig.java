package com.example.ecommercebackend.api.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mahyar Maleki
 */

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                        .addList("Basic Authorization").addList("Bearer Authentication"))
                .components(getComponents())
                .info(getInfo());
    }

    private Components getComponents() {
        return new Components().addSecuritySchemes("Basic Authorization", createBasicSecurityScheme())
                .addSecuritySchemes("Bearer Authorization", createAPIKeyScheme());

    }

    private SecurityScheme createBasicSecurityScheme() {
        return new SecurityScheme().name("Basic Authorization").type(SecurityScheme.Type.HTTP)
                .scheme("Basic");
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().name("Bearer Authorization").type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT").scheme("Bearer").in(SecurityScheme.In.HEADER);
    }

    private Info getInfo() {
        return new Info().title("My Rest API").description("Some custom description of API.").version("1.0")
                .contact(getContact())
                .license(getLicense());
    }

    private Contact getContact() {
        return new Contact().name("Mahyar Maleki").email("mahyarmaleki2003@gmail.com").url("company@example.com");
    }

    private License getLicense() {
        return new License().name("License of API").url("API license URL");
    }
}
