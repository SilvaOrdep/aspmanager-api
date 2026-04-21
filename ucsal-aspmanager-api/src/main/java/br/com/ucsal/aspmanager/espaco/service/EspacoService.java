package br.com.ucsal.aspmanager.espaco.service;

import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.espaco.repository.EspacoRepository;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.repository.SoftwareRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspacoService implements ServiceBase<Long,
        CreateEspacoRequest, UpdateEspacoRequest, EspacoResponse> {

    private final EspacoRepository espacos;
    private final EscolaRepository escolas;
    private final SoftwareRepository softwares;

    public EspacoService(EspacoRepository espacos, EscolaRepository escolas, SoftwareRepository softwares) {
        this.espacos = espacos;
        this.escolas = escolas;
        this.softwares = softwares;
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
                espaco.getEscola(), espaco.getSoftwares(), espaco.getStatusRegistro());
    }

    @Override
    public Page<EspacoResponse> buscarTodos(Pageable filtros) {
        return null;
    }

    @Override
    public EspacoResponse buscar(Long id) {
        return null;
    }

    @Override
    public EspacoResponse atualizar(Long id, UpdateEspacoRequest request) {
        return null;
    }

    @Override
    public void deletar(Long id) {

    }
}
