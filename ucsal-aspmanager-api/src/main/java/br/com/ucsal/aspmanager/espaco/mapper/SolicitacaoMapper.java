package br.com.ucsal.aspmanager.espaco.mapper;

import br.com.ucsal.aspmanager.espaco.dto.request.CreateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateSolicitacaoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.SolicitacaoResponse;
import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.espaco.model.SolicitacaoEspaco;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SolicitacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusSolicitacao", ignore = true) // Ignorado pois geralmente é definido na Service
    @Mapping(target = "espaco", source = "idEspaco", qualifiedByName = "espacoFromId")
    @Mapping(target = "professor", source = "idProfessor", qualifiedByName = "professorFromId")
    SolicitacaoEspaco toEntity(CreateSolicitacaoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "espaco", source = "idEspaco", qualifiedByName = "espacoFromId")
    @Mapping(target = "professor", source = "idProfessor", qualifiedByName = "professorFromId")
    void updateEntity(UpdateSolicitacaoRequest request, @MappingTarget SolicitacaoEspaco solicitacaoEspaco);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idEspaco", source = "espaco", qualifiedByName = "espacoToId")
    @Mapping(target = "idProfessor", source = "professor", qualifiedByName = "professorToId")
    SolicitacaoResponse toResponse(SolicitacaoEspaco solicitacaoEspaco);

    @Named("espacoFromId")
    default Espaco espacoFromId(Long idEspaco) {
        if (idEspaco == null) {
            return null;
        }

        Espaco espaco = new Espaco();
        espaco.setId(idEspaco);
        return espaco;
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

    @Named("espacoToId")
    default Long espacoToId(Espaco espaco) {
        return espaco == null ? null : espaco.getId();
    }

    @Named("professorToId")
    default Long professorToId(Professor professor) {
        return professor == null ? null : professor.getId();
    }
}