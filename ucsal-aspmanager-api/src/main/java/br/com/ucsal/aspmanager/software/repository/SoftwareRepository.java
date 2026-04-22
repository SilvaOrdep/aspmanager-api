package br.com.ucsal.aspmanager.software.repository;

import br.com.ucsal.aspmanager.software.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
	Optional<Software> findByNomeIgnoreCaseAndVersaoIgnoreCase(String nome, String versao);
}
