package br.com.ucsal.aspmanager.espaco.dto.request;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import br.com.ucsal.aspmanager.software.model.Software;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateEspacoRequest(

        @NotBlank(message = "Sigla é obrigatória!")
        @Size(min = 2, max = 10, message = "Nome do espaço deve ter entre 2 e 10 caracteres!")
        String sigla,

        @NotBlank(message = "Nome é obrigatório!")
        @Size(min = 3, max = 255, message = "Nome do espaço deve ter entre 3 e 255 caracteres!")
        String nome,

        @NotBlank(message = "Descrição é obrigatória!")
        @Size(min = 3, max = 500, message = "Descrição do espaço deve ter entre 3 e 500 caracteres!")
        String descricao,

        @Min(value = 1, message = "A capacidade máxima dever ser no mínimo 1!")
        Integer capacidadeMaxima,

        @NotBlank(message = "Localização é obrigatória!")
        @Size(min = 3, max = 255, message = "Localização do espaço deve ter entre 3 e 255 caracteres!")
        String localizacao,

        @NotBlank(message = "Tipo de computador é obrigatório!")
        String tipoComputadores,

        @NotNull(message = "O tipo de espaço não pode ser nulo!")
        TipoEspaco tipoEspaco,

        @NotNull(message = "Escola associada ao espaço não pode ser nula!")
        Long idEscola
) {
}
