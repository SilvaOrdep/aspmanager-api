package br.com.ucsal.aspmanager.software.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.software.dto.request.CreateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SolicitacaoSoftwareResponse;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.service.SoftwareService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/software")
public class SoftwareController extends AbstractCrudController<Long,
        CreateSoftwareRequest, UpdateSoftwareRequest, SoftwareResponse> {

    private final SoftwareService softwareService;

    public SoftwareController(SoftwareService softwareService) {
        super(softwareService);
        this.softwareService = softwareService;
    }

    @Override
    protected URI location(SoftwareResponse software, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/software/{id}").buildAndExpand(software.id()).toUri();
    }

    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoSoftwareResponse> criarSolicitacao(@Valid @RequestBody CreateSolicitacaoSoftwareRequest request,
                                                                        UriComponentsBuilder uriBuilder) {
        SolicitacaoSoftwareResponse solicitacao = softwareService.criarSolicitacao(request);
        URI uri = uriBuilder.path("/api/v1/software/solicitacoes/{id}").buildAndExpand(solicitacao.id()).toUri();
        return ResponseEntity.created(uri).body(solicitacao);
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<Page<SolicitacaoSoftwareResponse>> buscarSolicitacoes(Pageable filtros) {
        return ResponseEntity.ok(softwareService.buscarSolicitacoes(filtros));
    }

    @GetMapping("/solicitacoes/minhas")
    public ResponseEntity<Page<SolicitacaoSoftwareResponse>> buscarMinhasSolicitacoes(Pageable filtros) {
        return ResponseEntity.ok(softwareService.buscarMinhasSolicitacoes(filtros));
    }

    @GetMapping("/solicitacoes/{id}")
    public ResponseEntity<SolicitacaoSoftwareResponse> buscarSolicitacao(@PathVariable Long id) {
        return ResponseEntity.ok(softwareService.buscarSolicitacao(id));
    }

    @PatchMapping("/solicitacoes/{id}")
    public ResponseEntity<SolicitacaoSoftwareResponse> atualizarSolicitacao(@PathVariable Long id,
                                                                             @Valid @RequestBody UpdateSolicitacaoSoftwareRequest request) {
        return ResponseEntity.ok(softwareService.atualizarSolicitacao(id, request));
    }

    @DeleteMapping("/solicitacoes/{id}")
    public ResponseEntity<Void> deletarSolicitacao(@PathVariable Long id) {
        softwareService.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}
