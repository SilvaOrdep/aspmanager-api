package br.com.ucsal.aspmanager.software.repository;

import br.com.ucsal.aspmanager.software.model.SolicitacaoSoftware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoSoftwareRepository extends JpaRepository<SolicitacaoSoftware, Long> {
	Page<SolicitacaoSoftware> findByProfessor_Id(Long idProfessor, Pageable pageable);
}
