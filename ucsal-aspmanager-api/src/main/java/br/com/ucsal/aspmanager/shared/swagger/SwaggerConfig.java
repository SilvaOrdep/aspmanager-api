package br.com.ucsal.aspmanager.shared.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI customizacaoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ASP Manager API")
                        .version("1.0.0")
                        .description("Documentação oficial das APIs de gerenciamento escolar da UCSAL."));
    }
}
