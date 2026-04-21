package br.com.ucsal.aspmanager.usuario.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.usuario.dto.request.AlterarSenhaRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateProfessorRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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

    @PatchMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, AlterarSenhaRequest request) {
        usuarioService.alterarSenha(request, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/professores")
    public ResponseEntity<Page<UsuarioResponse>> buscarTodosOsProfessores(Pageable filtros) {
        return ResponseEntity.ok(usuarioService.buscarTodosProfessores(filtros));
    }

    @PutMapping("/professores/{idProfessor}")
    public ResponseEntity<UsuarioResponse> atualizarProfessor(@PathVariable Long idProfessor, @RequestBody UpdateProfessorRequest request) {
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
}
