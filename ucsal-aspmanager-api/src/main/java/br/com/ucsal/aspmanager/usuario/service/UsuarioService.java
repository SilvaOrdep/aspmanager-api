package br.com.ucsal.aspmanager.usuario.service;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.escola.repository.EscolaRepository;
import br.com.ucsal.aspmanager.shared.model.Telefone;
import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.model.TelefoneUsuario;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import br.com.ucsal.aspmanager.usuario.repository.ProfessorRepository;
import br.com.ucsal.aspmanager.usuario.repository.TelefoneUsuarioRepository;
import br.com.ucsal.aspmanager.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class UsuarioService implements UserDetailsService, ServiceBase<Long, CreateUsuarioRequest, UpdateUsuarioRequest, UsuarioResponse> {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder codificadorDeSenha;
    private final ProfessorRepository professores;
    private final EscolaRepository escolas;
    private final TelefoneUsuarioRepository telefones;

    public UsuarioService(UsuarioRepository usuarios, PasswordEncoder codificadorDeSenha, ProfessorRepository professores, EscolaRepository escolas, TelefoneUsuarioRepository telefones) {
        this.usuarios = usuarios;
        this.codificadorDeSenha = codificadorDeSenha;
        this.professores = professores;
        this.escolas = escolas;
        this.telefones = telefones;
    }

    @Override
    @Transactional
    public UsuarioResponse criar(CreateUsuarioRequest createUsuarioRequest) {
        Usuario usuario = Usuario.builder()
                .nomeCompleto(createUsuarioRequest.nomeCompleto())
                .email(createUsuarioRequest.email())
                .senha(codificadorDeSenha.encode(createUsuarioRequest.senha()))
                .perfil(createUsuarioRequest.perfil())
                .statusRegistro(StatusRegistro.ATIVO)
                .telefones(new ArrayList<>())
                .build();

        if (createUsuarioRequest.telefones() != null && !createUsuarioRequest.telefones().isEmpty()) {
            for(String telefone : createUsuarioRequest.telefones()) {
                criarTelefoneParaUsuario(telefone, usuario);
            }
        }

        boolean isProfessor = createUsuarioRequest.perfil().equals(Perfil.PROFESSOR);

        if (createUsuarioRequest.matricula() != null && createUsuarioRequest.idEscola() != null && isProfessor) {
            return criarProfessor(usuario, createUsuarioRequest.matricula(), createUsuarioRequest.idEscola());
        } else if (isProfessor && createUsuarioRequest.matricula() == null || createUsuarioRequest.idEscola() == null) {
            throw new RuntimeException("Matrícula e Escola não devem ser nulas para professores");
        }

        usuarios.save(usuario);

        return new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null, usuario.getTelefones().stream().map(Telefone::getNumero).toList());
    }

    @Override
    public Page<UsuarioResponse> buscarTodos(Pageable filtros) {
        return usuarios.findAll(filtros).map(usuario -> new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), null, usuario.getTelefones().stream().map(Telefone::getNumero).toList()));
    }

    @Override
    public UsuarioResponse buscar(Long aLong) {
        return null;
    }

    @Override
    public UsuarioResponse atualizar(Long aLong, UpdateUsuarioRequest updateUsuarioRequest) {
        return null;
    }

    @Override
    public void deletar(Long aLong) {

    }

    private UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarios.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return null;
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
        return new UsuarioResponse(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getPerfil(), usuario.getStatusRegistro(), professor.getMatricula(), professor.getUsuario().getTelefones().stream().map(Telefone::getNumero).toList());
    }

    private void criarTelefoneParaUsuario(String telefone, Usuario usuario) {
        String telefoneLimpo = telefone.replaceAll("[()\\s-]","");
        if (telefoneLimpo.matches("\\d{10,11}")) {
            TelefoneUsuario telefoneUsuario = new TelefoneUsuario();
            telefoneUsuario.setNumero(telefoneLimpo);
            telefoneUsuario.setUsuario(usuario);

            telefones.save(telefoneUsuario);

            usuario.getTelefones().add(telefoneUsuario);
        }
    }

}
