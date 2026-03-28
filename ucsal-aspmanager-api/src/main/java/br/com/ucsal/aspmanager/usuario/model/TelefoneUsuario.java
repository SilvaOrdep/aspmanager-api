package br.com.ucsal.aspmanager.usuario.model;

import br.com.ucsal.aspmanager.shared.model.Telefone;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "telefones_usuarios")
public class TelefoneUsuario extends Telefone {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
