package br.com.ucsal.aspmanager.instituicao;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
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
