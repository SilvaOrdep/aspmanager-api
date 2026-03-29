package br.com.ucsal.aspmanager.software.repository;

import br.com.ucsal.aspmanager.software.model.SolicitacaoSoftware;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoSoftwareRepository extends JpaRepository<SolicitacaoSoftware, Long> {
}
