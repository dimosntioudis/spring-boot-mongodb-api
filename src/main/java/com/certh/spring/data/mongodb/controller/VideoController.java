package com.certh.spring.data.mongodb.controller;

import com.certh.spring.data.mongodb.model.Video;
import com.certh.spring.data.mongodb.repository.VideoRepository;
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
public class VideoController {

  @Autowired
  private VideoRepository videoRepository;

  @GetMapping("/videos")
  public ResponseEntity<List<Video>> getAllVideos() {
    try {
      List<Video> videos = new ArrayList<Video>();

      videoRepository.findAll().forEach(videos::add);

      if (videos.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(videos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/videos/{id}")
  public ResponseEntity<Video> getVideoById(@PathVariable("id") String id) {
    Optional<Video> videoData = videoRepository.findById(id);

    if (videoData.isPresent()) {
      return new ResponseEntity<>(videoData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/videos")
  public ResponseEntity<Video> createVideo(@RequestBody Video video) {
    try {
      Video _videos = videoRepository.save(
          new Video(video.getTitle(), video.getDescription(), video.getFilename(), video.getPath(),
              video.getCreatedAt()));
      return new ResponseEntity<>(_videos, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/videos/{id}")
  public ResponseEntity<Video> updateVideo(@PathVariable("id") String id,
      @RequestBody Video video) {
    Optional<Video> videoData = videoRepository.findById(id);

    if (videoData.isPresent()) {
      Video _video = videoData.get();
      _video.setTitle(video.getTitle());
      _video.setDescription(video.getDescription());

      return new ResponseEntity<>(videoRepository.save(_video), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/videos/{id}")
  public ResponseEntity<HttpStatus> deleteVideo(@PathVariable("id") String id) {
    try {
      videoRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/videos")
  public ResponseEntity<HttpStatus> deleteAllVideos() {
    try {
      videoRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

