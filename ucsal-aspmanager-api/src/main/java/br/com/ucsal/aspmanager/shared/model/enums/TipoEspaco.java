package br.com.ucsal.aspmanager.shared.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de espaço acadêmico permitido")
public enum TipoEspaco {
    SALA,
    AUDITORIO,
    LABORATORIO
}
