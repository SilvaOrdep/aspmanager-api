package br.com.ucsal.aspmanager.auth;

import br.com.ucsal.aspmanager.auth.dto.LoginRequest;
import br.com.ucsal.aspmanager.auth.dto.TokenResponse;
import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
import br.com.ucsal.aspmanager.shared.security.jwt.JwtService;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de autenticação e emissão de token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(operationId = "loginUsuario", summary = "Autenticar usuário", description = "Valida credenciais de acesso e retorna um token JWT para uso nas demais rotas protegidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @SecurityRequirements
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(), request.senha());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Usuario principal = (Usuario) authenticate.getPrincipal();
        String token = jwtService.gerarToken(principal);

        return ResponseEntity.ok(new TokenResponse(token));
    }

}
