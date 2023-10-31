package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.model.Annotation;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnotationRepository extends MongoRepository<Annotation, String> {
  List<Annotation> findByVideoIdAndUserId(String videoId, String userId);
}
