package br.com.ucsal.aspmanager.software.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSolicitacaoSoftwareRequest(
        @NotNull(message = "Software não pode ser nulo!")
        CreateSoftwareRequest software,
        @NotNull(message = "Id do Professor não pode ser nulo!")
        Long idProfessor
) {
}
