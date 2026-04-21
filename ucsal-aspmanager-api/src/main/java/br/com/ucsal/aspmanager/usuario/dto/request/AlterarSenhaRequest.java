package br.com.ucsal.aspmanager.usuario.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequest(
        @NotBlank(message = "Senha antiga não pode estar vazia") String senhaAntiga,
        @Size(min = 6, max = 30, message = "Senha deve ter entre 6 e 30 caracteres") String senhaNova
) {
}
