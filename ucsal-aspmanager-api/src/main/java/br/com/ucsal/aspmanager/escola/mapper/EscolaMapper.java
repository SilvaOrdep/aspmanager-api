package br.com.ucsal.aspmanager.escola.mapper;

import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EscolaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    @Mapping(target = "instituicao", source = "idInstituicao", qualifiedByName = "instituicaoFromId")
    @Mapping(target = "coordenador", source = "idCoordenador", qualifiedByName = "professorFromId")
    Escola toEntity(CreateEscolaRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    @Mapping(target = "instituicao", source = "idInstituicao", qualifiedByName = "instituicaoFromId")
    @Mapping(target = "coordenador", source = "idCoordenador", qualifiedByName = "professorFromId")
    void updateEntity(UpdateEscolaRequest request, @MappingTarget Escola escola);

    @Mapping(target = "idInstituicao", source = "instituicao", qualifiedByName = "instituicaoToId")
    @Mapping(target = "idCoordenador", source = "coordenador", qualifiedByName = "professorToId")
    @Mapping(target = "idsDisciplinas", source = "disciplinas", qualifiedByName = "disciplinasToIds")
    EscolaResponse toResponse(Escola escola);

    @Named("instituicaoFromId")
    default InstituicaoEnsino instituicaoFromId(Long idInstituicao) {
        if (idInstituicao == null) {
            return null;
        }

        InstituicaoEnsino instituicao = new InstituicaoEnsino();
        instituicao.setId(idInstituicao);
        return instituicao;
    }

    @Named("professorFromId")
    default Professor professorFromId(Long idCoordenador) {
        if (idCoordenador == null) {
            return null;
        }

        Professor professor = new Professor();
        professor.setId(idCoordenador);
        return professor;
    }

    @Named("instituicaoToId")
    default Long instituicaoToId(InstituicaoEnsino instituicao) {
        return instituicao == null ? null : instituicao.getId();
    }

    @Named("professorToId")
    default Long professorToId(Professor professor) {
        return professor == null ? null : professor.getId();
    }

    @Named("disciplinasToIds")
    default List<Long> disciplinasToIds(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return Collections.emptyList();
        }

        return disciplinas.stream().map(Disciplina::getId).toList();
    }
}

