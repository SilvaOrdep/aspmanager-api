package br.com.ucsal.aspmanager.shared.controller;

import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public abstract class AbstractCrudController<ID, CreateRequest, UpdateRequest, Response> {

    public ServiceBase<ID, CreateRequest, UpdateRequest, Response> servicoBase;

    public AbstractCrudController(ServiceBase<ID, CreateRequest, UpdateRequest, Response> servicoBase) {
        this.servicoBase = servicoBase;
    }

        @Operation(
            summary = "Criar registro",
            description = "Cria um novo registro do recurso correspondente ao endpoint."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Entidade relacionada não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio/integridade",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    @PostMapping
    public ResponseEntity<Response> criar(@Valid @RequestBody CreateRequest createRequest, UriComponentsBuilder uriBuilder) {
        Response criar = servicoBase.criar(createRequest);
        URI uri = location(criar, uriBuilder);

        return ResponseEntity.created(uri).body(criar);
    }

        @Operation(
            summary = "Listar registros",
            description = "Retorna uma lista paginada de registros do recurso correspondente ao endpoint."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    @GetMapping
        public ResponseEntity<Page<Response>> buscar(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(servicoBase.buscarTodos(filtros));
    }

        @Operation(
            summary = "Buscar registro por ID",
            description = "Retorna os detalhes de um registro específico por identificador."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    @GetMapping("/{id}")
        public ResponseEntity<Response> buscar(@Parameter(description = "Identificador do registro", example = "1") @PathVariable ID id) {
        return ResponseEntity.ok(servicoBase.buscar(id));
    }

        @Operation(
            summary = "Atualizar registro",
            description = "Atualiza os dados de um registro existente."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registro ou entidade relacionada não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio/integridade",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    @PutMapping("/{id}")
        public ResponseEntity<Response> atualizar(@Parameter(description = "Identificador do registro", example = "1") @PathVariable ID id,
                              @RequestBody @Valid UpdateRequest updateRequest) {
        return ResponseEntity.ok(servicoBase.atualizar(id, updateRequest));
    }

        @Operation(
            summary = "Excluir/inativar registro",
            description = "Remove o registro quando permitido. Dependendo da regra de negócio, o recurso pode ser inativado em vez de excluído fisicamente."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro removido/inativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio/integridade",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
    @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@Parameter(description = "Identificador do registro", example = "1") @PathVariable ID id) {
        servicoBase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    protected abstract URI location(Response createDTO, UriComponentsBuilder uriBuilder);

}
