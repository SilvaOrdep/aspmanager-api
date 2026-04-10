package br.com.ucsal.aspmanager.instituicao.dto.response;

import java.util.List;

public record InstituicaoEnsinoResponse(
    Long id,
    String nome,
    String endereco,
    List<String> telefones
) {
}

