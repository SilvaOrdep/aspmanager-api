package br.com.ucsal.aspmanager.software.mapper;


import br.com.ucsal.aspmanager.software.dto.request.CreateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SolicitacaoSoftwareResponse;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.model.SolicitacaoSoftware;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SolicitacaoSoftwareMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "software", source = "idSoftware", qualifiedByName = "softwareFromId")
    @Mapping(target = "professor", source = "idProfessor", qualifiedByName = "professorFromId")
    SolicitacaoSoftware toEntity(CreateSolicitacaoSoftwareRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "software", source = "idSoftware", qualifiedByName = "softwareFromId")
    @Mapping(target = "professor", source = "idProfessor", qualifiedByName = "professorFromId")
    void updateEntity(UpdateSolicitacaoSoftwareRequest request, @MappingTarget SolicitacaoSoftware solicitacaoSoftware);

    @Mapping(target = "idSoftware", source = "software", qualifiedByName = "softwareToId")
    @Mapping(target = "idProfessor", source = "professor", qualifiedByName = "professorToId")
    SolicitacaoSoftwareResponse toResponse(SolicitacaoSoftware solicitacaoSoftware);

    @Named("softwareFromId")
    default Software softwareFromId(Long idSoftware) {
        if (idSoftware == null) {
            return null;
        }

        Software software = new Software();
        software.setId(idSoftware);
        return software;
    }

    @Named("professorFromId")
    default Professor professorFromId(Long idProfessor) {
        if (idProfessor == null) {
            return null;
        }

        Professor professor = new Professor();
        professor.setId(idProfessor);
        return professor;
    }

    @Named("softwareToId")
    default Long softwareToId(Software software) {
        return software == null ? null : software.getId();
    }

    @Named("professorToId")
    default Long professorToId(Professor professor) {
        return professor == null ? null : professor.getId();
    }

}