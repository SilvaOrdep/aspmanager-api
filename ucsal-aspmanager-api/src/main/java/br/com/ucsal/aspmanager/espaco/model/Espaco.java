package br.com.ucsal.aspmanager.espaco.model;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "espacos")
@AllArgsConstructor
@NoArgsConstructor
public class Espaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sigla;
    private String nome;
    private String descricao;
    @Column(name = "capacidade_maxima")
    private Integer capacidadeMaxima;
    private String localizacao;
    @Column(name = "status_registro")
    @Enumerated(EnumType.STRING)
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;
    @Column(name = "tipo_computadores")
    private String tipoComputadores;
    @Enumerated(EnumType.STRING)
    private TipoEspaco tipoEspaco;
}
