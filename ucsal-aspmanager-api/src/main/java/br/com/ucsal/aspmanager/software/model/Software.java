package br.com.ucsal.aspmanager.software.model;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "softwares")
@AllArgsConstructor
@NoArgsConstructor
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String versao;
    @Column(name = "url_download")
    private String urlDownload;
    @Column(name = "tipo_licenca")
    private String tipoLicenca;
    @Column(name = "objetivo_uso")
    private String objetivoUso;
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
    @Column(name = "status_registro")
    private StatusRegistro statusRegistro;

}
