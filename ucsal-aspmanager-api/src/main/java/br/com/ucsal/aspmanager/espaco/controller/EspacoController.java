package br.com.ucsal.aspmanager.espaco.controller;

import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.dto.response.SolicitacaoResponse;
import br.com.ucsal.aspmanager.espaco.service.EspacoService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/solicitacao")
    public ResponseEntity<Page<SolicitacaoResponse>> buscarSolicitacao(Pageable filtros){
        return ResponseEntity.ok(espacoService.buscarSolicitacao(filtros));
    }

    @GetMapping("/solicitacao/{id}")
    public ResponseEntity<SolicitacaoResponse> buscarSolicitacao(@PathVariable Long id){
        return ResponseEntity.ok(espacoService.buscarSolicitacao(id));
    }

    @PutMapping("/solicitacao/{id}")
    public ResponseEntity<SolicitacaoResponse> atualizarSolicitacao(@PathVariable Long id, UpdateSolicitacaoRequest request){
        return ResponseEntity.ok(espacoService.atualizarSolicitacao(id, request));
    }

    @PatchMapping("/solicitacao/{id}")
    public ResponseEntity<SolicitacaoResponse> mudarStatusSolicitacao(@PathVariable Long id, StatusSolicitacao statusSolicitacao){
        return ResponseEntity.ok(espacoService.mudarStatusSolicitacao(id, statusSolicitacao));
    }

    @DeleteMapping("/solicitacao/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id){
        espacoService.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }

}
