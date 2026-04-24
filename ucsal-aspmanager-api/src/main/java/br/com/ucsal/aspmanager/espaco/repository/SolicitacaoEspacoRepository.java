package br.com.ucsal.aspmanager.espaco.repository;

import br.com.ucsal.aspmanager.espaco.model.SolicitacaoEspaco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoEspacoRepository extends JpaRepository<SolicitacaoEspaco, Long> {
    boolean existsByEspaco_Id(Long espacoId);

    Page<SolicitacaoEspaco> findByProfessor_Id(Long professorId, Pageable pageable);
}
