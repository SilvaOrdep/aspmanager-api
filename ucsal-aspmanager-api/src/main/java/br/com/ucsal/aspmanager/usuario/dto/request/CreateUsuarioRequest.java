package br.com.ucsal.aspmanager.usuario.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateUsuarioRequest(
        @NotBlank(message = "Nome completo é obrigatório") @Size(min = 3, max = 254, message = "Nome completo deve ter entre 3 e 254 caracteres") String nomeCompleto,

        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,

        @NotBlank(message = "Senha é obrigatória") @Size(min = 6, max = 30, message = "Senha deve ter entre 6 e 30 caracteres") String senha,

        @NotNull(message = "Perfil é obrigatório") Perfil perfil,

        Long idEscola,

        String matricula,

        List<String> telefones
) {
}