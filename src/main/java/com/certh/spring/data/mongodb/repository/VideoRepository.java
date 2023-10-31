package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {

}
