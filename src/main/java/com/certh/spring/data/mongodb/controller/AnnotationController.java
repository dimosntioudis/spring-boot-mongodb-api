package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.exception.ResourceNotFoundException;
import com.certh.spring.data.mongodb.exception.ResourceOwnershipException;
import com.certh.spring.data.mongodb.models.Annotation;
import com.certh.spring.data.mongodb.repository.AnnotationRepository;
import com.certh.spring.data.mongodb.security.services.AnnotationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AnnotationController {

  private final AnnotationRepository annotationRepository;
  private final AnnotationService annotationService;

  @Autowired
  public AnnotationController(AnnotationRepository annotationRepository,
      AnnotationService annotationService) {
    this.annotationRepository = annotationRepository;
    this.annotationService = annotationService;
  }

  @GetMapping("/annotations")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<List<Annotation>> getAllAnnotations(
      @RequestParam() String videoId) {

    String userId = annotationService.findLoggedInUser();

    try {
      List<Annotation> annotations = new ArrayList<>();

      Iterable<Annotation> iterable = annotationRepository.findByVideoIdAndUserId(videoId,
          userId);

      for (Annotation annotation : iterable) {
        annotations.add(annotation);
      }

      if (annotations.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(annotations, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/annotations/{id}")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<Annotation> getAnnotationById(@PathVariable("id") String id) {
    String userId = annotationService.findLoggedInUser();

    Annotation _annotation = annotationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

    if (!_annotation.getUserId().contentEquals(userId)) {
      throw new ResourceOwnershipException(
          "You do not have permission to access Annotation with id = " + id);
    }

    return new ResponseEntity<>(_annotation, HttpStatus.OK);
  }

  @PostMapping("/annotations")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<Annotation> createAnnotation(@RequestBody Annotation annotation) {
    try {
      Annotation _annotation = annotationService.createAnnotation(annotation);
      _annotation = annotationRepository.save(_annotation);
      return new ResponseEntity<>(_annotation, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/annotations/{id}")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<Annotation> updateAnnotation(@PathVariable("id") String id,
      @RequestBody Annotation annotation) {
    String userId = annotationService.findLoggedInUser();

    Annotation _annotation = annotationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

    if (!_annotation.getUserId().contentEquals(userId)) {
      throw new ResourceOwnershipException(
          "You do not have permission to modify Annotation with id = " + id);
    }

    _annotation.setRectangle(annotation.getRectangle());
    _annotation.setFrameNumber(annotation.getFrameNumber());
    _annotation.setSecond(annotation.getSecond());
    _annotation.setDescription(annotation.getDescription());
    _annotation.setDropdownValue(annotation.getDropdownValue());
    _annotation.setVideoId(annotation.getVideoId());
    _annotation.setUserId(userId);

    return new ResponseEntity<>(annotationRepository.save(_annotation), HttpStatus.OK);
  }

  @DeleteMapping("/annotations/{id}")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteAnnotation(@PathVariable("id") String id) {
    String userId = annotationService.findLoggedInUser();

    Annotation _annotation = annotationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

    if (!_annotation.getUserId().contentEquals(userId)) {
      throw new ResourceOwnershipException(
          "You do not have permission to modify Annotation with id = " + id);
    }

    try {
      annotationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/annotations")
  @PreAuthorize("hasRole('TRAINER') or hasRole('TRAINEE') or hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteAllAnnotations(@RequestParam(required = false) String videoId) {
    String userId = annotationService.findLoggedInUser();

    try {
      if (videoId == null) {
        annotationRepository.deleteByUserId(userId);
      } else {
        annotationRepository.deleteByUserIdAndVideoId(userId, videoId);
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
