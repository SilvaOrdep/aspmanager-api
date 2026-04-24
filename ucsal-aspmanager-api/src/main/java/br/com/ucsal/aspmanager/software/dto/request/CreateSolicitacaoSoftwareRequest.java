package br.com.ucsal.aspmanager.software.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


@Schema(description = "Payload para criação de solicitação de software")
public record CreateSolicitacaoSoftwareRequest(
        @Schema(description = "Dados do software solicitado")
        @NotNull(message = "Software não pode ser nulo!")
        CreateSoftwareRequest software,

        @Schema(description = "ID do professor solicitante", example = "12")
        @NotNull(message = "Id do Professor não pode ser nulo!")
        Long idProfessor
) {
}
