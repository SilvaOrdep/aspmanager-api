package br.com.ucsal.aspmanager.espaco.mapper;

import br.com.ucsal.aspmanager.espaco.dto.request.CreateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.request.UpdateEspacoRequest;
import br.com.ucsal.aspmanager.espaco.dto.response.EspacoResponse;
import br.com.ucsal.aspmanager.espaco.model.Espaco;
import org.mapstruct.*;

//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)*
public interface EspacoMapper {

   /* @Mapping(target = "id", ignore = true)
    @Mapping(target = "sigla", ignore = true)
    @Mapping(target = "nome", ignore = true)
    @Mapping(target = "descricao", ignore = true)
    @Mapping(target = "capacidade_maxima", ignore = true)
    @Mapping(target = "status_registro", ignore = true)
    @Mapping(target = "tipo_computadores", ignore = true)
    @Mapping(target = "tipoEspaco", ignore = true)
    @Mapping(target = "id_escola", ignore = true)
    @Mapping(target = "softwares", ignore = true)
    Espaco toEntity(CreateEspacoRequest request);

    void updateEntity(UpdateEspacoRequest request, @MappingTarget Espaco espaco);

    EspacoResponse toResponse(Espaco espaco);

    @Named("espacoFromId")
    default Espaco espacoFromId(Long idEspaco) {
        if (idEspaco == null) {
            return null;
        }

        Espaco espaco = new Espaco();
        espaco.setId(idEspaco);
        return espaco;
    }*/
}
