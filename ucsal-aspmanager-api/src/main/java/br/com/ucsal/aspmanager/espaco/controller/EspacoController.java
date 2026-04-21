package br.com.ucsal.aspmanager.espaco.controller;

import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.dto.response.SolicitacaoResponse;
import br.com.ucsal.aspmanager.espaco.service.EspacoService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/espaco")
public class EspacoController extends AbstractCrudController<Long,
        CreateEspacoRequest, UpdateEspacoRequest, EspacoResponse> {

    private final EspacoService espacoService;

    public EspacoController(EspacoService espacoService) {
        super(espacoService);
        this.espacoService = espacoService;
    }

    @Override
    protected URI location(EspacoResponse espaco, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/espaco/{id}").buildAndExpand(espaco.id()).toUri();
    }

    @PostMapping("/solicitacao")
    public ResponseEntity<SolicitacaoResponse> criarSolicitacao(@Valid @RequestBody CreateSolicitacaoRequest createSolicitacao, UriComponentsBuilder uriBuilder){
        SolicitacaoResponse solicitacaoResponse = espacoService.criarSolicitacao(createSolicitacao);
        URI uri = espacoLocation(solicitacaoResponse, uriBuilder);

        return ResponseEntity.created(uri).body(solicitacaoResponse);
    }

    protected URI espacoLocation(SolicitacaoResponse solicitacao, UriComponentsBuilder uriBuilder){
        return uriBuilder.path("/api/v1/espaco/solicitacao{id}").buildAndExpand(solicitacao.idSolicitacao()).toUri();
    }


}
