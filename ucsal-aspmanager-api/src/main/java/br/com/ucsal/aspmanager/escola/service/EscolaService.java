package br.com.ucsal.aspmanager.escola.service;

import br.com.ucsal.aspmanager.escola.dto.request.CreateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.DisciplinaResponse;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EscolaService implements ServiceBase<Long,
        CreateEscolaRequest, UpdateEscolaRequest, EscolaResponse> {

    private final EscolaRepository escolas;
    private final DisciplinaRepository disciplinas;
    private InstituicaoEnsinoRepository instituicoes;
    private ProfessorRepository professores;

    public EscolaService(EscolaRepository escolas, DisciplinaRepository disciplinas,
                         InstituicaoEnsinoRepository instituicoes, ProfessorRepository professores){
        this.escolas = escolas;
        this.disciplinas = disciplinas;
        this.instituicoes = instituicoes;
        this.professores = professores;
    }

    @Override
    public EscolaResponse criar(CreateEscolaRequest createEscolaRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(createEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        Professor professor = professores.findById(createEscolaRequest.idCoordenador()).
                orElseThrow(() -> new EntityNotFoundException(("Professor não encontrado!")));

        List<Long> idsDisciplinas = createEscolaRequest.idsDisciplinas();
        List<Disciplina> disciplinas = null;

        if(!idsDisciplinas.isEmpty()){

            for(Long idDisciplina : idsDisciplinas){

                Optional <Disciplina> disciplina = this.disciplinas.findById(idDisciplina);
                disciplina.ifPresent(value -> disciplinas.add(value));

            }
        }

        Escola escola = Escola.builder().
                nome(createEscolaRequest.nome()).
                instituicao(instituicao).
                statusRegistro(StatusRegistro.ATIVO).
                coordenador(professor).
                disciplinas(disciplinas).
                build();

        escolas.save(escola);

        return new EscolaResponse(escola.getId(), escola.getNome(), escola.getStatusRegistro(),
                escola.getInstituicao().getId(), escola.getCoordenador().getId(),
                escola.getDisciplinas().stream().map(Disciplina::getId).toList());
    }

    @Override
    public Page<EscolaResponse> buscarTodos(Pageable filtros) {

        return escolas.findAll(filtros).map(escola ->
                new EscolaResponse(escola.getId(), escola.getNome(), escola.getStatusRegistro(),
                        escola.getInstituicao().getId(), escola.getCoordenador().getId(),
                        escola.getDisciplinas().stream().map(Disciplina::getId).toList()));
    }

    @Override
    public EscolaResponse buscar(Long id) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        return new EscolaResponse(escola.getId(), escola.getNome(), escola.getStatusRegistro(),
                escola.getInstituicao().getId(), escola.getCoordenador().getId(),
                escola.getDisciplinas().stream().map(Disciplina::getId).toList());
    }

    @Override
    public EscolaResponse atualizar(Long id, UpdateEscolaRequest updateEscolaRequest) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        InstituicaoEnsino instituicao = instituicoes.findById(updateEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        Professor professor = professores.findById(updateEscolaRequest.idCoordenador()).
                orElseThrow(() -> new EntityNotFoundException(("Professor não encontrado!")));

        escola.setNome(updateEscolaRequest.nome());
        escola.setInstituicao(instituicao);
        escola.setCoordenador(professor);

        return new EscolaResponse(escola.getId(), escola.getNome(), escola.getStatusRegistro(),
                escola.getInstituicao().getId(), escola.getCoordenador().getId(),
                escola.getDisciplinas().stream().map(Disciplina::getId).toList());
    }

    @Override
    public void deletar(Long id) {

        try{
            escolas.deleteById(id);

        }catch(DataIntegrityViolationException e){

            Escola escola = escolas.findById(id).orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));
            escola.setStatusRegistro(StatusRegistro.INATIVO);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Escola não encontrada!");
        }
    }

    public DisciplinaResponse criarDisciplina(CreateDisciplinaRequest createDisciplinaRequest){

        return null;
    }

    public Page<DisciplinaResponse> buscarDisciplina(Pageable filtros){
        return null;
    }

    public DisciplinaResponse buscarDisciplina(Long id){
        return null;
    }

    public DisciplinaResponse atualizarDisciplina(Long id, UpdateDisciplinaRequest updateDisciplinaRequest){
        return null;
    }

    public void deletarDisciplina (Long id){

    }
}
