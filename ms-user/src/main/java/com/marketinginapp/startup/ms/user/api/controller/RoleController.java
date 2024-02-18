package com.marketinginapp.startup.ms.user.api.controller;

import com.marketinginapp.startup.ms.user.api.dto.request.RoleRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.RoleResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RoleController {

    @PostMapping(value = "/save")
    ResponseEntity<RoleResponse> save(@Valid @RequestBody RoleRequest request);

    @GetMapping(value = "/findById/{id}")
    ResponseEntity<RoleResponse> findById(@PathVariable String id);

    @GetMapping(value = "/findAll")
    ResponseEntity<List<RoleResponse>> findAll();

    @PutMapping(value = "/update/{id}")
    ResponseEntity<RoleResponse> update(@PathVariable String id, @Valid @RequestBody RoleRequest request);
}
