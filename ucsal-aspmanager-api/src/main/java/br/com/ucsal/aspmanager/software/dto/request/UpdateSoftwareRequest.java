package br.com.ucsal.aspmanager.software.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload para atualização de software")
public record UpdateSoftwareRequest(
        @Schema(description = "Nome do software", example = "IntelliJ IDEA")
        @NotBlank(message = "Nome do Software é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome do Software deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Versão do software", example = "2026.1")
        @NotBlank(message = "Versão do Software é obrigatória!")
        @Size(min = 3, max = 255, message = "Versão do Software deve ter entre 3 e 255 caracteres")
        String versao,

        @Schema(description = "URL de download", example = "https://www.jetbrains.com/idea/download")
        @NotBlank(message = "URL de download é obrigatória!")
        String urlDownload,

        @Schema(description = "Tipo de licença", example = "Educacional")
        @NotBlank(message = "Tipo de lincença do software é obrigatório!")
        @Size(min = 3, max = 255, message = "Tipo de licença do software deve ter entre 3 e 255 caracteres")
        String tipoLicenca,

        @Schema(description = "Objetivo de uso", example = "Uso em aulas práticas de desenvolvimento")
        @NotBlank(message = "Objetivo de uso do software é obrigatório!")
        String objetivoUso

) {
}
