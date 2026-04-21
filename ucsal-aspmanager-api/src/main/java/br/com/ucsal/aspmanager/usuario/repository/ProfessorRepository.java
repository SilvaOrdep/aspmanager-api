package br.com.ucsal.aspmanager.usuario.repository;

import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
	Optional<Professor> findByUsuario_Id(Long usuarioId);
    Page<Professor> findByUsuario_StatusRegistro(StatusRegistro statusRegistro, Pageable pageable);

    void deleteByUsuario(Usuario usuario);

    Optional<Professor> findByMatricula(String matricula);
}
