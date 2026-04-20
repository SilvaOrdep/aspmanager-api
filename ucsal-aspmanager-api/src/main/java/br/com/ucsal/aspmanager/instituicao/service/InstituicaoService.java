package br.com.ucsal.aspmanager.instituicao.service;

import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.instituicao.model.TelefoneInstituicao;
import br.com.ucsal.aspmanager.instituicao.repository.InstituicaoEnsinoRepository;
import br.com.ucsal.aspmanager.instituicao.repository.TelefoneInstituicaoRepository;
import br.com.ucsal.aspmanager.shared.model.Telefone;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstituicaoService implements ServiceBase<Long,
        CreateInstituicaoEnsinoRequest, UpdateInstituicaoEnsinoRequest, InstituicaoEnsinoResponse> {

    private final InstituicaoEnsinoRepository instituicoes;
    private final TelefoneInstituicaoRepository telefonesInstituicao;

    public InstituicaoService(InstituicaoEnsinoRepository instituicoes, TelefoneInstituicaoRepository telefonesInstituicao){
        this.instituicoes = instituicoes;
        this.telefonesInstituicao = telefonesInstituicao;
    }

    @Override
    public InstituicaoEnsinoResponse criar(CreateInstituicaoEnsinoRequest createInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = InstituicaoEnsino.builder()
                .nome(createInstituicaoEnsinoRequest.nome())
                .endereco(createInstituicaoEnsinoRequest.endereco())
                .telefones(new ArrayList<>()).build();

        if (createInstituicaoEnsinoRequest.telefones() != null && !createInstituicaoEnsinoRequest.telefones().isEmpty()) {
            for(String telefone : createInstituicaoEnsinoRequest.telefones()) {
                criarTelefoneParaIES(telefone, instituicao);
            }
        }

        instituicoes.save(instituicao);

        return new InstituicaoEnsinoResponse(instituicao.getId(), instituicao.getNome(),
                instituicao.getEndereco(), instituicao.getTelefones().stream().map(Telefone::getNumero).toList());
    }

    @Override
    public Page<InstituicaoEnsinoResponse> buscarTodos(Pageable filtros) {

        return instituicoes.findAll(filtros).map(instituicao ->
                new InstituicaoEnsinoResponse(instituicao.getId(), instituicao.getNome(),
                instituicao.getEndereco(), instituicao.getTelefones().stream().map(Telefone::getNumero).toList()));
    }

    @Override
    public InstituicaoEnsinoResponse buscar(Long id) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        return new InstituicaoEnsinoResponse(instituicao.getId(), instituicao.getNome(),
                instituicao.getEndereco(), instituicao.getTelefones().stream().map(Telefone::getNumero).toList());
    }

    @Override
    public InstituicaoEnsinoResponse atualizar(Long id, UpdateInstituicaoEnsinoRequest updateInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        instituicao.setNome(updateInstituicaoEnsinoRequest.nome());
        instituicao.setEndereco(updateInstituicaoEnsinoRequest.endereco());

        if(updateInstituicaoEnsinoRequest.telefones() != null
                && !updateInstituicaoEnsinoRequest.telefones().isEmpty()){

            List telefonesIES = instituicao.getTelefones();
            telefonesIES.addAll(updateInstituicaoEnsinoRequest.telefones());

            instituicao.setTelefones(telefonesIES);

        }
        return new InstituicaoEnsinoResponse(instituicao.getId(), instituicao.getNome(),
                instituicao.getEndereco(), instituicao.getTelefones().stream().map(Telefone::getNumero).toList());
    }

    @Override
    public void deletar(Long id) {



    }

    private void criarTelefoneParaIES(String telefone, InstituicaoEnsino instituicaoEnsino) {
        String telefoneLimpo = telefone.replaceAll("[()\\s-]","");
        if (telefoneLimpo.matches("\\d{10,11}")) {
            TelefoneInstituicao telefoneInstituicao = new TelefoneInstituicao();
            telefoneInstituicao.setNumero(telefoneLimpo);
            telefoneInstituicao.setInstituicao(instituicaoEnsino);

            telefonesInstituicao.save(telefoneInstituicao);

            instituicaoEnsino.getTelefones().add(telefoneInstituicao);
        }
    }
}
