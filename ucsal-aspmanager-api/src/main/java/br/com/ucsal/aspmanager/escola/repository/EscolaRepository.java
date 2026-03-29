package br.com.ucsal.aspmanager.escola.repository;

import br.com.ucsal.aspmanager.escola.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EscolaRepository extends JpaRepository<Escola, Long> {
}
