package com.connection.api.v1.model.common;

import com.connection.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MetadataUtil {

    private final JwtUtils jwtUtils;

    @Autowired
    public MetadataUtil(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public Metadata createMetadata(String authToken) {
        Metadata metadata = new Metadata();
        metadata.setCreatedBy(jwtUtils.getUserIdFromToken(authToken));
        metadata.setCreatorName(jwtUtils.getUsernameFromToken(authToken));
        metadata.setCreatedOn(Instant.now());
        metadata.setModifiedBy(jwtUtils.getUserIdFromToken(authToken));
        metadata.setModifierName(jwtUtils.getUsernameFromToken(authToken));
        metadata.setModifiedOn(Instant.now());
        return metadata;
//        return Metadata.builder()
//                .createdBy(jwtUtils.getUserIdFromToken(authToken))
//                .creatorName(jwtUtils.getUsernameFromToken(authToken))
//                .createdOn(Instant.now())
//                .modifiedBy(jwtUtils.getUserIdFromToken(authToken))
//                .modifierName(jwtUtils.getUsernameFromToken(authToken))
//                .modifiedOn(Instant.now())
//                .build();
    }

    public Metadata updateMetadata(Metadata metadata, String authToken) {
        metadata.setModifiedBy(jwtUtils.getUserIdFromToken(authToken));
        metadata.setModifierName(jwtUtils.getUsernameFromToken(authToken));
        metadata.setModifiedOn(Instant.now());
        return metadata;
    }
}
