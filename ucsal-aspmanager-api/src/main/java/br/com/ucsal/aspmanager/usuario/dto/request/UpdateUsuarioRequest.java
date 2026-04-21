package br.com.ucsal.aspmanager.usuario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
    @Size(min = 3, max = 254, message = "Nome completo deve ter entre 3 e 254 caracteres") String nomeCompleto,

    @Email(message = "Email inválido") String email) {
}
