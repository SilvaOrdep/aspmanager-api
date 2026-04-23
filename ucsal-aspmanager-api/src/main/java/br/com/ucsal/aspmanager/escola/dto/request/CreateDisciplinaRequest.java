package br.com.ucsal.aspmanager.escola.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para criação de disciplina")
public record CreateDisciplinaRequest(
        @Schema(description = "Nome da disciplina", example = "Programação Orientada a Objetos")
        @NotBlank(message = "Nome da disciplina é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Descrição da disciplina", example = "Disciplina focada em fundamentos de orientação a objetos.")
        @NotBlank(message = "Descrição da disciplina não pode ser vazia!")
        @Size(min = 3, max = 500, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String descricao,

        @Schema(description = "ID da escola associada", example = "2")
        @NotNull(message = "A disciplina precisa estar associada a uma escola!")
        Long idEscola

        ) {

}
