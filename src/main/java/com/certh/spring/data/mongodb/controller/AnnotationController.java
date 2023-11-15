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

@CrossOrigin(origins = "http://localhost:63342", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/test")
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
  public ResponseEntity<List<Annotation>> getAllAnnotations(
      @RequestParam() String videoId, @RequestParam(required = false) String id) {

    String userId;
    if (id == null) {
      userId = annotationService.findLoggedInUser();
    } else {
      userId = id;
    }

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
  public ResponseEntity<List<Annotation>> createAnnotation(
      @RequestBody List<Annotation> annotations, @RequestParam(required = false) String id) {

    String userId;
    if (id == null) {
      userId = annotationService.findLoggedInUser();
    } else {
      userId = id;
    }

    try {
      List<Annotation> createdAnnotations = new ArrayList<>();

      for (Annotation annotation : annotations) {
        Annotation createdAnnotation = annotationService.createAnnotation(annotation, userId);
        createdAnnotation = annotationRepository.save(createdAnnotation);
        createdAnnotations.add(createdAnnotation);
      }

      return new ResponseEntity<>(createdAnnotations, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/annotations/{id}")
  public ResponseEntity<Annotation> updateAnnotation(@PathVariable("id") String id,
      @RequestBody Annotation annotation) {
    String userId = annotationService.findLoggedInUser();

    Annotation _annotation = annotationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

//    if (!_annotation.getUserId().contentEquals(userId)) {
//      throw new ResourceOwnershipException(
//          "You do not have permission to modify Annotation with id = " + id);
//    }

    _annotation.setDescription(annotation.getDescription());
    _annotation.setDropdownValue(annotation.getDropdownValue());
    _annotation.setComment(annotation.getComment());
    _annotation.setEvaluation(annotation.isEvaluation());

    return new ResponseEntity<>(annotationRepository.save(_annotation), HttpStatus.OK);
  }

  @DeleteMapping("/annotations/{id}")
  public ResponseEntity<HttpStatus> deleteAnnotation(@PathVariable("id") String id) {
//    String userId = annotationService.findLoggedInUser();

    Annotation _annotation = annotationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Annotation with id = " + id));

//    Added comment so that the trainer can also delete an annotation.
//    if (!_annotation.getUserId().contentEquals(userId)) {
//      throw new ResourceOwnershipException(
//          "You do not have permission to modify Annotation with id = " + id);
//    }

    try {
      annotationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/annotations")
  public ResponseEntity<HttpStatus> deleteAllAnnotations(
      @RequestParam(required = false) String videoId) {
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
