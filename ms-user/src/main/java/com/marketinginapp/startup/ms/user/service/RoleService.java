package com.marketinginapp.startup.ms.user.service;

import com.marketinginapp.startup.ms.user.api.dto.request.RoleRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse save(RoleRequest request);
    RoleResponse findById(String id);
    List<RoleResponse> findAll();
    RoleResponse update(String id, RoleRequest request);
}
