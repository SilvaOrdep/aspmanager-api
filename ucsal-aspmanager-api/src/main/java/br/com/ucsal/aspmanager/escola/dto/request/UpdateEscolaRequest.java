package br.com.ucsal.aspmanager.escola.dto.request;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.validation.constraints.Size;

public record UpdateEscolaRequest(
    @Size(min = 3, max = 255, message = "Nome da escola deve ter entre 3 e 255 caracteres")
    String nome,

    Long idInstituicao,

    Long idCoordenador,

    StatusRegistro statusRegistro
) {
}

