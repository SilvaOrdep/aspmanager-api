package br.com.ucsal.aspmanager.espaco.model;

import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "solicitacoes_espacos")
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoEspaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_uso")
    private LocalDate dataUso;
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    @Column(name = "hora_fim")
    private LocalTime horaFim;
    @Column(name = "status_solicitacao")
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao;
    @ManyToOne
    @JoinColumn(name = "id_espaco")
    private Espaco espaco;
    @ManyToOne
    @JoinColumn(name = "id_professor")
    private Professor professor;

}
