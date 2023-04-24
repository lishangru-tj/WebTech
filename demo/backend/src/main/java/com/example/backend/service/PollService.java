package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.Activity;
import com.example.backend.entity.Poll;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public interface PollService {
  List<Poll> findPoll(Long activity_id);
  Long createPoll(Long activity_id, String topic_text, LocalDateTime deadline,boolean multiple_choice);

  Integer deletePoll(Long pollID);
}
