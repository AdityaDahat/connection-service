package com.connection.api.v1.service.connector.management;

import com.connection.api.v1.model.common.MetadataUtil;
import com.connection.api.v1.model.connector.management.OperationModes;
import com.connection.api.v1.model.connector.management.PipelineTemplate;
import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.payload.connector.management.PipelineTemplateCreationPayload;
import com.connection.api.v1.payload.connector.management.PipelineTemplateUpdatePayload;
import com.connection.api.v1.repository.connector.management.PipelineTemplateRepository;
import com.connection.constants.Constants;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PipelineTemplateService {

    private final PipelineTemplateRepository templateRepository;
    private final MetadataUtil metadataUtil;

    @Autowired
    public PipelineTemplateService(PipelineTemplateRepository templateRepository, MetadataUtil metadataUtil) {
        this.templateRepository = templateRepository;
        this.metadataUtil = metadataUtil;
    }

    public PipelineTemplate createPipelineTemplate(PipelineTemplateCreationPayload payload, String authenticationToken) {
        Optional<PipelineTemplate> existingTemplate = templateRepository.findByNameAndIsDeleted(payload.getName(),
                false);

        if (existingTemplate.isPresent())
            throw new ApiException(new ApiResponse<>(HttpStatus.CONFLICT.getReasonPhrase(), CustomMessages.PIPELINE_TEMPLATE_NAME_ALREADY_TAKEN));

        PipelineTemplate pipelineTemplate = new PipelineTemplate();
        pipelineTemplate.setAdditionalInfo(payload.getAdditionalInfo());
        pipelineTemplate.setDescription(payload.getDescription());
        pipelineTemplate.setPipelineHistoryDisplayProperties(payload.getPipelineHistoryDisplayProperties());
        pipelineTemplate.setPipelineType(payload.getPipelineType());
        pipelineTemplate.setIntermediateTemplates(payload.getIntermediateTemplate());
        pipelineTemplate.setName(payload.getName());
        pipelineTemplate.setShortName(payload.getShortName());
        pipelineTemplate.setIncludePipelineFields(payload.getIncludePipelineFields());
        pipelineTemplate.setExcludePipelineFields(payload.getExcludePipelineFields());
        pipelineTemplate.setStatus(OperationModes.PREVIEW.toString());
        pipelineTemplate.setMode(payload.getMode());

        pipelineTemplate.setMetadata(metadataUtil.createMetadata(authenticationToken));

        return templateRepository.save(pipelineTemplate);
    }

    public PipelineTemplate updatePipelineTemplate(PipelineTemplateUpdatePayload payload, String authenticationToken) {

        Optional<PipelineTemplate> existingTemplate = templateRepository.findByIdAndIsDeleted(payload.getId(), false);

        if (existingTemplate.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), CustomMessages.PIPELINE_TEMPLATE_NOT_FOUND));

        PipelineTemplate pipelineTemplate = existingTemplate.get();


//        if ((!payload.getName().equals(existingTemplate.get().getName()))
//                || (!payload.getDescription().equals(existingTemplate.get().getDescription()))) {
//            existingTemplate.get().setName(payload.getName());
//            existingTemplate.get().setDescription(payload.getDescription());
//            updateAllValidPipelines(existingTemplate.get());
//        }


        pipelineTemplate.setAdditionalInfo(payload.getAdditionalInfo());
        pipelineTemplate.setPipelineHistoryDisplayProperties(payload.getPipelineHistoryDisplayProperties());
        pipelineTemplate.setIntermediateTemplates(payload.getIntermediateTemplate());
        pipelineTemplate.setPipelineType(payload.getPipelineType());
        pipelineTemplate.setIncludePipelineFields(payload.getIncludePipelineFields());
        pipelineTemplate.setShortName(payload.getShortName());
        pipelineTemplate.setExcludePipelineFields(payload.getExcludePipelineFields());
        pipelineTemplate.setMode(payload.getMode());
        pipelineTemplate.setStatus(payload.getStatus());

        pipelineTemplate.setMetadata(metadataUtil.updateMetadata(pipelineTemplate.getMetadata(), authenticationToken));

        return templateRepository.save(pipelineTemplate);
    }

    public String deleteTemplates(List<String> ids, String authentication) {
        validateTemplates(ids);

        for (String id : ids) {
            templateRepository.deleteAllById(ids);
        }

        return Constants.SUCCESSFULLY_DELETED;
    }

    private void validateTemplates(List<String> ids) {
        for (String id : ids) {
            if (templateRepository.findByIdAndIsDeleted(id, false).isEmpty())
                throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), CustomMessages.PIPELINE_TEMPLATE_NOT_FOUND + id));
        }
    }

    public PipelineTemplate getPipelineTemplate(String id) {
        Optional<PipelineTemplate> pipelineTemplate = templateRepository.findByIdAndIsDeleted(id, false);
        if (pipelineTemplate.isEmpty())
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), CustomMessages.PIPELINE_TEMPLATE_NOT_FOUND));

        return pipelineTemplate.get();
    }

    public Page<PipelineTemplate> getAllPipelineTemplates(Pageable pageable) {
        return templateRepository.findAllByIsDeleted(false ,pageable);
    }

    public Page<PipelineTemplate> getTemplateByName(String name, Pageable pageable) {
        return templateRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
