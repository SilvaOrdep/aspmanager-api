package br.com.ucsal.aspmanager.usuario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUsuarioRequest(
    @Size(min = 3, max = 254, message = "Nome completo deve ter entre 3 e 254 caracteres") String nomeCompleto,

    @Email(message = "Email inválido") String email,

    List<String> telefones // telefones = [] → apaga todos e telefones = null → não mexe nos telefones
) {
}
