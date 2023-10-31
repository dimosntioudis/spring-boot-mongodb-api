package com.certh.spring.data.mongodb.repository;

import com.certh.spring.data.mongodb.models.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {

}
