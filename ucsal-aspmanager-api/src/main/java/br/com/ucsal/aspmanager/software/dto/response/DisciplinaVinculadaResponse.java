package br.com.ucsal.aspmanager.software.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Disciplina vinculada ao software")
public record DisciplinaVinculadaResponse(
        @Schema(description = "ID da disciplina", example = "5")
        Long id,

        @Schema(description = "Nome da disciplina", example = "Estruturas de Dados")
        String nome,

        @Schema(description = "ID da escola da disciplina", example = "2")
        Long idEscola
) {
}