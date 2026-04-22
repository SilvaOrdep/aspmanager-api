package br.com.ucsal.aspmanager.usuario.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.usuario.dto.request.AlterarSenhaRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateProfessorRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.service.UsuarioService;
import jakarta.validation.Valid;
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
public class UsuarioController extends AbstractCrudController<Long, CreateUsuarioRequest, UpdateUsuarioRequest, UsuarioResponse> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> alterarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.alterarStatusRegistro(id));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long id) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        return ResponseEntity.ok(usuarioService.buscar(id));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid UpdateUsuarioRequest request) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @PatchMapping("/{id}/alterar-senha")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody @Valid AlterarSenhaRequest request) {
        UsuarioResponse usuarioAutenticado = usuarioAutenticado();
        validarAcessoAoUsuario(id, usuarioAutenticado);
        usuarioService.alterarSenha(request, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/professores")
    public ResponseEntity<Page<UsuarioResponse>> buscarTodosOsProfessores(Pageable filtros) {
        return ResponseEntity.ok(usuarioService.buscarTodosProfessores(filtros));
    }

    @PutMapping("/professores/{idProfessor}")
    public ResponseEntity<UsuarioResponse> atualizarProfessor(@PathVariable Long idProfessor, @RequestBody @Valid UpdateProfessorRequest request) {
        return ResponseEntity.ok(usuarioService.atualizarProfessor(idProfessor, request));
    }

    @DeleteMapping("/professores/{idProfessor}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long idProfessor) {
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
