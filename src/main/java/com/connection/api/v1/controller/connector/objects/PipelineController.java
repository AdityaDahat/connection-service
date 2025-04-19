package com.connection.api.v1.controller.connector.objects;

import com.connection.api.v1.payload.connector.objects.PipelineSetupPayload;
import com.connection.api.v1.service.connector.objects.PipelineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping
    public ResponseEntity<Object> createPipeline(@Validated @RequestBody PipelineSetupPayload payload,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                pipelineService.setupPipeline(payload, authenticationToken)
//        );

        return ResponseEntity.ok().build();
    }

}
