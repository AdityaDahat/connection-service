package com.connection.api.v1.controller.connector.management;

import com.connection.api.v1.payload.connector.management.PipelineTemplateCreationPayload;
import com.connection.api.v1.payload.connector.management.PipelineTemplateUpdatePayload;
import com.connection.api.v1.service.connector.management.PipelineTemplateService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/management")
@RestController
public class pipelineTemplateController {

    private final PipelineTemplateService pipelineTemplateService;

    @Autowired
    public pipelineTemplateController(PipelineTemplateService pipelineTemplateService) {
        this.pipelineTemplateService = pipelineTemplateService;
    }

    /***
     * @param payload
     * @param authenticationToken
     * @return PipelineTemplate Object
     */
    @PostMapping("/pipeline-template")
    public ResponseEntity<Object> createTemplate(@RequestBody PipelineTemplateCreationPayload payload,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.createPipelineTemplate(payload, authenticationToken));
    }

    /***
     * @param payload
     * @param authenticationToken
     * @return PipelineTemplate Object
     */
    @PutMapping("/pipeline-template")
    public ResponseEntity<Object> updateTemplate(@RequestBody PipelineTemplateUpdatePayload payload,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.updatePipelineTemplate(payload, authenticationToken));
    }

    /***
     *
     * @param ids
     * @param authentication
     * @return Bulk Delete PipelineTemplate
     */
    @DeleteMapping("/pipeline-template")
    public ResponseEntity<Object> delete(@RequestBody List<String> ids, @RequestHeader(HttpHeaders.AUTHORIZATION) String authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.deleteTemplates(ids, authentication));
    }


    /**
     * @param id
     * @return PipelineTemplate Object
     */
    @GetMapping("/pipeline-template/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.getPipelineTemplate(id));
    }

    /**
     * @param pageable
     * @return List of PipelineTemplate Object
     */
    @GetMapping("/pipeline-templates")
    public ResponseEntity<Object> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.getAllPipelineTemplates(pageable));
    }

    /**
     * @param name
     * @param pageable
     * @return List of Pipeline Templates
     */
    @GetMapping("/search/pipeline-template/{name}")
    public ResponseEntity<Object> getWiringByName(@PathVariable("name") String name, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pipelineTemplateService.getTemplateByName(name, pageable));
    }
}
