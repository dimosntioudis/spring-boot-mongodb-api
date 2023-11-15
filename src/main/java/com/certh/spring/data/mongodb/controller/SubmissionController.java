package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.exception.ResourceNotFoundException;
import com.certh.spring.data.mongodb.exception.ResourceOwnershipException;
import com.certh.spring.data.mongodb.models.Annotation;
import com.certh.spring.data.mongodb.models.Submission;
import com.certh.spring.data.mongodb.repository.AnnotationRepository;
import com.certh.spring.data.mongodb.repository.SubmissionRepository;
import com.certh.spring.data.mongodb.security.services.SubmissionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class SubmissionController {

  @Autowired
  private SubmissionRepository submissionRepository;

  @Autowired
  private SubmissionService submissionService;

  @Autowired
  private AnnotationRepository annotationRepository;

  @GetMapping("/submissions")
  public ResponseEntity<List<Submission>> getAllSubmissions(
      @RequestParam(required = false) String status) {
    Collection<?> authorities = submissionService.findLoggedInUserRole();

    try {
      List<Submission> submissions = new ArrayList<>();

      if (authorities.toString().contains("ROLE_TRAINEE")) {
        String userId = submissionService.findLoggedInUserId();
        submissionRepository.findByUserId(userId).forEach(submissions::add);
      } else {
        if (status == null) {
          submissions = submissionService.findSubmissionsByTrainerCountry();
        } else {
          submissions = submissionService.findSubmissionsByTrainerCountryAndStatus(status);
        }
      }

      if (submissions.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        for (Submission submission : submissions) {
          int sum = 0;
          int count = submission.getAnnotationIds().size();
          if ("Completed".equals(submission.getStatus())) {
            for (String id : submission.getAnnotationIds()) {
              Optional<Annotation> annotation = annotationRepository.findById(id);
              if (annotation.isPresent()) {
                Boolean evaluation = annotation.get().isEvaluation();
                if (evaluation) {
                  sum += 1;
                }
              }
            }
            submission.setEvaluation(sum + "/" + count);
          } else {
            submission.setEvaluation("Pending");
          }
        }
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

  @PutMapping("/submissions/{id}")
  public ResponseEntity<Submission> updateSubmission(@PathVariable("id") String id,
      @RequestBody Submission submission) {
    Submission _submission = submissionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

    _submission.setStatus(submission.getStatus());

    return new ResponseEntity<>(submissionRepository.save(_submission), HttpStatus.OK);
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
