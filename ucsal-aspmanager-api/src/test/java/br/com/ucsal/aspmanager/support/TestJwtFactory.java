package br.com.ucsal.aspmanager.support;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.security.jwt.JwtService;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestJwtFactory {

    @Autowired
    private JwtService jwtService;

    public String token(Long id, String email, Perfil perfil) {
        Usuario usuario = Usuario.builder()
                .id(id)
                .nomeCompleto("Usuário de Teste")
                .email(email)
                .senha("senha")
                .perfil(perfil)
                .statusRegistro(StatusRegistro.ATIVO)
                .build();
        return jwtService.gerarToken(usuario);
    }
}

