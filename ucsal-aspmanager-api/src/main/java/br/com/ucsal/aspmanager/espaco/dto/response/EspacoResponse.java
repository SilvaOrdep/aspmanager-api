package br.com.ucsal.aspmanager.espaco.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta de espaço acadêmico")
public record EspacoResponse(
        @Schema(description = "ID do espaço", example = "5")
        Long id,

        @Schema(description = "Sigla do espaço", example = "LAB-01")
        String sigla,

        @Schema(description = "Nome do espaço", example = "Laboratório de Informática 1")
        String nome,

        @Schema(description = "Descrição do espaço", example = "Laboratório com 30 computadores para aulas práticas.")
        String descricao,

        @Schema(description = "Capacidade máxima", example = "30")
        Integer capacidadeMaxima,

        @Schema(description = "Localização", example = "Bloco B - 2º andar")
        String localizacao,

        @Schema(description = "Tipo de computadores disponíveis", example = "Desktop i5 16GB")
        String tipoComputadores,

        @Schema(description = "Tipo de espaço", example = "LABORATORIO")
        TipoEspaco tipoEspaco,

        @Schema(description = "ID da escola vinculada", example = "2")
        Long idEscola,

        @Schema(description = "IDs de softwares vinculados", example = "[4, 7, 9]")
        List<Long> softwares,

        @Schema(description = "Status do registro", example = "ATIVO")
        StatusRegistro statusRegistro
) {
}
