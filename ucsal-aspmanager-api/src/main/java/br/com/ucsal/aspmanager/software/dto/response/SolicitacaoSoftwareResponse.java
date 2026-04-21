package br.com.ucsal.aspmanager.software.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SolicitacaoSoftwareResponse(
        Long id,
        LocalDate dataSolicitacao,
        TipoSolicitacaoSoftware tipoSolicitacaoSoftware,
        StatusSolicitacao statusSolicitacao,
        Long idSoftware,
        Long idProfessor
) {
}
