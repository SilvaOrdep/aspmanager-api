package br.com.ucsal.aspmanager.espaco.model;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import br.com.ucsal.aspmanager.software.model.Software;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "espacos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Espaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String sigla;
    @Column(nullable = false)
    private String nome;
    private String descricao;
    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;
    @Column(nullable = false)
    private String localizacao;
    @Column(name = "status_registro", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;
    @Column(name = "tipo_computadores")
    private String tipoComputadores;
    @Enumerated(EnumType.STRING)
    private TipoEspaco tipoEspaco;
    @ManyToOne
    @JoinColumn(name = "id_escola")
    private Escola escola;
    @ManyToMany
    @JoinTable(
            name = "espaco_softwares",
            joinColumns = @JoinColumn(name = "id_espaco"),
            inverseJoinColumns = @JoinColumn(name = "id_software")
    )
    private List<Software> softwares;

}
