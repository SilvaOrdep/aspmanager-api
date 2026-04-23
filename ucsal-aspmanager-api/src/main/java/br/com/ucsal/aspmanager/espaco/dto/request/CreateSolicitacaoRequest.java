package br.com.ucsal.aspmanager.espaco.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Payload para criação de solicitação de reserva de espaço")
public record CreateSolicitacaoRequest(
        @Schema(description = "Descrição da solicitação", example = "Aula prática de redes de computadores")
        String descricao,

        @Schema(description = "Data de uso do espaço", example = "2026-05-12", format = "date")
        @NotNull(message = "Data não pode ser nula!")
        LocalDate dataUso,

        @Schema(description = "Horário de início", example = "08:00:00", format = "time")
        @NotNull(message = "Hora início não pode ser nula!")
        LocalTime horaInicio,

        @Schema(description = "Horário de término", example = "10:00:00", format = "time")
        @NotNull(message = "Hora fim não pode ser nula!")
        LocalTime horaFim,

        @Schema(description = "ID do espaço solicitado", example = "5")
        @NotNull(message = "Id do espaço não pode ser nulo!")
        Long idEspaco,

        @Schema(description = "ID do professor solicitante", example = "12")
        @NotNull(message = "Id do professor não pode ser nulo!")
        Long idProfessor
) {
}
