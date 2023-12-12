package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.responses.JobPostingViewOverCandidateDto;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Like;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.LikeRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.LikeService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.capstoneproject.enums.BasicStatus.ACTIVE;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    PrettyTime prettyTime;

    @Override
    public String likeJobPosting(Integer userId, Integer jobPostingId) {
        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findById(jobPostingId);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                Optional<Like> likeOptional = likeRepository.findByUser_IdAndJobPosting_Id(users.getId(), jobPosting.getId());
                if(likeOptional.isPresent()){
                    Like like = likeOptional.get();
                    likeRepository.delete(like);
                    return "Unliked";
                }else {
                    Like like = new Like();
                    like.setUser(users);
                    like.setJobPosting(jobPosting);
                    likeRepository.save(like);
                    return "Liked";
                }
            }else{
                throw new BadRequestException("Job Posting ID not found.");
            }
        }else{
            throw new BadRequestException("User ID not found.");
        }
    }

    @Override
    public List<JobPostingViewOverCandidateDto> getJobPostingLiked(Integer userId) {
        List<JobPostingViewOverCandidateDto> jobPostingLike = new ArrayList<>();
        List<Like> likes = likeRepository.findAllByUser_Id(userId);
        if(likes!=null){
            for(Like likeG: likes){
                Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndShareAndStatusAndBanIsFalse(likeG.getJobPosting().getId(),StatusReview.Published, ACTIVE);
                    if(jobPostingOptional.isPresent()){
                        JobPostingViewOverCandidateDto jobPostingLikeAdd = new JobPostingViewOverCandidateDto();
                        JobPosting jobPosting = jobPostingOptional.get();
                        jobPostingLikeAdd.setId(jobPosting.getId());
                        jobPostingLikeAdd.setTitle(jobPosting.getTitle());
                        jobPostingLikeAdd.setCompanyName(jobPosting.getCompanyName());
                        jobPostingLikeAdd.setAvatar(jobPosting.getAvatar());
                        jobPostingLikeAdd.setLocation(jobPosting.getLocation());
                        jobPostingLikeAdd.setSkill(jobPosting.getSkill().split(","));
                        jobPostingLikeAdd.setSalary(jobPosting.getSalary());
                        jobPostingLikeAdd.setCreateDate(prettyTime.format(jobPosting.getCreateDate()));
                        jobPostingLike.add(jobPostingLikeAdd);
                    }

            }
            return jobPostingLike;
        }else{
            throw new BadRequestException("Currently, the system cannot find the job postings you have liked.");
        }
    }

}
