package br.com.ucsal.aspmanager.escola.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "disciplinas")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "id_escola", nullable = false)
    private Escola escola;

}
