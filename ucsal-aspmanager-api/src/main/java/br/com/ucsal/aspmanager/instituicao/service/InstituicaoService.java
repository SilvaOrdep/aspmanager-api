package br.com.ucsal.aspmanager.instituicao.service;

import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.instituicao.mapper.InstituicaoEnsinoMapper;
import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.instituicao.model.TelefoneInstituicao;
import br.com.ucsal.aspmanager.instituicao.repository.InstituicaoEnsinoRepository;
import br.com.ucsal.aspmanager.instituicao.repository.TelefoneInstituicaoRepository;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InstituicaoService implements ServiceBase<Long,
        CreateInstituicaoEnsinoRequest, UpdateInstituicaoEnsinoRequest, InstituicaoEnsinoResponse> {

    private final InstituicaoEnsinoRepository instituicoes;
    private final TelefoneInstituicaoRepository telefonesInstituicao;
    private final InstituicaoEnsinoMapper instituicaoEnsinoMapper;

    public InstituicaoService(InstituicaoEnsinoRepository instituicoes, TelefoneInstituicaoRepository telefonesInstituicao, InstituicaoEnsinoMapper instituicaoEnsinoMapper) {
        this.instituicoes = instituicoes;
        this.telefonesInstituicao = telefonesInstituicao;
        this.instituicaoEnsinoMapper = instituicaoEnsinoMapper;
    }

    @Override
    @Transactional
    public InstituicaoEnsinoResponse criar(CreateInstituicaoEnsinoRequest createInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicaoEnsinoMapper.toEntity(createInstituicaoEnsinoRequest);

        if (createInstituicaoEnsinoRequest.telefones() != null && !createInstituicaoEnsinoRequest.telefones().isEmpty()) {
            for (String telefone : createInstituicaoEnsinoRequest.telefones()) {
                criarTelefoneParaIES(telefone, instituicao);
            }
        }

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

        instituicao.setNome(updateInstituicaoEnsinoRequest.nome());
        instituicao.setEndereco(updateInstituicaoEnsinoRequest.endereco());

        if (updateInstituicaoEnsinoRequest.telefones() != null
                && !updateInstituicaoEnsinoRequest.telefones().isEmpty()) {

            List telefonesIES = instituicao.getTelefones();
            telefonesIES.addAll(updateInstituicaoEnsinoRequest.telefones());

            instituicao.setTelefones(telefonesIES);

        }
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

    @Transactional
    protected void criarTelefoneParaIES(String telefone, InstituicaoEnsino instituicaoEnsino) {
        String telefoneLimpo = telefone.replaceAll("[()\\s-]", "");
        if (telefoneLimpo.matches("\\d{10,11}")) {
            TelefoneInstituicao telefoneInstituicao = new TelefoneInstituicao();
            telefoneInstituicao.setNumero(telefoneLimpo);
            telefoneInstituicao.setInstituicao(instituicaoEnsino);

            telefonesInstituicao.save(telefoneInstituicao);

            instituicaoEnsino.getTelefones().add(telefoneInstituicao);
        }
    }
}
