package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.models.ERole;
import com.certh.spring.data.mongodb.models.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}

