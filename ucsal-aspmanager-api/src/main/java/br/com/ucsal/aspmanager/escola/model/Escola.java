package br.com.ucsal.aspmanager.escola.model;

import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.professor.Professor;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "escolas")
@AllArgsConstructor
@NoArgsConstructor
public class Escola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(name = "status_registro")
    private StatusRegistro statusRegistro;
    @ManyToOne
    @JoinColumn(name = "id_instituicao")
    private InstituicaoEnsino instituicao;
    @OneToOne
    @JoinColumn(name = "id_professor_coordenador")
    private Professor coordenador;
    @OneToMany(mappedBy = "escola")
    private List<Disciplina> disciplinas;

}
