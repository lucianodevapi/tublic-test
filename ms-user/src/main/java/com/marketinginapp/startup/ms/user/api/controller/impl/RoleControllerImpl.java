package com.marketinginapp.startup.ms.user.api.controller.impl;

import com.marketinginapp.startup.ms.user.api.controller.RoleController;
import com.marketinginapp.startup.ms.user.api.dto.request.RoleRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.RoleResponse;
import com.marketinginapp.startup.ms.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/v1/role")
public class RoleControllerImpl implements RoleController {

    private final RoleService service;

    @Override
    public ResponseEntity<RoleResponse> save(RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @Override
    public ResponseEntity<RoleResponse> findById(String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @Override
    public ResponseEntity<List<RoleResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @Override
    public ResponseEntity<RoleResponse> update(String id, RoleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, request));
    }
}
