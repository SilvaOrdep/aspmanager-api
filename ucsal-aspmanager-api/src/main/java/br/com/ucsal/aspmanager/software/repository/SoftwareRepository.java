package br.com.ucsal.aspmanager.software.repository;

import br.com.ucsal.aspmanager.software.model.Software;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
}
