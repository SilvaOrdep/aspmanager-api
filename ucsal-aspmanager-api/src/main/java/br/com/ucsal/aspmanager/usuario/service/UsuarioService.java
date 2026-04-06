package br.com.ucsal.aspmanager.usuario.service;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;
import br.com.ucsal.aspmanager.shared.model.Telefone;
import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import br.com.ucsal.aspmanager.usuario.repository.ProfessorRepository;
import br.com.ucsal.aspmanager.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ProfessorRepository professores;
    private final EscolaRepository escolas;

    public UsuarioService(UsuarioRepository usuarios, PasswordEncoder codificadorDeSenha, ProfessorRepository professores, EscolaRepository escolas) {
        this.usuarios = usuarios;
        this.codificadorDeSenha = codificadorDeSenha;
        this.professores = professores;
        this.escolas = escolas;
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

        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null, null);

        if (createUsuarioRequest.matricula() != null && createUsuarioRequest.idEscola() != null && createUsuarioRequest.perfil().equals(Perfil.PROFESSOR)) {
            response = criarProfessor(usuario, createUsuarioRequest.matricula(), createUsuarioRequest.idEscola());
        }

        return response;
    }

    public Page<UsuarioResponse> buscarTodos(Pageable filtros) {
        return usuarios.findAll(filtros).map(usuario -> new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null, usuario.getTelefones().stream().map(Telefone::getNumero).toList()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarios.findUsuarioByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }

    private UsuarioResponse criarProfessor(Usuario usuario, String matricula, Long idEscola) {
        Escola escola = escolas.findById(idEscola).orElseThrow(() -> new EntityNotFoundException("Escola não encontrada"));
        Professor professor = Professor.builder()
                .matricula(matricula)
                .escola(escola)
                .usuario(usuario)
                .build();

        professores.save(professor);
        return new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), professor.getMatricula(), null);
    }

}
