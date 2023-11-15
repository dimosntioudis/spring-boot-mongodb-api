package com.certh.spring.data.mongodb.security.services;

import com.certh.spring.data.mongodb.models.Annotation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

  public String findLoggedInUser() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String userId = ((UserDetailsImpl) principle).getId();

    return userId;
  }

  public Annotation createAnnotation(Annotation annotation, String userId) {
    Annotation _annotation = new Annotation();

    _annotation.setUserId(userId);
    _annotation.setRectangle(annotation.getRectangle());
    _annotation.setFrameNumber(annotation.getFrameNumber());
    _annotation.setSecond(annotation.getSecond());
    _annotation.setDescription(annotation.getDescription());
    _annotation.setDropdownValue(annotation.getDropdownValue());
    _annotation.setVideoId(annotation.getVideoId());
    _annotation.setComment(annotation.getComment());
    _annotation.setEvaluation(annotation.isEvaluation());
    _annotation.setColor(annotation.getColor());

    return _annotation;
  }
}
