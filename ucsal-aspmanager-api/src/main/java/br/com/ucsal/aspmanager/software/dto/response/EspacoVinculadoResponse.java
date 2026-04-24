package br.com.ucsal.aspmanager.software.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Espaço vinculado ao software")
public record EspacoVinculadoResponse(
        @Schema(description = "ID do espaço", example = "10")
        Long id,

        @Schema(description = "Sigla do espaço", example = "LAB-01")
        String sigla,

        @Schema(description = "Nome do espaço", example = "Laboratório de Redes")
        String nome
) {
}