package br.com.ucsal.aspmanager.shared.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErroApiResponse (

        LocalDateTime timestamp,

        Integer codigo,

        String status,

        List<String> erros

) {
}