package br.com.ucsal.aspmanager.instituicao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta de instituição de ensino")
public record InstituicaoEnsinoResponse(
        @Schema(description = "ID da instituição", example = "1")
        Long id,

        @Schema(description = "Nome da instituição", example = "Universidade Católica do Salvador")
        String nome,

        @Schema(description = "Endereço da instituição", example = "Av. Professor Pinto de Aguiar, 2589 - Pituaçu, Salvador - BA")
        String endereco,

        @Schema(description = "Telefones da instituição", example = "[\"7132017000\", \"71999998888\"]")
        List<String> telefones
) {
}

