package com.marketinginapp.startup.ms.user.domain.repository;

import com.marketinginapp.startup.ms.user.domain.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    List<Role> findAllByNameIn(List<String> names);
    Optional<Role> findByName(String name);
}
