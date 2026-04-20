package br.com.ucsal.aspmanager.escola.dto.response;

public record DisciplinaResponse(
        Long id,
        String nome,
        String descricao,
        Long idInstituicao
) {
}
