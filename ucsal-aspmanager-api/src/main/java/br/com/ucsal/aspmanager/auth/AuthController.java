package br.com.ucsal.aspmanager.auth;

import br.com.ucsal.aspmanager.auth.dto.LoginRequest;
import br.com.ucsal.aspmanager.auth.dto.TokenResponse;
import br.com.ucsal.aspmanager.shared.security.jwt.JwtService;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    @SecurityRequirements
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Usuario principal = (Usuario) authenticate.getPrincipal();
        String token = jwtService.gerarToken(principal);

        return ResponseEntity.ok(new TokenResponse(token));
    }

}
