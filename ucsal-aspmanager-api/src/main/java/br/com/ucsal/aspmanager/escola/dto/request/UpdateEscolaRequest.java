package br.com.ucsal.aspmanager.escola.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para atualização de escola")
public record UpdateEscolaRequest(
    @Schema(description = "Nome da escola", example = "Escola de Ciências Naturais e da Saúde")
    @Size(min = 3, max = 255, message = "Nome da escola deve ter entre 3 e 255 caracteres")
    String nome,

    @Schema(description = "ID da instituição de ensino vinculada", example = "1")
    Long idInstituicao,

    @Schema(description = "ID do coordenador da escola", example = "12")
    Long idCoordenador,

    @Schema(description = "Status do registro da escola", example = "ATIVO")
    StatusRegistro statusRegistro
) {
}

