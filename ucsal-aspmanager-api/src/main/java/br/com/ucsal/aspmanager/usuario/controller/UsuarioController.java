package br.com.ucsal.aspmanager.usuario.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.service.UsuarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Override
    protected URI location(UsuarioResponse usuario, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
    }
}
