package br.com.ucsal.aspmanager.instituicao.repository;

import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituicaoEnsinoRepository extends JpaRepository<InstituicaoEnsino, Long> {
}
