package br.com.ucsal.aspmanager.espaco.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateSolicitacaoRequest(
        String descricao,
        @NotNull(message = "Data não pode ser nula!")
        LocalDate dataUso,
        @NotNull(message = "Hora início não pode ser nula!")
        LocalTime horaInicio,
        @NotNull(message = "Hora fim não pode ser nula!")
        LocalTime horaFim,
        @NotNull(message = "Id do espaço não pode ser nulo!")
        Long idEspaco,
        @NotNull(message = "Id do professor não pode ser nulo!")
        Long idProfessor
) {
}
