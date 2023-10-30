package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.model.Annotation;
import com.certh.spring.data.mongodb.repository.AnnotationRepository;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AnnotationController {

  @Autowired
  AnnotationRepository annotationRepository;

  @GetMapping("/annotations")
  public ResponseEntity<List<Annotation>> getAllAnnotations() {
    try {
      List<Annotation> annotations = new ArrayList<Annotation>();

      annotationRepository.findAll().forEach(annotations::add);

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
    Optional<Annotation> annotationData = annotationRepository.findById(id);

    if (annotationData.isPresent()) {
      return new ResponseEntity<>(annotationData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/annotations")
  public ResponseEntity<Annotation> createAnnotation(@RequestBody Annotation annotation) {
    try {
      Annotation _annotations = annotationRepository.save(
          new Annotation(annotation.getRectangle(), annotation.getFrameNumber(),
              annotation.getSecond(), annotation.getDescription(), annotation.getDropdownValue(), annotation.getVideoId()));
      return new ResponseEntity<>(_annotations, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/annotations/{id}")
  public ResponseEntity<Annotation> updateAnnotation(@PathVariable("id") String id,
      @RequestBody Annotation annotation) {
    Optional<Annotation> annotationData = annotationRepository.findById(id);

    if (annotationData.isPresent()) {
      Annotation _annotation = annotationData.get();
      _annotation.setRectangle(annotation.getRectangle());
      _annotation.setFrameNumber(annotation.getFrameNumber());
      _annotation.setSecond(annotation.getSecond());
      _annotation.setDescription(annotation.getDescription());
      _annotation.setDropdownValue(annotation.getDropdownValue());

      return new ResponseEntity<>(annotationRepository.save(_annotation), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/annotations/{id}")
  public ResponseEntity<HttpStatus> deleteAnnotation(@PathVariable("id") String id) {
    try {
      annotationRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/annotations")
  public ResponseEntity<HttpStatus> deleteAllAnnotations() {
    try {
      annotationRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
