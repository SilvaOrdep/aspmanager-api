package br.com.ucsal.aspmanager.software.mapper;


import br.com.ucsal.aspmanager.escola.model.Disciplina;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.CreateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSolicitacaoSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SolicitacaoSoftwareResponse;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.model.SolicitacaoSoftware;
import br.com.ucsal.aspmanager.usuario.model.Professor;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SolicitacaoSoftwareMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "tipoSolicitacaoSoftware", ignore = true)
    @Mapping(target = "statusSolicitacao", ignore = true)
    @Mapping(target = "nome", source = "software.nome")
    @Mapping(target = "versao", source = "software.versao")
    @Mapping(target = "urlDownload", source = "software.urlDownload")
    @Mapping(target = "tipoLicenca", source = "software.tipoLicenca")
    @Mapping(target = "objetivoUso", source = "software.objetivoUso")
    @Mapping(target = "disciplinasSolicitadas", source = "software.idDisciplinas", qualifiedByName = "disciplinasFromIds")
    @Mapping(target = "softwareCriado", ignore = true)
    @Mapping(target = "professor", source = "idProfessor", qualifiedByName = "professorFromId")
    SolicitacaoSoftware toEntity(CreateSolicitacaoSoftwareRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "tipoSolicitacaoSoftware", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "nome", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "urlDownload", ignore = true)
    @Mapping(target = "tipoLicenca", ignore = true)
    @Mapping(target = "objetivoUso", ignore = true)
    @Mapping(target = "disciplinasSolicitadas", ignore = true)
    @Mapping(target = "softwareCriado", ignore = true)
    void updateEntity(UpdateSolicitacaoSoftwareRequest request, @MappingTarget SolicitacaoSoftware solicitacaoSoftware);

    @Mapping(target = "software", source = ".", qualifiedByName = "softwareRequestFromSolicitacao")
    @Mapping(target = "idProfessor", source = "professor", qualifiedByName = "professorToId")
    @Mapping(target = "idSoftwareCriado", source = "softwareCriado", qualifiedByName = "softwareToId")
    SolicitacaoSoftwareResponse toResponse(SolicitacaoSoftware solicitacaoSoftware);

    @Named("professorFromId")
    default Professor professorFromId(Long idProfessor) {
        if (idProfessor == null) {
            return null;
        }

        Professor professor = new Professor();
        professor.setId(idProfessor);
        return professor;
    }

    @Named("disciplinasFromIds")
    default List<Disciplina> disciplinasFromIds(List<Long> idsDisciplinas) {
        if (idsDisciplinas == null || idsDisciplinas.isEmpty()) {
            return Collections.emptyList();
        }

        return idsDisciplinas.stream().map(id -> {
            Disciplina disciplina = new Disciplina();
            disciplina.setId(id);
            return disciplina;
        }).toList();
    }

    @Named("disciplinaToId")
    default Long disciplinaToId(Disciplina disciplina) {
        return disciplina == null ? null : disciplina.getId();
    }

    @Named("softwareRequestFromSolicitacao")
    default CreateSoftwareRequest softwareRequestFromSolicitacao(SolicitacaoSoftware solicitacao) {
        List<Long> idsDisciplinas = solicitacao.getDisciplinasSolicitadas() == null
                ? Collections.emptyList()
                : solicitacao.getDisciplinasSolicitadas().stream().map(this::disciplinaToId).toList();

        return new CreateSoftwareRequest(
                solicitacao.getNome(),
                solicitacao.getVersao(),
                solicitacao.getUrlDownload(),
                solicitacao.getTipoLicenca(),
                solicitacao.getObjetivoUso(),
                idsDisciplinas
        );
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