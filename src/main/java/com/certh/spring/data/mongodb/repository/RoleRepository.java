package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.model.ERole;
import com.certh.spring.data.mongodb.model.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}

