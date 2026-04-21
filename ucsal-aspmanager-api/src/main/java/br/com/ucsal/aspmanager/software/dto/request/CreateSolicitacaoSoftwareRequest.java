package br.com.ucsal.aspmanager.software.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSolicitacaoSoftwareRequest(
        @NotNull(message = "Data da Solicitação não pode ser nula!")
        LocalDate dataSolicitacao,
        @NotNull(message = "Tipo da Solicitação não pode ser nulo!")
        TipoSolicitacaoSoftware tipoSolicitacaoSoftware,
        @NotNull(message = "Status da Solicitação não pode ser nulo!")
        StatusSolicitacao statusSolicitacao,
        @NotNull(message = "Id do Software não pode ser nulo!")
        Long idSoftware,
        @NotNull(message = "Id do Professor não pode ser nulo!")
        Long idProfessor
) {
}
