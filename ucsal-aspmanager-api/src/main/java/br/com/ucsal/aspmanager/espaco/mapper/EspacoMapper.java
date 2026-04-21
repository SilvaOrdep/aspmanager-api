package br.com.ucsal.aspmanager.espaco.mapper;

import br.com.ucsal.aspmanager.escola.model.Escola;
import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.model.Espaco;
import br.com.ucsal.aspmanager.software.model.Software;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EspacoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "escola", source = "idEscola", qualifiedByName = "escolaFromId")
    @Mapping(target = "softwares", source = "softwares", qualifiedByName = "softwaresFromIds")
    Espaco toEntity(CreateEspacoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "escola", source = "idEscola", qualifiedByName = "escolaFromId")
    @Mapping(target = "softwares", ignore = true)
    void updateEntity(UpdateEspacoRequest request, @MappingTarget Espaco espaco);

    @Mapping(target = "softwares", source = "softwares", qualifiedByName = "softwaresToIds")
    EspacoResponse toResponse(Espaco espaco);

    @Named("escolaFromId")
    default Escola escolaFromId(Long idEscola) {
        if (idEscola == null) {
            return null;
        }

        Escola escola = new Escola();
        escola.setId(idEscola);
        return escola;
    }

    @Named("softwaresFromIds")
    default List<Software> softwaresFromIds(List<Long> idsSoftwares) {
        if (idsSoftwares == null || idsSoftwares.isEmpty()) {
            return Collections.emptyList();
        }

        return idsSoftwares.stream().map(id -> {
            Software software = new Software();
            software.setId(id);
            return software;
        }).toList();
    }

    @Named("softwaresToIds")
    default List<Long> softwaresToIds(List<Software> softwares) {
        if (softwares == null || softwares.isEmpty()) {
            return Collections.emptyList();
        }

        return softwares.stream().map(Software::getId).toList();
    }
}
