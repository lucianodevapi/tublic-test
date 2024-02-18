package com.marketinginapp.startup.ms.user.api.controller.impl;

import com.marketinginapp.startup.ms.user.api.controller.UserController;
import com.marketinginapp.startup.ms.user.api.dto.request.UserRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.UserResponse;
import com.marketinginapp.startup.ms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Override
    public ResponseEntity<UserResponse> save(UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @Override
    public ResponseEntity<UserResponse> findById(String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @Override
    public ResponseEntity<UserResponse> findByEmail(String email) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByEmail(email));
    }

    @Override
    public ResponseEntity<UserResponse> findByUsernameOrEmail(String name, String email) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByUsernameOrEmail(name, email));
    }

    @Override
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @Override
    public ResponseEntity<UserResponse> update(String id, UserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, request));
    }
}
