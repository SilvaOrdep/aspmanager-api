package br.com.ucsal.aspmanager.usuario.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizaUsuarioDto(
    @Size(min = 3, max = 254, message = "Nome completo deve ter entre 3 e 254 caracteres") String nomeCompleto,

    @Email(message = "Email inválido") String email,

    @Size(min = 6, max = 30, message = "Senha deve ter entre 6 e 30 caracteres") String senha,

    Perfil perfil,

    StatusRegistro statusRegistro) {
}
