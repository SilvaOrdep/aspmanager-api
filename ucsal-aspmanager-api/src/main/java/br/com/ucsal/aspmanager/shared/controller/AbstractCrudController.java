package br.com.ucsal.aspmanager.shared.controller;

import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public abstract class AbstractCrudController<ID, CreateRequest, UpdateRequest, Response> {

    public ServiceBase<ID, CreateRequest, UpdateRequest, Response> servicoBase;

    public AbstractCrudController(ServiceBase<ID, CreateRequest, UpdateRequest, Response> servicoBase) {
        this.servicoBase = servicoBase;
    }

    @PostMapping
    public ResponseEntity<Response> criar(@Valid @RequestBody CreateRequest createRequest, UriComponentsBuilder uriBuilder) {
        Response criar = servicoBase.criar(createRequest);
        URI uri = location(criar, uriBuilder);

        return ResponseEntity.created(uri).body(criar);
    }

    @GetMapping
    public ResponseEntity<Page<Response>> buscar(Pageable filtros) {
        return ResponseEntity.ok(servicoBase.buscarTodos(filtros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> buscar(@PathVariable ID id) {
        return ResponseEntity.ok(servicoBase.buscar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> atualizar(@PathVariable ID id, @RequestBody @Valid UpdateRequest updateRequest) {
        return ResponseEntity.ok(servicoBase.atualizar(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable ID id) {
        servicoBase.deletar(id);
        return ResponseEntity.noContent().build();
    }

    protected abstract URI location(Response createDTO, UriComponentsBuilder uriBuilder);

}
