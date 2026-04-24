package br.com.ucsal.aspmanager.espaco.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para atualização de espaço acadêmico")
public record UpdateEspacoRequest(

        @Schema(description = "Sigla do espaço", example = "AUD-01")
        @NotBlank(message = "Sigla é obrigatória!")
        @Size(min = 2, max = 10, message = "Nome do espaço deve ter entre 2 e 10 caracteres!")
        String sigla,

        @Schema(description = "Nome do espaço", example = "Auditório Principal")
        @NotBlank(message = "Nome é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome do espaço deve ter entre 3 e 255 caracteres!")
        String nome,

        @Schema(description = "Descrição do espaço", example = "Auditório com capacidade para eventos acadêmicos.")
        @NotBlank(message = "Descrição é obrigatória!")
        @Size(min = 3, max = 500, message = "Descrição do espaço deve ter entre 3 e 500 caracteres!")
        String descricao,

        @Schema(description = "Capacidade máxima", example = "120")
        @Min(value = 1, message = "A capacidade máxima dever ser no mínimo 1!")
        Integer capacidadeMaxima,

        @Schema(description = "Localização física", example = "Bloco A - Térreo")
        @NotBlank(message = "Localização é obrigatória!")
        @Size(min = 3, max = 255, message = "Localização do espaço deve ter entre 3 e 255 caracteres!")
        String localizacao,

        @Schema(description = "Tipo de computadores disponíveis", example = "Desktop i7 16GB")
        @NotBlank(message = "Tipo de computador é obrigatório!")
        String tipoComputadores,

        @Schema(description = "Tipo do espaço", example = "AUDITORIO")
        @NotNull(message = "O tipo de espaço não pode ser nulo!")
        TipoEspaco tipoEspaco,

        @Schema(description = "ID da escola vinculada", example = "2")
        @NotNull(message = "Escola associada ao espaço não pode ser nula!")
        Long idEscola
) {
}
