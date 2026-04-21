package br.com.ucsal.aspmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "ASP Manager API",
                version = "1.0.0",
                description = "Documentação oficial das APIs de gerenciamento escolar da UCSAL."
        )
)
public class UcsalAspmanagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcsalAspmanagerApiApplication.class, args);
    }

}
