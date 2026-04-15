package br.com.ucsal.aspmanager.software.model;

import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "solicitacoes_softwares")
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoSoftware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;
    @Column(name = "tipo_soliticacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoSolicitacaoSoftware tipoSolicitacaoSoftware;
    @Column(name = "status_solicitado", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusSolicitacao;
    @ManyToOne
    @JoinColumn(name = "id_software", nullable = false)
    private Software software;
    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

}
