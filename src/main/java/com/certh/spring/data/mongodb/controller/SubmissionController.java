package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.exception.ResourceNotFoundException;
import com.certh.spring.data.mongodb.exception.ResourceOwnershipException;
import com.certh.spring.data.mongodb.models.Submission;
import com.certh.spring.data.mongodb.repository.SubmissionRepository;
import com.certh.spring.data.mongodb.security.services.SubmissionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:63342", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/test")
public class SubmissionController {

  @Autowired
  private SubmissionRepository submissionRepository;

  @Autowired
  private SubmissionService submissionService;

  @GetMapping("/submissions")
  public ResponseEntity<List<Submission>> getAllSubmissions() {
    Collection<?> authorities = submissionService.findLoggedInUserRole();

    try {
      List<Submission> submissions = new ArrayList<>();

      if (authorities.toString().contains("ROLE_TRAINER")) {
        String userId = submissionService.findLoggedInUserId();
        submissionRepository.findByUserId(userId).forEach(submissions::add);
      } else {
        submissionRepository.findAll().forEach(submissions::add);
      }

      if (submissions.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(submissions, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/submissions/{id}")
  public ResponseEntity<Submission> getSubmissionById(@PathVariable("id") String id) {
    Optional<Submission> submissionData = submissionRepository.findById(id);

    if (submissionData.isPresent()) {
      return new ResponseEntity<>(submissionData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/submissions")
  public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission) {
    try {
      Submission _submission = submissionService.createSubmission(submission);
      _submission = submissionRepository.save(_submission);
      return new ResponseEntity<>(_submission, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/submissions/{id}")
  public ResponseEntity<HttpStatus> deleteSubmission(@PathVariable("id") String id) {
    String userId = submissionService.findLoggedInUserId();

    Submission _submission = submissionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Submission with id = " + id));

    if (!_submission.getUserId().contentEquals(userId)) {
      throw new ResourceOwnershipException(
          "You do not have permission to modify Submission with id = " + id);
    }

    try {
      submissionRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/submissions")
  public ResponseEntity<HttpStatus> deleteAllSubmissions() {
    String userId = submissionService.findLoggedInUserId();

    try {
      submissionRepository.deleteByUserId(userId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
