package br.com.ucsal.aspmanager.software.service;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.repository.DisciplinaRepository;
import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoSolicitacaoSoftware;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.CreateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.dto.response.SolicitacaoSoftwareResponse;
import br.com.ucsal.aspmanager.software.mapper.SoftwareMapper;
import br.com.ucsal.aspmanager.software.mapper.SolicitacaoSoftwareMapper;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.model.SolicitacaoSoftware;
import br.com.ucsal.aspmanager.software.repository.SoftwareRepository;
import br.com.ucsal.aspmanager.software.repository.SolicitacaoSoftwareRepository;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SoftwareService implements ServiceBase<Long,
        CreateSoftwareRequest, UpdateSoftwareRequest, SoftwareResponse> {

    private final SoftwareRepository softwares;
    private final SolicitacaoSoftwareRepository solicitacoesSoftware;
    private final ProfessorRepository professores;
    private final DisciplinaRepository disciplinas;
    private final SoftwareMapper softwareMapper;
    private final SolicitacaoSoftwareMapper solicitacaoSoftwareMapper;

    public SoftwareService(SoftwareRepository softwares,
                           SolicitacaoSoftwareRepository solicitacoesSoftware,
                           ProfessorRepository professores,
                           DisciplinaRepository disciplinas,
                           SoftwareMapper softwareMapper,
                           SolicitacaoSoftwareMapper solicitacaoSoftwareMapper) {
        this.softwares = softwares;
        this.solicitacoesSoftware = solicitacoesSoftware;
        this.professores = professores;
        this.disciplinas = disciplinas;
        this.softwareMapper = softwareMapper;
        this.solicitacaoSoftwareMapper = solicitacaoSoftwareMapper;
    }

    @Override
    @Transactional
    public SoftwareResponse criar(CreateSoftwareRequest createSoftwareRequest) {

        Software software = softwareMapper.toEntity(createSoftwareRequest);
        software.setDataCadastro(LocalDate.now());

        return softwareMapper.toResponse(softwares.save(software));
    }

    @Override
    public Page<SoftwareResponse> buscarTodos(Pageable filtros) {
        return softwares.findAll(filtros).map(softwareMapper::toResponse);
    }

    @Override
    public SoftwareResponse buscar(Long id) {
        Software software = softwares.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Software não encontrado!"));

        return softwareMapper.toResponse(software);
    }

    @Override
    @Transactional
    public SoftwareResponse atualizar(Long id, UpdateSoftwareRequest updateSoftwareRequest) {
        Software software = softwares.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Software não encontrado!"));

        softwareMapper.updateEntity(updateSoftwareRequest, software);

        return softwareMapper.toResponse(software);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Software software = softwares.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Software não encontrado!"));

        boolean possuiHistorico = softwares.existsVinculoEmEspacos(id)
                || solicitacoesSoftware.existsBySoftwareCriado_Id(id);

        if (possuiHistorico) {
            software.setStatusRegistro(StatusRegistro.INATIVO);
            return;
        }

        softwares.delete(software);
    }

    @Transactional
    public SolicitacaoSoftwareResponse criarSolicitacao(CreateSolicitacaoSoftwareRequest request) {
        Professor professor = professores.findById(request.idProfessor()).orElseThrow(() ->
                new EntityNotFoundException("Professor não encontrado!"));

        SolicitacaoSoftware solicitacao = solicitacaoSoftwareMapper.toEntity(request);
        solicitacao.setProfessor(professor);
        solicitacao.setDataSolicitacao(LocalDate.now());
        solicitacao.setTipoSolicitacaoSoftware(TipoSolicitacaoSoftware.ATIVACAO);
        solicitacao.setStatusSolicitacao(StatusSolicitacao.PENDENTE);
        solicitacao.setDisciplinasSolicitadas(carregarDisciplinas(request.software().idDisciplinas()));

        return solicitacaoSoftwareMapper.toResponse(solicitacoesSoftware.save(solicitacao));
    }

    public Page<SolicitacaoSoftwareResponse> buscarSolicitacoes(Pageable filtros) {
        return solicitacoesSoftware.findAll(filtros).map(solicitacaoSoftwareMapper::toResponse);
    }

    public Page<SolicitacaoSoftwareResponse> buscarMinhasSolicitacoes(Pageable filtros) {
        Professor professorAutenticado = buscarProfessorAutenticado();
        return solicitacoesSoftware.findByProfessor_Id(professorAutenticado.getId(), filtros)
                .map(solicitacaoSoftwareMapper::toResponse);
    }

    public SolicitacaoSoftwareResponse buscarSolicitacao(Long id) {
        SolicitacaoSoftware solicitacao = buscarSolicitacaoPorId(id);
        return solicitacaoSoftwareMapper.toResponse(solicitacao);
    }

    @Transactional
    public SolicitacaoSoftwareResponse atualizarSolicitacao(Long id, UpdateSolicitacaoSoftwareRequest request) {
        SolicitacaoSoftware solicitacao = buscarSolicitacaoPorId(id);

        if (solicitacao.getStatusSolicitacao() != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("A solicitação já foi analisada.");
        }

        if (request.statusSolicitacao() == StatusSolicitacao.PENDENTE) {
            throw new IllegalArgumentException("Não é permitido definir o status como PENDENTE na análise.");
        }

        solicitacaoSoftwareMapper.updateEntity(request, solicitacao);

        if (request.statusSolicitacao() == StatusSolicitacao.APROVADO) {
            Software softwareCriado = softwares.findByNomeIgnoreCaseAndVersaoIgnoreCase(
                    solicitacao.getNome(), solicitacao.getVersao()
            ).orElseGet(() -> criarSoftwareAPartirDaSolicitacao(solicitacao));

            solicitacao.setSoftwareCriado(softwareCriado);
        }

        return solicitacaoSoftwareMapper.toResponse(solicitacao);
    }

    @Transactional
    public void deletarSolicitacao(Long id) {
        if (!solicitacoesSoftware.existsById(id)) {
            throw new EntityNotFoundException("Solicitação de software não encontrada!");
        }

        solicitacoesSoftware.deleteById(id);
    }

    private SolicitacaoSoftware buscarSolicitacaoPorId(Long id) {
        return solicitacoesSoftware.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Solicitação de software não encontrada!"));
    }

    private Software criarSoftwareAPartirDaSolicitacao(SolicitacaoSoftware solicitacao) {
        List<Disciplina> disciplinasDaSolicitacao = solicitacao.getDisciplinasSolicitadas() == null
            ? Collections.emptyList()
            : new ArrayList<>(solicitacao.getDisciplinasSolicitadas());

        Software software = Software.builder()
                .nome(solicitacao.getNome())
                .versao(solicitacao.getVersao())
                .urlDownload(solicitacao.getUrlDownload())
                .tipoLicenca(solicitacao.getTipoLicenca())
                .objetivoUso(solicitacao.getObjetivoUso())
            .disciplinas(disciplinasDaSolicitacao)
                .build();

        software.setDataCadastro(LocalDate.now());
        return softwares.save(software);
    }

    private List<Disciplina> carregarDisciplinas(List<Long> idsDisciplinas) {
        if (idsDisciplinas == null || idsDisciplinas.isEmpty()) {
            return Collections.emptyList();
        }

        List<Disciplina> disciplinasEncontradas = new ArrayList<>(disciplinas.findAllById(idsDisciplinas));
        if (disciplinasEncontradas.size() != idsDisciplinas.size()) {
            throw new EntityNotFoundException("Uma ou mais disciplinas informadas não foram encontradas!");
        }

        return disciplinasEncontradas;
    }

    private Professor buscarProfessorAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UsuarioResponse usuario)) {
            throw new IllegalStateException("Usuário autenticado inválido.");
        }

        if (usuario.perfil() != Perfil.PROFESSOR) {
            throw new IllegalStateException("Somente professores possuem solicitações próprias de software.");
        }

        return professores.findByUsuario_Id(usuario.id()).orElseThrow(() ->
                new EntityNotFoundException("Professor autenticado não encontrado!"));
    }
}
