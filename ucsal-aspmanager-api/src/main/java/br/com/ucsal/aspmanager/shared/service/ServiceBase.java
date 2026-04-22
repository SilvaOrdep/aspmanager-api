package br.com.ucsal.aspmanager.shared.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceBase<ID, CreateRequest, UpdateRequest, Response> {

    Response criar(CreateRequest createRequest);

    Page<Response> buscarTodos(Pageable filtros);

    Response buscar(ID id);

    Response atualizar(ID id, UpdateRequest updateRequest);

    void deletar(ID id);

}
