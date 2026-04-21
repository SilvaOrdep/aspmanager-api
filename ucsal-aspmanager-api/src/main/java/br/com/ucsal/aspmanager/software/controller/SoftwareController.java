package br.com.ucsal.aspmanager.software.controller;

import br.com.ucsal.aspmanager.shared.controller.AbstractCrudController;
import br.com.ucsal.aspmanager.shared.service.ServiceBase;
import br.com.ucsal.aspmanager.software.dto.request.CreateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.request.UpdateSoftwareRequest;
import br.com.ucsal.aspmanager.software.dto.response.SoftwareResponse;
import br.com.ucsal.aspmanager.software.model.Software;
import br.com.ucsal.aspmanager.software.service.SoftwareService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/software")
public class SoftwareController extends AbstractCrudController<Long,
        CreateSoftwareRequest, UpdateSoftwareRequest, SoftwareResponse> {

    private final SoftwareService softwareService;

    public SoftwareController(SoftwareService softwareService) {
        super(softwareService);
        this.softwareService = softwareService;
    }

    @Override
    protected URI location(SoftwareResponse software, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/software/{id}").buildAndExpand(software.id()).toUri();
    }
}
