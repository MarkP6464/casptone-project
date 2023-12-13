package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.JobPostingView;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.JobPostingViewRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.JobPostingViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class JobPostingViewServiceImpl implements JobPostingViewService {
    @Autowired
    JobPostingViewRepository jobPostingViewRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Override
    public void createJobPostingView(Integer userId, Integer postingId) {
        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatus(postingId, BasicStatus.ACTIVE);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                Optional<JobPostingView> jobPostingViewOptional = jobPostingViewRepository.findByUser_IdAndJobPosting_Id(userId,postingId);
                if(jobPostingViewOptional.isEmpty()){
                    JobPostingView jobPostingView = new JobPostingView();
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    jobPostingView.setUser(users);
                    jobPostingView.setJobPosting(jobPosting);
                    jobPostingView.setViewDateTime(currentTimestamp);
                    jobPostingViewRepository.save(jobPostingView);
                }
            }
        }
    }
}
