package br.com.ucsal.aspmanager.software.model;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
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

    @Column(name = "nome_software", nullable = false)
    private String nome;

    @Column(name = "versao_software", nullable = false)
    private String versao;

    @Column(name = "url_download", nullable = false)
    private String urlDownload;

    @Column(name = "tipo_licenca", nullable = false)
    private String tipoLicenca;

    @Column(name = "objetivo_uso", nullable = false)
    private String objetivoUso;

    @ManyToMany
    @JoinTable(
            name = "solicitacoes_softwares_disciplinas",
            joinColumns = @JoinColumn(name = "id_solicitacao_software"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina")
    )
    private List<Disciplina> disciplinasSolicitadas;

    @ManyToOne
    @JoinColumn(name = "id_software_aprovado")
    private Software softwareCriado;

    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false)
    private Professor professor;

}
