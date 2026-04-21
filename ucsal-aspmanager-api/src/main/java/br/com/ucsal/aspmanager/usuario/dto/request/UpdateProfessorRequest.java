package br.com.ucsal.aspmanager.usuario.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateProfessorRequest(
        @Size(min = 3, max = 100, message = "Matrícula deve ter entre 3 e 100 caracteres") String matricula,
        Long idEscola
) {
}

