package br.com.ucsal.aspmanager.shared.security.jwt;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("idUsuario", usuario.getId())
                .withClaim("perfil", usuario.getPerfil().name())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(84600))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<UsuarioResponse> validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build().verify(token);
        //tratamento de exceção
        return Optional.of(new UsuarioResponse(decodedJWT.getClaim("idUsuario").asLong(), null, decodedJWT.getSubject(),
                Perfil.valueOf(decodedJWT.getClaim("perfil").asString()), null, null, null));
    }

}
