package br.com.ucsal.aspmanager.escola.controller;

import br.com.ucsal.aspmanager.escola.dto.request.CreateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.CreateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateDisciplinaRequest;
import br.com.ucsal.aspmanager.escola.dto.request.UpdateEscolaRequest;
import br.com.ucsal.aspmanager.escola.dto.response.DisciplinaResponse;
import br.com.ucsal.aspmanager.escola.dto.response.EscolaResponse;
import br.com.ucsal.aspmanager.escola.service.EscolaService;
import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;

import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/escola")
public class EscolaController extends AbstractCrudController<Long,
        CreateEscolaRequest, UpdateEscolaRequest, EscolaResponse> {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        super(escolaService);
        this.escolaService = escolaService;
    }

    @Override
    protected URI location(EscolaResponse escola, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/escola/{id}").buildAndExpand(escola.id()).toUri();
    }

    @PostMapping("/disciplina")
    public ResponseEntity<DisciplinaResponse> criarDisciplina(@Valid @RequestBody CreateDisciplinaRequest createDisciplina, UriComponentsBuilder uriBuilder){
        DisciplinaResponse disciplinaResponse = escolaService.criarDisciplina(createDisciplina);
        URI uri = disciplinaLocation(disciplinaResponse, uriBuilder);

        return ResponseEntity.created(uri).body(disciplinaResponse);
    }

    @GetMapping("/disciplina")
    public ResponseEntity<Page<DisciplinaResponse>> buscarDisciplina(Pageable filtros){
        return ResponseEntity.ok(escolaService.buscarDisciplina(filtros));
    }

    @GetMapping("/disciplina/{id}")
    public ResponseEntity<DisciplinaResponse> buscarDisciplina(@PathVariable Long id){
        return ResponseEntity.ok(escolaService.buscarDisciplina(id));
    }

    @PutMapping("disciplina/{id}")
    public ResponseEntity<DisciplinaResponse> atualizarDisciplina(Long id, UpdateDisciplinaRequest updateDisciplinaRequest){
        return ResponseEntity.ok(escolaService.atualizarDisciplina(id, updateDisciplinaRequest)) ;
    }

    @DeleteMapping("disciplina/{id}")
    public ResponseEntity<Void> deletarDisciplina(Long id){
        escolaService.deletarDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    protected URI disciplinaLocation(DisciplinaResponse disciplina, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/escola/disciplina/{id}").buildAndExpand(disciplina.id()).toUri();
    }
}
