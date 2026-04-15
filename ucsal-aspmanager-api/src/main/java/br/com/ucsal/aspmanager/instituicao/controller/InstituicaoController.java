package br.com.ucsal.aspmanager.instituicao.controller;

import br.com.ucsal.aspmanager.instituicao.service.InstituicaoService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/instituicao")
public class InstituicaoController extends AbstractCrudController {
    public InstituicaoController(InstituicaoService instituicaoService) {
        super(instituicaoService);
    }

    @Override
    protected URI location(Object createDTO, UriComponentsBuilder uriBuilder) {
        return null;
    }
}
