package br.com.ucsal.aspmanager.espaco.repository;

import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EspacoRepository extends JpaRepository<Espaco, Long> {

    @Query("""
            SELECT e
            FROM Espaco e
            WHERE e.statusRegistro = :statusRegistro
              AND NOT EXISTS (
                SELECT s.id
                FROM SolicitacaoEspaco s
                WHERE s.espaco.id = e.id
                  AND s.statusSolicitacao = :statusSolicitacao
                  AND s.dataUso = :dataUso
                  AND s.horaInicio < :horaFim
                  AND s.horaFim > :horaInicio
              )
            """)
    Page<Espaco> buscarDisponiveisNoPeriodo(
            @Param("dataUso") LocalDate dataUso,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim,
            @Param("statusRegistro") StatusRegistro statusRegistro,
            @Param("statusSolicitacao") StatusSolicitacao statusSolicitacao,
            Pageable pageable
    );
}
