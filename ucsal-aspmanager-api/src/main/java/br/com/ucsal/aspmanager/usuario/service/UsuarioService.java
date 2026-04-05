package br.com.ucsal.aspmanager.usuario.service;

import br.com.ucsal.aspmanager.shared.model.Telefone;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import br.com.ucsal.aspmanager.usuario.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder codificadorDeSenha;

    public UsuarioService(UsuarioRepository usuarios, PasswordEncoder codificadorDeSenha) {
        this.usuarios = usuarios;
        this.codificadorDeSenha = codificadorDeSenha;
    }

    public UsuarioResponse criar(CreateUsuarioRequest createUsuarioRequest) {
        Usuario usuario = Usuario.builder()
                .nomeCompleto(createUsuarioRequest.nomeCompleto())
                .email(createUsuarioRequest.email())
                .senha(codificadorDeSenha.encode(createUsuarioRequest.senha()))
                .perfil(createUsuarioRequest.perfil())
                .statusRegistro(StatusRegistro.ATIVO)
                .build();

        usuarios.save(usuario);

        return new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null);
    }

    public Page<UsuarioResponse> buscarTodos(Pageable filtros) {
        return usuarios.findAll(filtros).map(usuario -> new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), usuario.getTelefones().stream().map(Telefone::getNumero).toList()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarios.findUsuarioByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
}
