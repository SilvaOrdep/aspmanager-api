package br.com.ucsal.aspmanager.software.mapper;

import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.model.Software;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SoftwareMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    Software toEntity(CreateSoftwareRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    void updateEntity(UpdateSoftwareRequest request, @MappingTarget Software software);

    SoftwareResponse toResponse(Software software);
}