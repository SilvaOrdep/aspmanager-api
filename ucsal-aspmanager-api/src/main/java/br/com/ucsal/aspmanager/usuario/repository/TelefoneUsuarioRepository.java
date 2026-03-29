package br.com.ucsal.aspmanager.usuario.repository;

import br.com.ucsal.aspmanager.usuario.model.TelefoneUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneUsuarioRepository extends JpaRepository<TelefoneUsuario, Long> {
}
