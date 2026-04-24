package br.com.ucsal.aspmanager.espaco.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload para análise/atualização de solicitação de reserva")
public record UpdateSolicitacaoRequest(
        @Schema(description = "ID do espaço da solicitação", example = "5")
        @NotNull(message = "Id do espaço não pode ser nulo!")
        Long idEspaco,

        @Schema(description = "ID do professor da solicitação", example = "12")
        @NotNull(message = "Id do professor não pode ser nulo!")
        Long idProfessor,

        @Schema(description = "Status de análise da solicitação", example = "APROVADO")
        @NotNull(message = "Status da solicitação não pode ser nulo!")
        StatusSolicitacao statusSolicitacao
) {
}
