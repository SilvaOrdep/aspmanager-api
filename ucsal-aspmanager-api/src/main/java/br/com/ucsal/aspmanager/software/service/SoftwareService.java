package br.com.ucsal.aspmanager.software.service;

import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.mapper.SoftwareMapper;
import br.com.ucsal.aspmanager.software.mapper.SolicitacaoSoftwareMapper;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.repository.SoftwareRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SoftwareService implements ServiceBase<Long,
        CreateSoftwareRequest, UpdateSoftwareRequest, SoftwareResponse> {

    private final SoftwareRepository softwares;
    private final SoftwareMapper softwareMapper;
    private final SolicitacaoSoftwareMapper solicitacaoSoftwareMapper;

    public SoftwareService(SoftwareRepository softwares, SoftwareMapper softwareMapper, SolicitacaoSoftwareMapper solicitacaoSoftwareMapper) {
        this.softwares = softwares;
        this.softwareMapper = softwareMapper;
        this.solicitacaoSoftwareMapper = solicitacaoSoftwareMapper;
    }

    @Override
    @Transactional
    public SoftwareResponse criar(CreateSoftwareRequest createSoftwareRequest) {

        Software software = softwareMapper.toEntity(createSoftwareRequest);

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

        try{
            softwares.deleteById(id);

        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Software não encontrado!");
        }catch(DataIntegrityViolationException e){
            Software software = softwares.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Software não encontrado!"));

            software.setStatusRegistro(StatusRegistro.INATIVO);
        }

    }
}
