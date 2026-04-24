package br.com.ucsal.aspmanager.escola.service;

import br.com.ucsal.aspmanager.escola.dto.request.CreateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.DisciplinaResponse;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.mapper.EscolaMapper;
import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.escola.repository.DisciplinaRepository;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;

import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.instituicao.repository.InstituicaoEnsinoRepository;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EscolaService implements ServiceBase<Long,
        CreateEscolaRequest, UpdateEscolaRequest, EscolaResponse> {

    private final EscolaRepository escolas;
    private final DisciplinaRepository disciplinas;
    private final InstituicaoEnsinoRepository instituicoes;
    private final ProfessorRepository professores;
    private final EscolaMapper escolaMapper;

    public EscolaService(EscolaRepository escolas, DisciplinaRepository disciplinas,
                         InstituicaoEnsinoRepository instituicoes, ProfessorRepository professores, EscolaMapper escolaMapper) {
        this.escolas = escolas;
        this.disciplinas = disciplinas;
        this.instituicoes = instituicoes;
        this.professores = professores;
        this.escolaMapper = escolaMapper;
    }

    @Override
    @Transactional
    public EscolaResponse criar(CreateEscolaRequest createEscolaRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(createEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        if (createEscolaRequest.idCoordenador() != null) {
            Professor professor = professores.findById(createEscolaRequest.idCoordenador()).
                    orElseThrow(() -> new EntityNotFoundException(("Professor não encontrado!")));
        }

        List<Long> idsDisciplinas = createEscolaRequest.idsDisciplinas();
        List<Disciplina> disciplinas = new ArrayList<>();

        if (!idsDisciplinas.isEmpty()) {

            for (Long idDisciplina : idsDisciplinas) {

                Optional<Disciplina> disciplina = this.disciplinas.findById(idDisciplina);
                disciplina.ifPresent(disciplinas::add);

            }
        }

        Escola escola = escolaMapper.toEntity(createEscolaRequest);
        escola.setDisciplinas(disciplinas);

        return escolaMapper.toResponse(escolas.save(escola));
    }

    @Override
    public Page<EscolaResponse> buscarTodos(Pageable filtros) {

        return escolas.findAll(filtros).map(escolaMapper::toResponse);
    }

    @Override
    public EscolaResponse buscar(Long id) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        return escolaMapper.toResponse(escola);
    }

    @Override
    @Transactional
    public EscolaResponse atualizar(Long id, UpdateEscolaRequest updateEscolaRequest) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        InstituicaoEnsino instituicao = instituicoes.findById(updateEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        if (updateEscolaRequest.idCoordenador() != null ) {
            Professor professor = professores.findById(updateEscolaRequest.idCoordenador()).
                    orElseThrow(() -> new EntityNotFoundException(("Professor não encontrado!")));

        }

        escolaMapper.updateEntity(updateEscolaRequest, escola);

        return escolaMapper.toResponse(escola);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        boolean possuiHistorico = professores.existsByEscola_Id(id) || disciplinas.existsByEscola_Id(id);
        if (possuiHistorico) {
            escola.setStatusRegistro(StatusRegistro.INATIVO);
            return;
        }

        escolas.delete(escola);
    }

    @Transactional
    public DisciplinaResponse criarDisciplina(CreateDisciplinaRequest createDisciplinaRequest) {

        Escola escola = escolas.findById(createDisciplinaRequest.idEscola()).
                orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        Disciplina disciplina = Disciplina.builder().
                nome(createDisciplinaRequest.nome()).
                descricao(createDisciplinaRequest.descricao()).
                escola(escola).
                build();

        disciplinas.save(disciplina);

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    public Page<DisciplinaResponse> buscarDisciplina(Pageable filtros) {
        return disciplinas.findAll(filtros).map(disciplina -> new DisciplinaResponse(disciplina.getId(),
                disciplina.getNome(), disciplina.getDescricao(), disciplina.getEscola().getId()));
    }

    public DisciplinaResponse buscarDisciplina(Long id) {

        Disciplina disciplina = disciplinas.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada!"));

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    @Transactional
    public DisciplinaResponse atualizarDisciplina(Long id, UpdateDisciplinaRequest updateDisciplinaRequest) {

        Disciplina disciplina = disciplinas.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada!"));

        Escola escola = escolas.findById(updateDisciplinaRequest.idEscola()).
                orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        disciplina.setNome(updateDisciplinaRequest.nome());
        disciplina.setDescricao(updateDisciplinaRequest.descricao());
        disciplina.setEscola(escola);

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    @Transactional
    public void deletarDisciplina(Long id) {
        try {
            disciplinas.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Instituição de Ensino está associada a alguma Escola!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Disciplina não encontrada!");
        }

    }
}
