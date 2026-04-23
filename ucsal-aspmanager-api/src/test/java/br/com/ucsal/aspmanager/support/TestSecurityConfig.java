package br.com.ucsal.aspmanager.support;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = TestJwtFactory.class)
public class TestSecurityConfig {
}

