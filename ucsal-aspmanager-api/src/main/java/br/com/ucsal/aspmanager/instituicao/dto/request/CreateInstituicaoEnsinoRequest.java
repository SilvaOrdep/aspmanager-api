package br.com.ucsal.aspmanager.instituicao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Payload para criação de instituição de ensino")
public record CreateInstituicaoEnsinoRequest(
        @Schema(description = "Nome da instituição de ensino", example = "Universidade Católica do Salvador")
        @NotBlank(message = "Nome da instituicao e obrigatorio")
        @Size(min = 3, max = 255, message = "Nome da instituicao deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Endereço da instituição", example = "Av. Professor Pinto de Aguiar, 2589 - Pituaçu, Salvador - BA")
        @Size(max = 500, message = "Endereco deve ter no maximo 500 caracteres")
        String endereco,

        @Schema(description = "Lista de telefones de contato", example = "[\"7132017000\", \"71999998888\"]")
        List<String> telefones
) {
}

