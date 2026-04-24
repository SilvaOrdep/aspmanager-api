package br.com.ucsal.aspmanager.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com token JWT")
public record TokenResponse(
	@Schema(description = "Token JWT para autenticação Bearer", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcm9mZXNzb3JAdWNzYWwuYnIiLCJleHAiOjE3NDQyMzQ4MDB9.signature")
	String token
) {
}
