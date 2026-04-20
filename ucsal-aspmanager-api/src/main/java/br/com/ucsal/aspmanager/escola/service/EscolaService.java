package br.com.ucsal.aspmanager.escola.service;

import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.repository.DisciplinaRepository;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;
import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EscolaService implements ServiceBase<Long,
        CreateEscolaRequest, UpdateEscolaRequest, EscolaResponse> {

    private final EscolaRepository escolas;
    private final DisciplinaRepository disciplinas;

    public EscolaService(EscolaRepository escolas, DisciplinaRepository disciplinas){
        this.escolas = escolas;
        this.disciplinas = disciplinas;
    }


    @Override
    public EscolaResponse criar(CreateEscolaRequest createEscolaRequest) {
        return null;
    }

    @Override
    public Page<EscolaResponse> buscarTodos(Pageable filtros) {
        return null;
    }

    @Override
    public EscolaResponse buscar(Long aLong) {
        return null;
    }

    @Override
    public EscolaResponse atualizar(Long aLong, UpdateEscolaRequest updateEscolaRequest) {
        return null;
    }

    @Override
    public void deletar(Long aLong) {

    }
}
