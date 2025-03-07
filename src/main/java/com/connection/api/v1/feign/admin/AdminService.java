package com.connection.api.v1.feign.admin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("ADMIN-SERVICE")
public interface AdminService {

    @GetMapping("admin/api/v1/project/list")
    public ResponseEntity<Object> getAllProjects(@RequestHeader("Authorization") String token);


}
