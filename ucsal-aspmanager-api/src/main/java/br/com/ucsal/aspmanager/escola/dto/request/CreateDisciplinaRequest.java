package br.com.ucsal.aspmanager.escola.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDisciplinaRequest(
        @NotBlank(message = "Nome da disciplina é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String nome,

        @NotBlank(message = "Descrição da disciplina não pode ser vazia!")
        @Size(min = 3, max = 500, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String descricao,

        @NotNull(message = "A disciplina precisa estar associada a uma escola!")
        Long idEscola

        ) {

}
