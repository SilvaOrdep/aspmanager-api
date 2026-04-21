package br.com.ucsal.aspmanager.usuario.mapper;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.shared.model.Telefone;
import br.com.ucsal.aspmanager.usuario.dto.request.CreateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateProfessorRequest;
import br.com.ucsal.aspmanager.usuario.dto.request.UpdateUsuarioRequest;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import br.com.ucsal.aspmanager.usuario.model.TelefoneUsuario;
import br.com.ucsal.aspmanager.usuario.model.Usuario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "telefones", source = "telefones", qualifiedByName = "toTelefoneEntities")
    Usuario toEntity(CreateUsuarioRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateEntity(UpdateUsuarioRequest request, @MappingTarget Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "escola", source = "idEscola", qualifiedByName = "escolaFromId")
    void updateProfessor(UpdateProfessorRequest request, @MappingTarget Professor professor);

    default UsuarioResponse toResponse(Usuario usuario, Professor professor) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getStatusRegistro(),
                professor == null ? null : professor.getMatricula(),
                toTelefoneStrings(usuario.getTelefones())
        );
    }

    default UsuarioResponse toResponse(Usuario usuario) {
        return toResponse(usuario, null);
    }

    default Professor toProfessor(Usuario usuario, Escola escola, String matricula) {
        return Professor.builder()
                .usuario(usuario)
                .escola(escola)
                .matricula(matricula)
                .build();
    }

    @Named("escolaFromId")
    default Escola escolaFromId(Long idEscola) {
        if (idEscola == null) {
            return null;
        }

        Escola escola = new Escola();
        escola.setId(idEscola);
        return escola;
    }

    @Named("toTelefoneEntities")
    default List<TelefoneUsuario> toTelefoneEntities(List<String> telefones) {
        if (telefones == null || telefones.isEmpty()) {
            return new ArrayList<>();
        }

        List<TelefoneUsuario> entidades = new ArrayList<>();
        for (String numero : telefones) {
            if (numero == null || numero.isBlank()) {
                continue;
            }

            TelefoneUsuario telefone = new TelefoneUsuario();
            telefone.setNumero(numero.replaceAll("[()\\s-]", ""));
            entidades.add(telefone);
        }

        return entidades;
    }

    @Named("toTelefoneStrings")
    default List<String> toTelefoneStrings(List<? extends Telefone> telefones) {
        if (telefones == null || telefones.isEmpty()) {
            return Collections.emptyList();
        }

        return telefones.stream().map(Telefone::getNumero).toList();
    }

    @AfterMapping
    default void vincularTelefonesAoUsuario(@MappingTarget Usuario usuario) {
        if (usuario.getTelefones() == null) {
            return;
        }

        for (TelefoneUsuario telefone : usuario.getTelefones()) {
            telefone.setUsuario(usuario);
        }
    }
}

