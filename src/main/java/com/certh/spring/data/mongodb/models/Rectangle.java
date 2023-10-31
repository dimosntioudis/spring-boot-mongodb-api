package com.certh.spring.data.mongodb.models;

public class Rectangle {
  private Long x;
  private Long y;
  private Long width;
  private Long height;

  public Rectangle() {

  }

  public Rectangle(Long x, Long y, Long width, Long height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Long getX() {
    return x;
  }

  public void setX(Long x) {
    this.x = x;
  }

  public Long getY() {
    return y;
  }

  public void setY(Long y) {
    this.y = y;
  }

  public Long getWidth() {
    return width;
  }

  public void setWidth(Long width) {
    this.width = width;
  }

  public Long getHeight() {
    return height;
  }

  public void setHeight(Long height) {
    this.height = height;
  }
}
