package br.com.ucsal.aspmanager.software.service;

import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.repository.SoftwareRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SoftwareService implements ServiceBase<Long,
        CreateSoftwareRequest, UpdateSoftwareRequest, SoftwareResponse> {

    private final SoftwareRepository softwares;

    public SoftwareService(SoftwareRepository softwares) {
        this.softwares = softwares;
    }

    @Override
    public SoftwareResponse criar(CreateSoftwareRequest createSoftwareRequest) {

        Software software = Software.builder().
                nome(createSoftwareRequest.nome()).
                versao(createSoftwareRequest.versao()).
                dataCadastro(createSoftwareRequest.dataCadastro()).
                urlDownload(createSoftwareRequest.urlDownload()).
                objetivoUso(createSoftwareRequest.objetivoUso()).
                tipoLicenca(createSoftwareRequest.tipoLicenca()).
                build();

        softwares.save(software);

        return new SoftwareResponse(software.getId(), software.getNome(),
                software.getVersao(), software.getUrlDownload(),
                software.getTipoLicenca(), software.getObjetivoUso(),
                software.getDataCadastro(), software.getStatusRegistro());
    }

    @Override
    public Page<SoftwareResponse> buscarTodos(Pageable filtros) {
        return softwares.findAll(filtros).map(software ->
                new SoftwareResponse(software.getId(), software.getNome(),
                software.getVersao(), software.getUrlDownload(),
                software.getTipoLicenca(), software.getObjetivoUso(),
                software.getDataCadastro(), software.getStatusRegistro()));
    }

    @Override
    public SoftwareResponse buscar(Long id) {
        Software software = softwares.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Software não encontrado!"));

        return new SoftwareResponse(software.getId(), software.getNome(),
                software.getVersao(), software.getUrlDownload(),
                software.getTipoLicenca(), software.getObjetivoUso(),
                software.getDataCadastro(), software.getStatusRegistro());
    }

    @Override
    public SoftwareResponse atualizar(Long id, UpdateSoftwareRequest updateSoftwareRequest) {
        return null;
    }

    @Override
    public void deletar(Long id) {

    }
}
