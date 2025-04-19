package com.connection.api.v1.feign.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "ADMIN-SERVICE", path = "admin/api/v1")
public interface AdminService {

    // Auth
    @GetMapping("/auth/evaluate-permission")
    public ResponseEntity<Object> evaluatePermission(@RequestBody Map<String, String> request);

    @GetMapping("/project/list")
    public ResponseEntity<Object> getAllProjects(@RequestHeader("Authorization") String token);

    @GetMapping("/project/{id}")
    ResponseEntity<Object> getProject(@PathVariable String id, @RequestHeader("Authorization") String token);


}
