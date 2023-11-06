package com.certh.spring.data.mongodb.models;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "submissions")
public class Submission {
  @Id
  private String id;

  private String userId;
  private String username;

  private String firstName;
  private String lastName;
  private String country;

  private String videoId;
  private String videoTitle;

  private String status;

  private List<String> annotationIds;

  public Submission() {

  }

  public Submission(String userId, String username, String videoId, String videoTitle,
      List<String> annotationIds, String status, String firstName, String lastName, String country) {
    this.userId = userId;
    this.username = username;
    this.videoId = videoId;
    this.videoTitle = videoTitle;
    this.annotationIds = annotationIds;
    this.status = status;
    this.firstName = firstName;
    this.lastName = lastName;
    this.country = country;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getVideoTitle() {
    return videoTitle;
  }

  public void setVideoTitle(String videoTitle) {
    this.videoTitle = videoTitle;
  }

  public List<String> getAnnotationIds() {
    return annotationIds;
  }

  public void setAnnotationIds(List<String> annotationId) {
    this.annotationIds = annotationId;
  }
}
