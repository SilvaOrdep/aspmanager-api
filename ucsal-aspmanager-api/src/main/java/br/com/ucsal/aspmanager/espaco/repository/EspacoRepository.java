package br.com.ucsal.aspmanager.espaco.repository;

import br.com.ucsal.aspmanager.espaco.model.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspacoRepository extends JpaRepository<Espaco, Long> {
}
