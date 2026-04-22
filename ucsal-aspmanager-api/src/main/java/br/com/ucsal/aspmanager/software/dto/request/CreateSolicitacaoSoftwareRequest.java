package br.com.ucsal.aspmanager.software.dto.request;

import jakarta.validation.constraints.NotNull;


public record CreateSolicitacaoSoftwareRequest(
        @NotNull(message = "Software não pode ser nulo!")
        CreateSoftwareRequest software,
        @NotNull(message = "Id do Professor não pode ser nulo!")
        Long idProfessor
) {
}
