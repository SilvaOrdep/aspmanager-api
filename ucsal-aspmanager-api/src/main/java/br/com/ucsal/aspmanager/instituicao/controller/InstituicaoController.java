package br.com.ucsal.aspmanager.instituicao.controller;

import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.instituicao.service.InstituicaoService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/instituicao")
@Tag(name = "Instituições", description = "Gestão de Instituições de Ensino Superior (IES)")
public class InstituicaoController extends AbstractCrudController<Long,
        CreateInstituicaoEnsinoRequest, UpdateInstituicaoEnsinoRequest, InstituicaoEnsinoResponse> {
    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        super(instituicaoService);
        this.instituicaoService = instituicaoService;
    }

    @Override
    protected URI location(InstituicaoEnsinoResponse instituicaoEnsino, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/instituicao/{id}").buildAndExpand(instituicaoEnsino.id()).toUri();
    }
}
