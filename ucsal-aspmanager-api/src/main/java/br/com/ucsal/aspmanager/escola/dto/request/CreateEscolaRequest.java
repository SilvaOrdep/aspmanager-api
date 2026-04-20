package br.com.ucsal.aspmanager.escola.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateEscolaRequest(
    @NotBlank(message = "Nome da escola é obrigatório!")
    @Size(min = 3, max = 255, message = "Nome da escola deve ter entre 3 e 255 caracteres!")
    String nome,

    @NotNull(message = "Escola é obrigatória!")
    Long idInstituicao,

    Long idCoordenador,

    List<Long> idsDisciplinas
) {
}

