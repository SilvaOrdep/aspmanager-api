package br.com.ucsal.aspmanager.instituicao.mapper;

import br.com.ucsal.aspmanager.instituicao.dto.request.CreateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.request.UpdateInstituicaoEnsinoRequest;
import br.com.ucsal.aspmanager.instituicao.dto.response.InstituicaoEnsinoResponse;
import br.com.ucsal.aspmanager.instituicao.model.InstituicaoEnsino;
import br.com.ucsal.aspmanager.instituicao.model.TelefoneInstituicao;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InstituicaoEnsinoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telefones", source = "telefones", qualifiedByName = "toTelefoneEntities")
    InstituicaoEnsino toEntity(CreateInstituicaoEnsinoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telefones", source = "telefones", qualifiedByName = "toTelefoneEntities")
    void updateEntity(UpdateInstituicaoEnsinoRequest request, @MappingTarget InstituicaoEnsino instituicao);

    @Mapping(target = "telefones", source = "telefones", qualifiedByName = "toTelefoneStrings")
    InstituicaoEnsinoResponse toResponse(InstituicaoEnsino instituicao);

    @Named("toTelefoneEntities")
    default List<TelefoneInstituicao> toTelefoneEntities(List<String> telefones) {
        if (telefones == null || telefones.isEmpty()) {
            return new ArrayList<>();
        }

        List<TelefoneInstituicao> entidades = new ArrayList<>();
        for (String numero : telefones) {
            if (numero == null || numero.isBlank()) {
                continue;
            }

            TelefoneInstituicao telefone = new TelefoneInstituicao();
            telefone.setNumero(numero.replaceAll("[()\\s-]", ""));
            entidades.add(telefone);
        }

        return entidades;
    }

    @Named("toTelefoneStrings")
    default List<String> toTelefoneStrings(List<TelefoneInstituicao> telefones) {
        if (telefones == null || telefones.isEmpty()) {
            return Collections.emptyList();
        }

        return telefones.stream().map(TelefoneInstituicao::getNumero).toList();
    }

    @AfterMapping
    default void vincularInstituicaoNosTelefones(@MappingTarget InstituicaoEnsino instituicao) {
        if (instituicao.getTelefones() == null) {
            return;
        }

        for (TelefoneInstituicao telefone : instituicao.getTelefones()) {
            telefone.setInstituicao(instituicao);
        }
    }
}

