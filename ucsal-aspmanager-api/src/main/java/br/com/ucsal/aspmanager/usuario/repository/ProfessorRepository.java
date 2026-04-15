package br.com.ucsal.aspmanager.usuario.repository;

import br.com.ucsal.aspmanager.usuario.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
