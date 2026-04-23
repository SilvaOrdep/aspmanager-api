package br.com.ucsal.aspmanager.usuario.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.usuario.dto.request.AlterarSenhaRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateProfessorRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.service.UsuarioService;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Gestão de usuários do sistema e cadastro de professores")
public class UsuarioController extends AbstractCrudController<Long, CreateUsuarioRequest, UpdateUsuarioRequest, UsuarioResponse> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    @PatchMapping("/{id}")
        @Operation(
            summary = "Alternar status de usuário",
            description = "Altera o status entre ATIVO e INATIVO para o usuário informado."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<UsuarioResponse> alterarStatus(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.alterarStatusRegistro(id));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
        @Operation(
            summary = "Buscar usuário por ID",
            description = "ADMIN pode consultar qualquer usuário. PROFESSOR só pode consultar o próprio cadastro."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este usuário",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<UsuarioResponse> buscar(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        return ResponseEntity.ok(usuarioService.buscar(id));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
        @Operation(
            summary = "Atualizar usuário",
            description = "ADMIN pode atualizar qualquer usuário. PROFESSOR só pode atualizar o próprio cadastro."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para atualizar este usuário",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<UsuarioResponse> atualizar(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
                                 @RequestBody @Valid UpdateUsuarioRequest request) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @PatchMapping("/{id}/alterar-senha")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
        @Operation(
            summary = "Alterar senha do usuário",
            description = "Permite alterar senha informando senha atual e nova senha."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou senha atual incorreta",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão para alterar senha deste usuário",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Void> alterarSenha(@Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
                             @RequestBody @Valid AlterarSenhaRequest request) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        usuarioService.alterarSenha(request, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/professores")
        @Operation(
            summary = "Listar professores",
            description = "Retorna lista paginada de professores ativos."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Page<UsuarioResponse>> buscarTodosOsProfessores(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(usuarioService.buscarTodosProfessores(filtros));
    }

    @PutMapping("/professores/{idProfessor}")
        @Operation(
            summary = "Atualizar professor",
            description = "Atualiza dados acadêmicos do professor, como matrícula e escola vinculada."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<UsuarioResponse> atualizarProfessor(@Parameter(description = "ID do professor", example = "1") @PathVariable Long idProfessor,
                                      @RequestBody @Valid UpdateProfessorRequest request) {
        return ResponseEntity.ok(usuarioService.atualizarProfessor(idProfessor, request));
    }

    @DeleteMapping("/professores/{idProfessor}")
        @Operation(
            summary = "Excluir professor",
            description = "Exclui o vínculo de professor por identificador."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Professor excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado",
                content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
        })
        public ResponseEntity<Void> deletarProfessor(@Parameter(description = "ID do professor", example = "1") @PathVariable Long idProfessor) {
        usuarioService.deletarProfessor(idProfessor);
        return ResponseEntity.noContent().build();
    }

    @Override
    protected URI location(UsuarioResponse usuario, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
    }

    private void validarAcessoAoUsuario(Long id, UsuarioResponse usuarioAutenticado) {
        if (usuarioAutenticado == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        if (usuarioAutenticado.perfil() != Perfil.ADMIN && !Objects.equals(id, usuarioAutenticado.id())) {
            throw new AccessDeniedException("Sem permissão para acessar este usuário");
        }
    }

    private UsuarioResponse usuarioAutenticado() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UsuarioResponse usuarioResponse) {
            return usuarioResponse;
        }

        return null;
    }
}
