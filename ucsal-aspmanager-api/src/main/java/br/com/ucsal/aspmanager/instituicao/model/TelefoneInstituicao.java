package br.com.ucsal.aspmanager.instituicao.model;

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
@Table(name = "telefones_instituicoes")
public class TelefoneInstituicao extends Telefone {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instituicao", nullable = false)
    private InstituicaoEnsino instituicao;

}