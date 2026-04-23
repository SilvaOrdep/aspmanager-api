package br.com.ucsal.aspmanager.instituicao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Payload para atualização de instituição de ensino")
public record UpdateInstituicaoEnsinoRequest(
        @Schema(description = "Nome da instituição", example = "Universidade Católica do Salvador")
        @Size(min = 3, max = 255, message = "Nome da instituicao deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Endereço da instituição", example = "Av. Professor Pinto de Aguiar, 2589 - Pituaçu, Salvador - BA")
        @Size(max = 500, message = "Endereco deve ter no maximo 500 caracteres")
        String endereco,

        @Schema(description = "Telefones da instituição", example = "[\"7132017000\"]")
        List<String> telefones
) {
}

