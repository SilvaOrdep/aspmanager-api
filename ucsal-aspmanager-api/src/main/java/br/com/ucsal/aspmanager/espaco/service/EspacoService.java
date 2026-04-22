package br.com.ucsal.aspmanager.espaco.service;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.dto.response.SolicitacaoResponse;
import br.com.ucsal.aspmanager.espaco.mapper.EspacoMapper;
import br.com.ucsal.aspmanager.espaco.mapper.SolicitacaoMapper;
import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.espaco.model.SolicitacaoEspaco;
import br.com.ucsal.aspmanager.espaco.repository.EspacoRepository;
import br.com.ucsal.aspmanager.espaco.repository.SolicitacaoEspacoRepository;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.repository.SoftwareRepository;
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
public class EspacoService implements ServiceBase<Long,
        CreateEspacoRequest, UpdateEspacoRequest, EspacoResponse> {

    private final EspacoRepository espacos;
    private final SolicitacaoEspacoRepository solicitacoes;
    private final EscolaRepository escolas;
    private final SoftwareRepository softwares;
    private final ProfessorRepository professores;
    private final SolicitacaoMapper solicitacaoMapper;
    private final EspacoMapper espacoMapper;


    public EspacoService(EspacoRepository espacos, SolicitacaoEspacoRepository solicitacoes, EscolaRepository escolas, SoftwareRepository softwares, ProfessorRepository professores, SolicitacaoMapper solicitacaoMapper, EspacoMapper espacoMapper) {
        this.espacos = espacos;
        this.solicitacoes = solicitacoes;
        this.escolas = escolas;
        this.softwares = softwares;
        this.professores = professores;
        this.solicitacaoMapper = solicitacaoMapper;
        this.espacoMapper = espacoMapper;
    }

    @Override
    @Transactional
    public EspacoResponse criar(CreateEspacoRequest request) {

        Escola escola = escolas.findById(request.idEscola())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        List<Long> idsSoftwares = request.softwares();
        List<Software> softwares = new ArrayList<>();

        if(!idsSoftwares.isEmpty()){

            for(Long idSoftware : idsSoftwares){

                Optional<Software> software = this.softwares.findById(idSoftware);
                software.ifPresent(softwares::add);

            }
        }

        Espaco espaco = espacoMapper.toEntity(request);
        espaco.setSoftwares(softwares);

        return espacoMapper.toResponse(espacos.save(espaco));
    }

    @Override
    public Page<EspacoResponse> buscarTodos(Pageable filtros) {
        return espacos.findAll(filtros).map(espacoMapper::toResponse);
    }

    @Override
    public EspacoResponse buscar(Long id) {
        Espaco espaco = espacos.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        return espacoMapper.toResponse(espaco);
    }

    @Override
    @Transactional
    public EspacoResponse atualizar(Long id, UpdateEspacoRequest request) {

        Espaco espaco = espacos.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        Escola escola = escolas.findById(request.idEscola())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        espacoMapper.updateEntity(request, espaco);

        return espacoMapper.toResponse(espaco);
    }

    @Override
    @Transactional
    public void deletar(Long id) {

        try{
            espacos.deleteById(id);

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Espaço não encontrado!");
        }catch(DataIntegrityViolationException e){
            Espaco espaco = espacos.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Espaço não encontrado!"));

            espaco.setStatusRegistro(StatusRegistro.INATIVO);
        }

    }

    @Transactional
    public SolicitacaoResponse criarSolicitacao(CreateSolicitacaoRequest request){

        Professor professor = professores.findById(request.idProfessor()).orElseThrow(() ->
                new EntityNotFoundException("Professor não encontrado!"));

        Espaco espaco = espacos.findById(request.idEspaco()).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        SolicitacaoEspaco solicitacaoEspaco = solicitacaoMapper.toEntity(request);

        solicitacaoEspaco.setStatusSolicitacao(StatusSolicitacao.PENDENTE);

        return solicitacaoMapper.toResponse(solicitacoes.save(solicitacaoEspaco));
    }

    public Page<SolicitacaoResponse> buscarSolicitacao(Pageable filtros){
        return solicitacoes.findAll(filtros).map(solicitacaoMapper::toResponse);
    }

    public SolicitacaoResponse buscarSolicitacao(Long id){
        SolicitacaoEspaco solicitacaoEspaco = solicitacoes.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Solicitação não encontrada!"));

        return solicitacaoMapper.toResponse(solicitacaoEspaco);
    }

    @Transactional
    public SolicitacaoResponse atualizarSolicitacao(Long id, UpdateSolicitacaoRequest request){

        SolicitacaoEspaco solicitacaoEspaco = solicitacoes.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Solicitação não encontrada!"));

        Professor professor = professores.findById(request.idProfessor()).orElseThrow(() ->
                new EntityNotFoundException("Professor não encontrado!"));

        Espaco espaco = espacos.findById(request.idEspaco()).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        solicitacaoEspaco.setStatusSolicitacao(request.statusSolicitacao());

        return solicitacaoMapper.toResponse(solicitacaoEspaco);
    }

    @Transactional
    public void deletarSolicitacao(Long id){

        try{
            solicitacoes.deleteById(id);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Espaço não encontrado!");
        }

    }

}
