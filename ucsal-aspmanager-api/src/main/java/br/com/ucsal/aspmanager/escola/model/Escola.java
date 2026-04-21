package br.com.ucsal.aspmanager.escola.model;

import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "escolas")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Escola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(name = "status_registro", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusRegistro statusRegistro;
    @ManyToOne
    @JoinColumn(name = "id_instituicao", nullable = false)
    private InstituicaoEnsino instituicao;
    @OneToOne
    @JoinColumn(name = "id_professor_coordenador")
    private Professor coordenador;
    @OneToMany(mappedBy = "escola")
    private List<Disciplina> disciplinas;

}
