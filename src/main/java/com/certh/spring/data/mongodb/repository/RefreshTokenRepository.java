package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.models.RefreshToken;
import com.certh.spring.data.mongodb.models.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  int deleteByUser(User user);
}
