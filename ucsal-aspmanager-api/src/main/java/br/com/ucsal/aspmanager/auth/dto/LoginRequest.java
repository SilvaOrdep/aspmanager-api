package br.com.ucsal.aspmanager.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload de autenticação do usuário")
public record LoginRequest(
	@Schema(description = "Email de acesso do usuário", example = "professor@ucsal.br")
	@NotBlank(message = "Email não pode ser vazio")
	String email,

	@Schema(description = "Senha de acesso do usuário", example = "Senha@123")
	@NotBlank(message = "Senha não pode ser vazia")
	String senha
) {
}
