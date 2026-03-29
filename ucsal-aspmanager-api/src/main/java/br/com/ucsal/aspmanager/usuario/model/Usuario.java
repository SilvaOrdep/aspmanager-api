package br.com.ucsal.aspmanager.usuario.model;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_completo")
    private String nomeCompleto;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private Perfil perfil;
    @Column(name = "status_registro")
    @Enumerated(EnumType.STRING)
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<TelefoneUsuario> telefones;

}
