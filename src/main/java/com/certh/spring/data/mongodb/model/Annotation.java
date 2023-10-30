package com.certh.spring.data.mongodb.model;

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

  public Annotation() {

  }

  public Annotation(Rectangle rectangle, Long frameNumber, Double second,
      String description, String dropdownValue) {
    this.rectangle = rectangle;
    this.frameNumber = frameNumber;
    this.second = second;
    this.description = description;
    this.dropdownValue = dropdownValue;
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
