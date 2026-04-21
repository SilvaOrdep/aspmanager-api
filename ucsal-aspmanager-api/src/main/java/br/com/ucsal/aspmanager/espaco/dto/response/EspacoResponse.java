package br.com.ucsal.aspmanager.espaco.dto.response;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import br.com.ucsal.aspmanager.software.model.Software;

import java.util.List;

public record EspacoResponse(
        Long id,
        String sigla,
        String nome,
        String descricao,
        Integer capacidadeMaxima,
        String localizacao,
        String tipoComputadores,
        TipoEspaco tipoEspaco,
        Escola escola,
        List<Software> softwares,
        StatusRegistro statusRegistro
) {
}
