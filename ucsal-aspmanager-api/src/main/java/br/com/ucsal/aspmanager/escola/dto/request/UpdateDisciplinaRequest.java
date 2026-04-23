package br.com.ucsal.aspmanager.escola.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para atualização de disciplina")
public record UpdateDisciplinaRequest(
        @Schema(description = "Nome da disciplina", example = "Engenharia de Software")
        @Size(min = 3, max = 255, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Descrição da disciplina", example = "Disciplina com foco em processos e qualidade de software.")
        @Size(min = 3, max = 500, message = "Nome da disciplina deve ter entre 3 e 255 caracteres")
        String descricao,

        @Schema(description = "ID da escola associada", example = "2")
        Long idEscola

) {}
