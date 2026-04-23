package br.com.ucsal.aspmanager.software.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.CreateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.dto.response.SolicitacaoSoftwareResponse;
import br.com.ucsal.aspmanager.software.service.SoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/software")
@Tag(name = "Softwares", description = "Gestão de softwares e fluxo de solicitações de cadastro/ativação")
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
        @Operation(
            summary = "Criar solicitação de software",
            description = "Permite ao professor solicitar cadastro/ativação de software para análise administrativa."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da solicitação",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Professor ou disciplina não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    public ResponseEntity<SolicitacaoSoftwareResponse> criarSolicitacao(@Valid @RequestBody CreateSolicitacaoSoftwareRequest request,
                                                                        UriComponentsBuilder uriBuilder) {
        SolicitacaoSoftwareResponse solicitacao = softwareService.criarSolicitacao(request);
        URI uri = uriBuilder.path("/api/v1/software/solicitacoes/{id}").buildAndExpand(solicitacao.id()).toUri();
        return ResponseEntity.created(uri).body(solicitacao);
    }

    @GetMapping("/solicitacoes")
        @Operation(
            summary = "Listar solicitações de software",
            description = "Retorna lista paginada de solicitações de software."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Page<SolicitacaoSoftwareResponse>> buscarSolicitacoes(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(softwareService.buscarSolicitacoes(filtros));
    }

    @GetMapping("/solicitacoes/minhas")
        @Operation(
            summary = "Listar minhas solicitações de software",
            description = "Retorna lista paginada das solicitações do professor autenticado."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Professor autenticado não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Usuário autenticado inválido para esta operação",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Page<SolicitacaoSoftwareResponse>> buscarMinhasSolicitacoes(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(softwareService.buscarMinhasSolicitacoes(filtros));
    }

    @GetMapping("/solicitacoes/{id}")
        @Operation(
            summary = "Buscar solicitação de software por ID",
            description = "Retorna os detalhes de uma solicitação de software específica."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<SolicitacaoSoftwareResponse> buscarSolicitacao(@Parameter(description = "ID da solicitação", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(softwareService.buscarSolicitacao(id));
    }

    @PatchMapping("/solicitacoes/{id}")
        @Operation(
            summary = "Analisar solicitação de software",
            description = "Permite aprovar ou reprovar uma solicitação pendente. Quando aprovada, o software pode ser criado automaticamente."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação analisada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido para análise",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Solicitação já analisada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    public ResponseEntity<SolicitacaoSoftwareResponse> atualizarSolicitacao(@PathVariable Long id,
                                                                            @Valid @RequestBody UpdateSolicitacaoSoftwareRequest request) {
        return ResponseEntity.ok(softwareService.atualizarSolicitacao(id, request));
    }

    @DeleteMapping("/solicitacoes/{id}")
        @Operation(
            summary = "Excluir solicitação de software",
            description = "Exclui uma solicitação de software por identificador."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Void> deletarSolicitacao(@Parameter(description = "ID da solicitação", example = "1") @PathVariable Long id) {
        softwareService.deletarSolicitacao(id);
        return ResponseEntity.noContent().build();
    }
}
