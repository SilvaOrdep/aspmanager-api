package br.com.ucsal.aspmanager.escola.repository;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
	boolean existsByEscola_Id(Long escolaId);
}
