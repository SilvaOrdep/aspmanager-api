package br.com.ucsal.aspmanager.software.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateSoftwareRequest(
        @NotBlank(message = "Nome do Software é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome do Software deve ter entre 3 e 255 caracteres")
        String nome,
        @NotBlank(message = "Versão do Software é obrigatória!")
        @Size(min = 3, max = 255, message = "Versão do Software deve ter entre 3 e 255 caracteres")
        String versao,
        @NotBlank(message = "URL de download é obrigatória!")
        String urlDownload,
        @NotBlank(message = "Tipo de lincença do software é obrigatório!")
        @Size(min = 3, max = 255, message = "Tipo de licença do software deve ter entre 3 e 255 caracteres")
        String tipoLicenca,
        @NotBlank(message = "Objetivo de uso do software é obrigatório!")
        String objetivoUso,
        @NotNull(message = "Data de cadastro não pode ser nula")
        LocalDate dataCadastro

) {
}
