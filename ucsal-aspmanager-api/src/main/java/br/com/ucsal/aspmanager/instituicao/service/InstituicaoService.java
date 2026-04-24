package br.com.ucsal.aspmanager.instituicao.service;

import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.instituicao.mapper.InstituicaoEnsinoMapper;
import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.instituicao.repository.InstituicaoEnsinoRepository;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InstituicaoService implements ServiceBase<Long,
        CreateInstituicaoEnsinoRequest, UpdateInstituicaoEnsinoRequest, InstituicaoEnsinoResponse> {

    private final InstituicaoEnsinoRepository instituicoes;
    private final InstituicaoEnsinoMapper instituicaoEnsinoMapper;

    public InstituicaoService(InstituicaoEnsinoRepository instituicoes, InstituicaoEnsinoMapper instituicaoEnsinoMapper) {
        this.instituicoes = instituicoes;
        this.instituicaoEnsinoMapper = instituicaoEnsinoMapper;
    }

    @Override
    @Transactional
    public InstituicaoEnsinoResponse criar(CreateInstituicaoEnsinoRequest createInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicaoEnsinoMapper.toEntity(createInstituicaoEnsinoRequest);
        return instituicaoEnsinoMapper.toResponse(instituicoes.save(instituicao));
    }

    @Override
    public Page<InstituicaoEnsinoResponse> buscarTodos(Pageable filtros) {
        return instituicoes.findAll(filtros).map(instituicaoEnsinoMapper::toResponse);
    }

    @Override
    public InstituicaoEnsinoResponse buscar(Long id) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        return instituicaoEnsinoMapper.toResponse(instituicao);
    }

    @Override
    @Transactional
    public InstituicaoEnsinoResponse atualizar(Long id, UpdateInstituicaoEnsinoRequest updateInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        instituicaoEnsinoMapper.updateEntity(updateInstituicaoEnsinoRequest, instituicao);

        return instituicaoEnsinoMapper.toResponse(instituicao);
    }

    @Override
    @Transactional
    public void deletar(Long id) {

        try {
            instituicoes.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Instituição de Ensino está associada a alguma Escola!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Instituição de Ensino não encontrada!");
        }
    }
}
