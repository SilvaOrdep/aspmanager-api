package br.com.ucsal.aspmanager.usuario.dto.response;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta de usuário")
public record UsuarioResponse(
        @Schema(description = "ID do usuário", example = "12")
        Long id,

        @Schema(description = "Nome completo", example = "Ana Paula Souza")
        String nomeCompleto,

        @Schema(description = "Email", example = "ana.souza@ucsal.br")
        String email,

        @Schema(description = "Perfil de acesso", example = "PROFESSOR")
        Perfil perfil,

        @Schema(description = "Status do registro", example = "ATIVO")
        StatusRegistro statusRegistro,

        @Schema(description = "Matrícula do professor quando aplicável", example = "202600123")
        String matricula,

        @Schema(description = "Telefones vinculados", example = "[\"71999998888\", \"7132017000\"]")
        List<String> telefones) {
}
