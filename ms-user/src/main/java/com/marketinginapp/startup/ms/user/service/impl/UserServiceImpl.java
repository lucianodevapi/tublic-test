package com.marketinginapp.startup.ms.user.service.impl;

import com.marketinginapp.startup.ms.user.api.dto.request.UserRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.UserResponse;
import com.marketinginapp.startup.ms.user.domain.entity.Role;
import com.marketinginapp.startup.ms.user.domain.entity.User;
import com.marketinginapp.startup.ms.user.domain.repository.RoleRepository;
import com.marketinginapp.startup.ms.user.domain.repository.UserRepository;
import com.marketinginapp.startup.ms.user.exception.DuplicatedKeyException;
import com.marketinginapp.startup.ms.user.exception.MessageException;
import com.marketinginapp.startup.ms.user.exception.ObjectNotFoundException;
import com.marketinginapp.startup.ms.user.service.UserService;
import com.marketinginapp.startup.ms.user.utils.Constant;
import com.marketinginapp.startup.ms.user.utils.enums.ConverterEnum;
import com.marketinginapp.startup.ms.user.utils.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public UserResponse save(UserRequest request) {
        var entity = new User();
        try{
            entity = userRepository.save(toEntity(request));
        } catch (Exception exception){
            throw new DuplicatedKeyException(String.format(Constant.EXCEPTION_EMAIL_ALREADY_REGISTERED, request.email()));
        }
        return toResponse(entity);
    }
    @Transactional(readOnly = true)
    @Override
    public UserResponse findById(String id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_USER_NOT_FOUND_BY_ID, id))
        );
        return toResponse(user);
    }
    @Transactional(readOnly = true)
    @Override
    public UserResponse findByEmail(String email) {
        var user = userRepository.findByEmail( email).orElseThrow(
                () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_USER_NOT_FOUND_BY_EMAIL, email))
        );
        return toResponse(user);
    }
    @Transactional(readOnly = true)
    @Override
    public UserResponse findByUsernameOrEmail(String username, String email) {
        var user = userRepository.findFirstByUsernameOrEmailAndStatus(username, email, EStatus.ACTIVE.name()).orElseThrow(
                () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_USER_NOT_FOUND_BY_NAME_OR_EMAIL, username, email))
        );
        return toResponse(user);
    }
    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> findAll() {
        List<UserResponse> list = new ArrayList<>();
        userRepository.findAll().forEach(entity -> {
            list.add(toResponse(entity));
        });
        return list;
    }
    @Transactional
    @Override
    public UserResponse update(String id, UserRequest request) {
        try{
            var user = userRepository.findById(id).orElseThrow(
                    () -> new ObjectNotFoundException(String.format(Constant.EXCEPTION_USER_NOT_FOUND_BY_ID, id))
            );
            if(!request.email().equals(user.getEmail())){
                userRepository.findByEmail(user.getEmail()).orElseThrow(
                        ()-> new ObjectNotFoundException(String.format(Constant.EXCEPTION_USER_NOT_FOUND_BY_EMAIL, user.getEmail()))
                );
            }
            if(!request.username().equals(user.getUsername())){
                user.setUsername(request.username());
            }
            if (!request.email().equals(user.getEmail())){
                user.setEmail(request.email());
            }
            if (!encoder.matches(request.password(), user.getPassword())){
                user.setPassword(encoder.encode(request.password()));
            }
            if (!request.profile().equals(user.getProfile())){
                user.setProfile(request.profile());
            }
            var entity = new User();
            try{
                entity = userRepository.save(user);

            } catch (Exception exception){
                throw new MessageException(String.format(Constant.EXCEPTION_ERROR_WHEN_UPDATING_USER, exception.getMessage()));
            }
            return toResponse(entity);
        } catch (Exception exception){
            throw new MessageException(String.format(Constant.EXCEPTION_ERROR_WHEN_UPDATING_USER, exception.getMessage()));
        }
    }
    private User toEntity(UserRequest request){
        List<Role> list = new ArrayList<>();
        List<String>  roles = new ArrayList<>();
         request.roles().forEach( role -> {
             roles.add(role.toUpperCase());
        });
        roleRepository.findAllByNameIn(roles).forEach(role -> {list.add(role);});
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .profile(request.profile())
                .created(LocalDateTime.now())
                .status(ConverterEnum.toEStatus("active"))
                .roles(list)
                .build();
    }
    private UserResponse toResponse(User entity){
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getProfile(),
                entity.getStatus().name());
    }
}
