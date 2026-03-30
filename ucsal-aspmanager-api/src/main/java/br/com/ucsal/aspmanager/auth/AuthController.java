package br.com.ucsal.aspmanager.auth;

import br.com.ucsal.aspmanager.auth.dto.LoginRequest;
import br.com.ucsal.aspmanager.auth.dto.TokenResponse;
import br.com.ucsal.aspmanager.shared.security.jwt.JwtService;
import br.com.ucsal.aspmanager.usuario.dto.request.CriarUsuarioDto;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import br.com.ucsal.aspmanager.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder codificadorDeSenha;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder codificadorDeSenha, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usuarios = usuarios;
        this.codificadorDeSenha = codificadorDeSenha;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Usuario principal = (Usuario) authenticate.getPrincipal();
        String token = jwtService.gerarToken(principal);

        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody CriarUsuarioDto request) {

        Usuario usuario = Usuario.builder()
                .nomeCompleto(request.nomeCompleto())
                .email(request.email())
                .senha(codificadorDeSenha.encode(request.senha()))
                .perfil(request.perfil())
                .build();

        usuarios.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null));
    }

    @GetMapping("/test")
    public String teste() {
        return "teste auth";
    }

    @GetMapping("/prof")
    public String prof() {
        return "teste auth professor";
    }
}
