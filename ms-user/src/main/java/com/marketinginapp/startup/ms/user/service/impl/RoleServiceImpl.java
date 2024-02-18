package com.marketinginapp.startup.ms.user.service.impl;

import com.marketinginapp.startup.ms.user.api.dto.request.RoleRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.RoleResponse;
import com.marketinginapp.startup.ms.user.domain.entity.Role;
import com.marketinginapp.startup.ms.user.domain.repository.RoleRepository;
import com.marketinginapp.startup.ms.user.exception.DuplicatedKeyException;
import com.marketinginapp.startup.ms.user.exception.MessageException;
import com.marketinginapp.startup.ms.user.exception.ObjectNotFoundException;
import com.marketinginapp.startup.ms.user.service.RoleService;
import com.marketinginapp.startup.ms.user.utils.Constant;
import com.marketinginapp.startup.ms.user.utils.enums.ConverterEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    @Transactional
    @Override
    public RoleResponse save(RoleRequest request) {
        var entity = new Role();
        try{
            Optional<Role> o = repository.findByName(ConverterEnum.toERole(request.name()).toString());
            if(o.isPresent()){
                throw new DuplicatedKeyException(String.format(Constant.EXCEPTION_ROLE_ALREADY_REGISTERED, request.name()));
            }
            entity = repository.save(toEntity(request));
        } catch (Exception exception){
            throw new MessageException(String.format(Constant.EXCEPTION_MESSAGE, exception.getMessage()));
        }
        return toResponse(entity);
    }
    @Transactional(readOnly = true)
    @Override
    public RoleResponse findById(String id) {
        var entity = repository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_ROLE_NOT_FOUND_BY_ID, id))
        );
        return toResponse(entity);
    }
    @Transactional(readOnly = true)
    @Override
    public List<RoleResponse> findAll() {
        List<RoleResponse> list = new ArrayList<>();
        repository.findAll().forEach(role -> {
            list.add(toResponse(role));
        });
        return list;
    }
    @Transactional
    @Override
    public RoleResponse update(String id, RoleRequest request) {
        var entity = repository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_ROLE_NOT_FOUND_BY_ID, id))
        );
        entity.setName(request.name());
        var role = new Role();
        try{
            entity = repository.save(entity);
        } catch (Exception exception){
            throw new MessageException(String.format(Constant.EXCEPTION_ERROR_WHEN_UPDATING_ROLE, exception.getMessage()));
        }
        return toResponse(entity);
    }
    private Role toEntity(RoleRequest request){
        return Role.builder().name(ConverterEnum.toERole(request.name()).toString()).build();
    }
    private RoleResponse toResponse(Role entity){
        return new RoleResponse(entity.getId(), entity.getName().toLowerCase());
    }
}
