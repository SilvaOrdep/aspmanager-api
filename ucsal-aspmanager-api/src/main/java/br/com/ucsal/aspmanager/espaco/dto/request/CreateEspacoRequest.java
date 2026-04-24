package br.com.ucsal.aspmanager.espaco.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Payload para criação de espaço acadêmico")
public record CreateEspacoRequest(

    @Schema(description = "Sigla do espaço", example = "LAB-01")
    @NotBlank(message = "Sigla é obrigatória!")
    @Size(min = 2, max = 10, message = "Nome do espaço deve ter entre 2 e 10 caracteres!")
    String sigla,

    @Schema(description = "Nome do espaço", example = "Laboratório de Informática 1")
    @NotBlank(message = "Nome é obrigatório!")
    @Size(min = 3, max = 255, message = "Nome do espaço deve ter entre 3 e 255 caracteres!")
    String nome,

    @Schema(description = "Descrição do espaço", example = "Laboratório com 30 computadores para aulas práticas.")
    @NotBlank(message = "Descrição é obrigatória!")
    @Size(min = 3, max = 500, message = "Descrição do espaço deve ter entre 3 e 500 caracteres!")
    String descricao,

    @Schema(description = "Capacidade máxima do espaço", example = "30")
    @Min(value = 1, message = "A capacidade máxima dever ser no mínimo 1!")
    Integer capacidadeMaxima,

    @Schema(description = "Localização física do espaço", example = "Bloco B - 2º andar")
    @NotBlank(message = "Localização é obrigatória!")
    @Size(min = 3, max = 255, message = "Localização do espaço deve ter entre 3 e 255 caracteres!")
    String localizacao,

    @Schema(description = "Tipo de computadores disponíveis (obrigatório para laboratórios)", example = "Desktop i5 16GB")
    @NotBlank(message = "Tipo de computador é obrigatório!")
    String tipoComputadores,

    @Schema(description = "Tipo do espaço acadêmico", example = "LABORATORIO")
    @NotNull(message = "O tipo de espaço não pode ser nulo!")
    TipoEspaco tipoEspaco,

    @Schema(description = "ID da escola vinculada ao espaço", example = "2")
    @NotNull(message = "Escola associada ao espaço não pode ser nula!")
    Long idEscola,

    @Schema(description = "Lista de IDs de softwares instalados (aplicável para laboratórios)", example = "[4, 7, 9]")
    List<Long> softwares
) {
}
