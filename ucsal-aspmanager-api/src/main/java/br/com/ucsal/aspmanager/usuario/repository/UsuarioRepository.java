package br.com.ucsal.aspmanager.usuario.repository;

import br.com.ucsal.aspmanager.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
