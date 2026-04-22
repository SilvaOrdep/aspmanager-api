package br.com.ucsal.aspmanager.espaco.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;

import java.time.LocalDate;
import java.time.LocalTime;

public record SolicitacaoResponse(
        Long idSolicitacao,
        String descricao,
        LocalDate dataUso,
        LocalTime horaInicio,
        LocalTime horaFim,
        Long idEspaco,
        Long idProfessor,
        StatusSolicitacao statusSolicitacao
) {
}
