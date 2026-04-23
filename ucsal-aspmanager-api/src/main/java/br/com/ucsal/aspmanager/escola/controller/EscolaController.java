package br.com.ucsal.aspmanager.escola.controller;

import br.com.ucsal.aspmanager.escola.dto.request.CreateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.DisciplinaResponse;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.service.EscolaService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
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
@RequestMapping("/api/v1/escola")
@Tag(name = "Escolas", description = "Gestão de escolas e disciplinas acadêmicas")
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

    @PostMapping("/disciplina")
        @Operation(
            summary = "Criar disciplina",
            description = "Cadastra uma nova disciplina vinculada a uma escola."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da disciplina",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<DisciplinaResponse> criarDisciplina(@Valid @RequestBody CreateDisciplinaRequest createDisciplina,
                                       UriComponentsBuilder uriBuilder) {
        DisciplinaResponse disciplinaResponse = escolaService.criarDisciplina(createDisciplina);
        URI uri = disciplinaLocation(disciplinaResponse, uriBuilder);

        return ResponseEntity.created(uri).body(disciplinaResponse);
    }

    @GetMapping("/disciplina")
        @Operation(
            summary = "Listar disciplinas",
            description = "Retorna uma lista paginada de disciplinas cadastradas."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Page<DisciplinaResponse>> buscarDisciplina(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(escolaService.buscarDisciplina(filtros));
    }

    @GetMapping("/disciplina/{id}")
        @Operation(
            summary = "Buscar disciplina por ID",
            description = "Retorna os dados de uma disciplina específica."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<DisciplinaResponse> buscarDisciplina(@Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(escolaService.buscarDisciplina(id));
    }

    @PutMapping("disciplina/{id}")
        @Operation(
            summary = "Atualizar disciplina",
            description = "Atualiza os dados de uma disciplina existente."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina ou escola não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<DisciplinaResponse> atualizarDisciplina(@Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id,
                                      @RequestBody @Valid UpdateDisciplinaRequest updateDisciplinaRequest) {
        return ResponseEntity.ok(escolaService.atualizarDisciplina(id, updateDisciplinaRequest));
    }

    @DeleteMapping("disciplina/{id}")
        @Operation(
            summary = "Excluir disciplina",
            description = "Exclui uma disciplina por identificador."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Disciplina com vínculo de integridade",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Void> deletarDisciplina(@Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id) {
        escolaService.deletarDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    protected URI disciplinaLocation(DisciplinaResponse disciplina, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/escola/disciplina/{id}").buildAndExpand(disciplina.id()).toUri();
    }
}
