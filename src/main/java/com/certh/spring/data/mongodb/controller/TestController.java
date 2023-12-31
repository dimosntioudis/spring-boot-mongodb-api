package com.certh.spring.data.mongodb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/trainer")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public String userAccess() {
    return "Trainer Content.";
  }

  @GetMapping("/trainee")
  @PreAuthorize("hasRole('TRAINEE')")
  public String trainerAccess() {
    return "Trainee Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
}
