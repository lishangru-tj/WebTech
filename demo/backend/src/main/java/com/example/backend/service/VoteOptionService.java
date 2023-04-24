package com.example.backend.service;

import com.example.backend.common.Result;
import com.example.backend.entity.VoteOption;
import com.example.backend.entity.options;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VoteOptionService {
  List<VoteOption> findVoteOption(Long poll_id);
  int UpdateVotes(Long option_id);
  Result<String> createVoteOptions(List<options> voteOptions, Long poll_id);
}
