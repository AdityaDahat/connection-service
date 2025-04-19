package com.connection.api.v1.service.connector.objects;

import com.connection.api.v1.model.response.ApiResponse;
import com.connection.api.v1.payload.connector.objects.PipelineSetupPayload;
import com.connection.constants.Constants;
import com.connection.constants.CustomMessages;
import com.connection.exception.ApiException;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    private final BatchPipelineService batchPipelineService;
    private final RealTimePipelineService realTimePipelineService;

    @Autowired
    public PipelineService(BatchPipelineService batchPipelineService, RealTimePipelineService realTimePipelineService) {
        this.batchPipelineService = batchPipelineService;
        this.realTimePipelineService = realTimePipelineService;
    }

    public Object setupPipeline(PipelineSetupPayload payload, String authenticationToken) {

        if (Constants.PipelineType.BATCH.equalsIgnoreCase(payload.getPipelineType()))
            return batchPipelineService.setupBatchPipeline(payload, authenticationToken);
        else if (Constants.PipelineType.REAL_TIME.equalsIgnoreCase(payload.getPipelineType()))
            return realTimePipelineService.setupRealTimePipeline(payload, authenticationToken);
        else
            throw new ApiException(new ApiResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    CustomMessages.INVALID_PIPELINE_TYPE));

    }
}
