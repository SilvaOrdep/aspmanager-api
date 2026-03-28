package br.com.ucsal.aspmanager.instituicao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "instituicoes_ensino")
@AllArgsConstructor
@NoArgsConstructor
public class InstituicaoEnsino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    // telefone, listas de escolas relacao bidirecional
}
