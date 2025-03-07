package com.connection.api.v1.controller.connector.objects.connections.google;

import com.connection.api.v1.service.connector.connections.google.GoogleAds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/connector/google/ads")
public class GoogleAdsController {
    private final GoogleAds googleAdwords;

    public GoogleAdsController(GoogleAds googleAdwords) {
        this.googleAdwords = googleAdwords;
    }

    @GetMapping("/customers")
    public ResponseEntity<Object> getCustomerIds(@RequestHeader("connection-token") String connectionToken) {
        return ResponseEntity.status(HttpStatus.OK).body(
                googleAdwords.getAccountIds(connectionToken));
    }

    @GetMapping("/customer/{project-id}/{customer-id}")
    public ResponseEntity<Object> isCustomerIdInUse(@PathVariable("project-id") String projectId,
                                                    @PathVariable("customer-id") long customerId) {
        googleAdwords.isCustomerIdInUse(customerId, projectId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
