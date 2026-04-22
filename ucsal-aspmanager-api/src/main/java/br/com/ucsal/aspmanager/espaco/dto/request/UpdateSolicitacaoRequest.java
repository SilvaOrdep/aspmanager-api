package br.com.ucsal.aspmanager.espaco.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public record UpdateSolicitacaoRequest(
        @NotNull(message = "Id do espaço não pode ser nulo!")
        Long idEspaco,
        @NotNull(message = "Id do professor não pode ser nulo!")
        Long idProfessor,
        @NotNull(message = "Status da solicitação não pode ser nulo!")
        StatusSolicitacao statusSolicitacao
) {
}
