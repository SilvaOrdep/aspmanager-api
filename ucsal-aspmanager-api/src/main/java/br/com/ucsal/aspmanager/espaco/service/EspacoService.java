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

import java.util.List;
import java.util.Optional;

@Service
public class EspacoService implements ServiceBase<Long,
        CreateEspacoRequest, UpdateEspacoRequest, EspacoResponse> {

    private final EspacoRepository espacos;
    private final SolicitacaoEspacoRepository solicitacoes;
    private final EscolaRepository escolas;
    private final SoftwareRepository softwares;
    private final ProfessorRepository professores;
    private final SolicitacaoMapper solicitacaoMapper;

    public EspacoService(EspacoRepository espacos, SolicitacaoEspacoRepository solicitacoes, EscolaRepository escolas, SoftwareRepository softwares, ProfessorRepository professores, SolicitacaoMapper solicitacaoMapper) {
        this.espacos = espacos;
        this.solicitacoes = solicitacoes;
        this.escolas = escolas;
        this.softwares = softwares;
        this.professores = professores;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    @Override
    public EspacoResponse criar(CreateEspacoRequest request) {

        Escola escola = escolas.findById(request.idEscola())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        List<Long> idsSoftwares = request.softwares();
        List<Software> softwares = null;

        if(!idsSoftwares.isEmpty()){

            for(Long idSoftware : idsSoftwares){

                Optional<Software> software = this.softwares.findById(idSoftware);
                software.ifPresent(value -> softwares.add(value));

            }
        }

        Espaco espaco = Espaco.builder().
                sigla(request.sigla()).
                nome(request.nome()).
                descricao(request.descricao()).
                capacidadeMaxima(request.capacidadeMaxima()).
                tipoEspaco(request.tipoEspaco()).
                localizacao(request.localizacao()).
                softwares(softwares).
                tipoComputadores(request.tipoComputadores()).
                escola(escola).
                build();

        espacos.save(espaco);

        return new EspacoResponse(espaco.getId(), espaco.getSigla(),
                espaco.getNome(), espaco.getDescricao(), espaco.getCapacidadeMaxima(),
                espaco.getLocalizacao(), espaco.getTipoComputadores(), espaco.getTipoEspaco(),
                espaco.getEscola(), espaco.getSoftwares().stream().
                map(Software::getId).toList(), espaco.getStatusRegistro());
    }

    @Override
    public Page<EspacoResponse> buscarTodos(Pageable filtros) {
        return espacos.findAll(filtros).map(espaco ->
                new EspacoResponse(espaco.getId(), espaco.getSigla(),
                        espaco.getNome(), espaco.getDescricao(), espaco.getCapacidadeMaxima(),
                        espaco.getLocalizacao(), espaco.getTipoComputadores(), espaco.getTipoEspaco(),
                        espaco.getEscola(), espaco.getSoftwares().stream().
                        map(Software::getId).toList(), espaco.getStatusRegistro()));
    }

    @Override
    public EspacoResponse buscar(Long id) {
        Espaco espaco = espacos.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        return new EspacoResponse(espaco.getId(), espaco.getSigla(),
                espaco.getNome(), espaco.getDescricao(), espaco.getCapacidadeMaxima(),
                espaco.getLocalizacao(), espaco.getTipoComputadores(), espaco.getTipoEspaco(),
                espaco.getEscola(), espaco.getSoftwares().stream().
                map(Software::getId).toList(), espaco.getStatusRegistro());
    }

    @Override
    public EspacoResponse atualizar(Long id, UpdateEspacoRequest request) {

        Espaco espaco = espacos.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Espaço não encontrado!"));

        Escola escola = escolas.findById(request.idEscola())
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        espaco.setSigla(request.sigla());
        espaco.setNome(request.nome());
        espaco.setDescricao(request.descricao());
        espaco.setLocalizacao(request.localizacao());
        espaco.setTipoComputadores(request.tipoComputadores());
        espaco.setTipoEspaco(request.tipoEspaco());
        espaco.setEscola(escola);
        espaco.setCapacidadeMaxima(request.capacidadeMaxima());

        return new EspacoResponse(espaco.getId(), espaco.getSigla(),
                espaco.getNome(), espaco.getDescricao(), espaco.getCapacidadeMaxima(),
                espaco.getLocalizacao(), espaco.getTipoComputadores(), espaco.getTipoEspaco(),
                espaco.getEscola(), espaco.getSoftwares().stream().
                map(Software::getId).toList(), espaco.getStatusRegistro());
    }

    @Override
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


    public void deletarSolicitacao(Long id){

        try{
            solicitacoes.deleteById(id);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Espaço não encontrado!");
        }

    }

}
