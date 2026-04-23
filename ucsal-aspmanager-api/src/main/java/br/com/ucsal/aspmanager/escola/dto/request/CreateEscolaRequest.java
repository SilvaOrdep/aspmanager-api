package br.com.ucsal.aspmanager.escola.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Payload para criação de escola")
public record CreateEscolaRequest(
    @Schema(description = "Nome da escola", example = "Escola de Engenharias e Ciências Tecnológicas")
    @NotBlank(message = "Nome da escola é obrigatório!")
    @Size(min = 3, max = 255, message = "Nome da escola deve ter entre 3 e 255 caracteres!")
    String nome,

    @Schema(description = "ID da instituição de ensino vinculada", example = "1")
    @NotNull(message = "Escola é obrigatória!")
    Long idInstituicao,

    @Schema(description = "ID do coordenador da escola", example = "12")
    Long idCoordenador,

    @Schema(description = "Lista de IDs de disciplinas associadas à escola", example = "[3, 8, 15]")
    List<Long> idsDisciplinas
) {
}

