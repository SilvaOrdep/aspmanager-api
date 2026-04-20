package br.com.ucsal.aspmanager.escola.controller;

import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.service.EscolaService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;

import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/escola")
public class EscolaController extends AbstractCrudController<Long,
        CreateEscolaRequest, UpdateEscolaRequest, EscolaResponse> {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        super(escolaService);
        this.escolaService = escolaService;
    }

    @Override
    protected URI location(EscolaResponse escola, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/escola/{id}").buildAndExpand(escola.id()).toUri();
    }
}
