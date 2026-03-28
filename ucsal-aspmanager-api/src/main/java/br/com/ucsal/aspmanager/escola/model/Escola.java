package br.com.ucsal.aspmanager.escola.model;


import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
