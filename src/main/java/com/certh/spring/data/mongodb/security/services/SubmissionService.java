package com.certh.spring.data.mongodb.security.services;

import com.certh.spring.data.mongodb.models.Annotation;
import com.certh.spring.data.mongodb.models.Submission;
import com.certh.spring.data.mongodb.models.User;
import com.certh.spring.data.mongodb.models.Video;
import com.certh.spring.data.mongodb.repository.AnnotationRepository;
import com.certh.spring.data.mongodb.repository.SubmissionRepository;
import com.certh.spring.data.mongodb.repository.UserRepository;
import com.certh.spring.data.mongodb.repository.VideoRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private SubmissionRepository submissionRepository;

  @Autowired
  private AnnotationRepository annotationRepository;

  @Autowired
  private UserRepository userRepository;

  public String findLoggedInUserId() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String userId = ((UserDetailsImpl) principle).getId();

    return userId;
  }

  public Collection<?> findLoggedInUserRole() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Collection<?> authorities = ((UserDetailsImpl) principle).getAuthorities();

    return authorities;
  }

  public String findLoggedInUsername() {
    Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = ((UserDetailsImpl) principle).getUsername();

    return username;
  }

  /* Return Submissions based on Trainer's country */
  public List<Submission> findSubmissionsByTrainerCountry() {
    String userId = findLoggedInUserId();
    Optional<User> user = userRepository.findById(userId);

    List<Submission> submissions = new ArrayList<>();
    submissionRepository.findByCountry(user.get().getCountry()).forEach(submissions::add);

    return submissions;
  }

  public Submission createSubmission(Submission submission) {

    String userId = findLoggedInUserId();
    String username = findLoggedInUsername();

    Optional<User> userData = userRepository.findByUsername(username);
    String firstName = userData.get().getFirstname();
    String lastName = userData.get().getLastname();
    String country = userData.get().getCountry();

    Optional<Video> videoData = videoRepository.findById(submission.getVideoId());

    List<String> annotations = new ArrayList<>();
    Iterable<Annotation> iterable = annotationRepository.findByVideoIdAndUserId(submission.getVideoId(),
        userId);

    for (Annotation annotation : iterable) {
      annotations.add(annotation.getId());
    }

    Iterable<Submission> iterableSub = submissionRepository.findByVideoIdAndUserId(submission.getVideoId(), userId);

    for (Submission sub : iterableSub) {
      submissionRepository.deleteById(sub.getId());
    }

    Submission _submission = new Submission();

    _submission.setUserId(userId);
    _submission.setUsername(username);
    _submission.setFirstName(firstName);
    _submission.setLastName(lastName);
    _submission.setCountry(country);

    _submission.setVideoId(submission.getVideoId());
    _submission.setVideoTitle(videoData.get().getTitle());
    _submission.setAnnotationIds(annotations);
    _submission.setStatus("Submitted");

    return _submission;
  }
}
