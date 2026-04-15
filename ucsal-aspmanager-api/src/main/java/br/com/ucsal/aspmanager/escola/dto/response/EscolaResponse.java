package br.com.ucsal.aspmanager.escola.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;

import java.util.List;

public record EscolaResponse(
    Long id,
    String nome,
    StatusRegistro statusRegistro,
    Long idInstituicao,
    Long idCoordenador,
    List<Long> idsDisciplinas
) {
}

