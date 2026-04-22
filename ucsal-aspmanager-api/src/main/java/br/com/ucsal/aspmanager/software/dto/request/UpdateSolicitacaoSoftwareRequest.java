package br.com.ucsal.aspmanager.software.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;


public record UpdateSolicitacaoSoftwareRequest(
        @NotNull(message = "Status da Solicitação não pode ser nulo!")
        StatusSolicitacao statusSolicitacao
) {
}
