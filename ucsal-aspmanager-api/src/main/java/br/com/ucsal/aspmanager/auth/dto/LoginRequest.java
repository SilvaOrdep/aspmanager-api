package br.com.ucsal.aspmanager.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Email não pode ser vazio") String email, @NotBlank(message = "Senha não pode ser vazia") String senha) {
}
