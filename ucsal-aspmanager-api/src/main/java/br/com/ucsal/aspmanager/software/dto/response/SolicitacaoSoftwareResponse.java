package br.com.ucsal.aspmanager.software.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SolicitacaoSoftwareResponse(
        Long id,
        LocalDate dataSolicitacao,
        TipoSolicitacaoSoftware tipoSolicitacaoSoftware,
        StatusSolicitacao statusSolicitacao,
        CreateSoftwareRequest software,
        Long idProfessor
) {
}
