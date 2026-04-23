package br.com.ucsal.aspmanager.software.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Resposta de software")
public record SoftwareResponse(
        @Schema(description = "ID do software", example = "9")
        Long id,

        @Schema(description = "Nome do software", example = "IntelliJ IDEA")
        String nome,

        @Schema(description = "Versão do software", example = "2026.1")
        String versao,

        @Schema(description = "URL de download", example = "https://www.jetbrains.com/idea/download")
        String urlDownload,

        @Schema(description = "Tipo de licença", example = "Educacional")
        String tipoLicenca,

        @Schema(description = "Objetivo de uso", example = "Apoio às disciplinas de programação")
        String objetivoUso,

        @Schema(description = "Data de cadastro", example = "2026-04-23", format = "date")
        LocalDate dataCadastro,

        @Schema(description = "Status do registro", example = "ATIVO")
        StatusRegistro statusRegistro
) {
}
