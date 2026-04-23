package br.com.ucsal.aspmanager.software.repository;

import br.com.ucsal.aspmanager.software.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
	Optional<Software> findByNomeIgnoreCaseAndVersaoIgnoreCase(String nome, String versao);

	@Query(value = "select count(*) > 0 from espaco_softwares where id_software = :softwareId", nativeQuery = true)
	boolean existsVinculoEmEspacos(@Param("softwareId") Long softwareId);
}
