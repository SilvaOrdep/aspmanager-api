package br.com.ucsal.aspmanager.software.model;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
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
@Table(name = "softwares")
@AllArgsConstructor
@NoArgsConstructor
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private String versao;
    @Column(name = "url_download")
    private String urlDownload;
    @Column(name = "tipo_licenca")
    private String tipoLicenca;
    @Column(name = "objetivo_uso", nullable = false)
    private String objetivoUso;
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    @Column(name = "status_registro")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;
    @ManyToMany
    @JoinTable(
            name = "softwares_disciplinas",
            joinColumns = @JoinColumn(name = "id_software"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina")
    )
    private List<Disciplina> disciplinas;

}
