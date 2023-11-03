package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.models.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

  Iterable<Submission> findByVideoIdAndUserId(String videoId, String userId);

  Iterable<Submission> findByUserId(String userId);

  void deleteByUserId(String userId);
}
