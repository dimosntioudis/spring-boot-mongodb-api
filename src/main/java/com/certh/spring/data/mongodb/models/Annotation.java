package com.certh.spring.data.mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "annotations")
public class Annotation {
  @Id
  private String id;

  private Rectangle rectangle;
  private Long frameNumber;
  private Double second;
  private String description;
  private String dropdownValue;
  private String videoId;
  private String userId;

  /* The score of the trainer */
  private boolean evaluation;
  private String comment = "";

  /* The color of the annotation */
  private String color = "red";

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public boolean isEvaluation() {
    return evaluation;
  }

  public void setEvaluation(boolean evaluation) {
    this.evaluation = evaluation;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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

  public Rectangle getRectangle() {
    return rectangle;
  }

  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  public Long getFrameNumber() {
    return frameNumber;
  }

  public void setFrameNumber(Long frameNumber) {
    this.frameNumber = frameNumber;
  }

  public Double getSecond() {
    return second;
  }

  public void setSecond(Double second) {
    this.second = second;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDropdownValue() {
    return dropdownValue;
  }

  public void setDropdownValue(String dropdownValue) {
    this.dropdownValue = dropdownValue;
  }
}
