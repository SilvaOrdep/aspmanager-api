package br.com.ucsal.aspmanager.instituicao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateInstituicaoEnsinoRequest(
    @NotBlank(message = "Nome da instituicao e obrigatorio")
    @Size(min = 3, max = 255, message = "Nome da instituicao deve ter entre 3 e 255 caracteres")
    String nome,

    @Size(max = 500, message = "Endereco deve ter no maximo 500 caracteres")
    String endereco,

    List<String> telefones
) {
}

