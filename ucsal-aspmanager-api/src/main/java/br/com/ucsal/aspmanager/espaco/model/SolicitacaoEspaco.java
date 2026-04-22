package br.com.ucsal.aspmanager.espaco.model;

import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "solicitacoes_espacos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitacaoEspaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String descricao;
    @Column(name = "data_uso", nullable = false)
    private LocalDate dataUso;
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    @Column(name = "status_solicitacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao;
    @ManyToOne
    @JoinColumn(name = "id_espaco", nullable = false)
    private Espaco espaco;
    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

}
