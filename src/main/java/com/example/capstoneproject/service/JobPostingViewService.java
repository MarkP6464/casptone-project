package com.example.capstoneproject.service;

import org.springframework.stereotype.Service;

@Service
public interface JobPostingViewService {
    void createJobPostingView(Integer userId, Integer postingId);
}
