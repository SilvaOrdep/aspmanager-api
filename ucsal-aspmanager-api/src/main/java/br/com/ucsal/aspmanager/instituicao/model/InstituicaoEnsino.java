package br.com.ucsal.aspmanager.instituicao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "instituicoes_ensino")
@AllArgsConstructor
@NoArgsConstructor
public class InstituicaoEnsino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private String endereco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instituicao")
    private List<TelefoneInstituicao> telefones;

}
