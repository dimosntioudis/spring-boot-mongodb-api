package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.models.Annotation;
import com.certh.spring.data.mongodb.models.Role;
import com.certh.spring.data.mongodb.models.User;
import com.certh.spring.data.mongodb.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<User>> getAllUsers() {

    try {
      List<User> users = new ArrayList<>();

      Iterable<User> iterable = userRepository.findAll();

      for (User user : iterable) {
        users.add(user);
      }

      if (users.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
