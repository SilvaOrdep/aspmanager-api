package br.com.ucsal.aspmanager.software.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Resposta de solicitação de software")
public record SolicitacaoSoftwareResponse(
        @Schema(description = "ID da solicitação", example = "21")
        Long id,

        @Schema(description = "Data da solicitação", example = "2026-04-23", format = "date")
        LocalDate dataSolicitacao,

        @Schema(description = "Tipo da solicitação de software", example = "ATIVACAO")
        TipoSolicitacaoSoftware tipoSolicitacaoSoftware,

        @Schema(description = "Status da solicitação", example = "PENDENTE")
        StatusSolicitacao statusSolicitacao,

        @Schema(description = "Dados do software solicitado")
        CreateSoftwareRequest software,

        @Schema(description = "ID do professor solicitante", example = "12")
        Long idProfessor,

        @Schema(description = "ID do software criado após aprovação (quando aplicável)", example = "9")
        Long idSoftwareCriado
) {
}
