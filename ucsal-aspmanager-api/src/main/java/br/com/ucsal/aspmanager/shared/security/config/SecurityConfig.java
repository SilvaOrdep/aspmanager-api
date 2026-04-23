package br.com.ucsal.aspmanager.shared.security.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autenticado"))
                    .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado"))
                )
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/professores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/professores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/professores/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/escola/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/escola/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/escola/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/escola/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/instituicao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/instituicao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/instituicao/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/instituicao/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/espaco").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/espaco/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/espaco/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/espaco/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/software").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/software/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/software/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/software/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/software/solicitacoes/minhas").hasRole("PROFESSOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/software/solicitacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/software/solicitacoes/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/software/solicitacoes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/software/solicitacoes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/espaco/solicitacao").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/espaco/solicitacao/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/espaco/solicitacao/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/espaco/solicitacao/*").hasRole("ADMIN")

                        // PROFESSOR
                        .requestMatchers(HttpMethod.POST, "/api/v1/software/solicitacoes").hasRole("PROFESSOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/espaco/solicitacao").hasRole("PROFESSOR")

                        // AMBOS
                        .requestMatchers(HttpMethod.GET, "/api/v1/escola/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/instituicao/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/espaco").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/espaco/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/espaco/disponiveis").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/software").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/software/*").hasAnyRole("ADMIN", "PROFESSOR")

                        // Dados do próprio usuário
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/*").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/alterar-senha").authenticated()

                        .anyRequest().denyAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
