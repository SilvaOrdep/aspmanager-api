package br.com.ucsal.aspmanager.escola.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEscolaRequest(
    @NotBlank(message = "Nome da escola e obrigatorio")
    @Size(min = 3, max = 255, message = "Nome da escola deve ter entre 3 e 255 caracteres")
    String nome,

    @NotNull(message = "Instituicao e obrigatoria")
    Long idInstituicao,

    Long idCoordenador
) {
}

