package br.com.ucsal.aspmanager.usuario.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;

import java.util.List;

public record UsuarioResponse(
    Long id,
    String nomeCompleto,
    String email,
    Perfil perfil,
    StatusRegistro statusRegistro,
    List<String> telefones) {
}
