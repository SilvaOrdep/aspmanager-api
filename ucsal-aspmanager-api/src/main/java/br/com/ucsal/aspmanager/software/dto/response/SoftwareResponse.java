package br.com.ucsal.aspmanager.software.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;

import java.time.LocalDate;

public record SoftwareResponse(
        Long id,
        String nome,
        String versao,
        String urlDownload,
        String tipoLicenca,
        String objetivoUso,
        LocalDate dataCadastro,
        StatusRegistro statusRegistro
) {
}
