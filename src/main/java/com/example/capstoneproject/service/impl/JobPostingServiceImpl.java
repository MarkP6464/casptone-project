package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CvRepository cvRepository;
    @Override
    public boolean create(Integer userId, JobPostingDto dto) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(userId, RoleType.HR);
        JobPosting jobPosting = new JobPosting();
        LocalDate currentDate = LocalDate.now();
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            jobPosting.setTitle(dto.getTitle());
            jobPosting.setWorkingType(dto.getWorkingType());
            jobPosting.setLocation(dto.getLocation());
            jobPosting.setDescription(dto.getRequirement());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setCreateDate(currentDate);
            jobPosting.setStatus(BasicStatus.ACTIVE);
            jobPosting.setShare(BasicStatus.PRIVATE);
            jobPosting.setUser(users);
            jobPostingRepository.save(jobPosting);
            return true;

        }
        return false;
    }

    @Override
    public boolean update(Integer hrId, Integer jobPostingId, JobPostingDto dto) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(hrId, RoleType.HR);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, BasicStatus.ACTIVE);
        LocalDate currentDate = LocalDate.now();
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            if (jobPostingOptional.isPresent()) {
                JobPosting jobPosting = jobPostingOptional.get();
                if (dto.getTitle() != null && !dto.getTitle().equals(jobPosting.getTitle())) {
                    jobPosting.setTitle(dto.getTitle());
                }
                if (dto.getWorkingType() != null && !dto.getWorkingType().equals(jobPosting.getWorkingType())) {
                    jobPosting.setWorkingType(dto.getWorkingType());
                }
                if (dto.getLocation() != null && !dto.getLocation().equals(jobPosting.getLocation())) {
                    jobPosting.setLocation(dto.getLocation());
                }
                if (dto.getRequirement() != null && !dto.getRequirement().equals(jobPosting.getDescription())) {
                    jobPosting.setDescription(dto.getRequirement());
                }
                if (dto.getSalary() != null && !dto.getSalary().equals(jobPosting.getSalary())) {
                    jobPosting.setSalary(dto.getSalary());
                }
                jobPosting.setUpdateDate(currentDate);
                jobPosting.setStatus(BasicStatus.ACTIVE);
                jobPosting.setUser(users);
                jobPostingRepository.save(jobPosting);
                return true;
            } else {
                throw new RuntimeException("Job Posting Id not found.");
            }
        } else {
            throw new RuntimeException("HR ID not found.");
        }
    }

    @Override
    public boolean delete(Integer hrId, Integer jobPostingId) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(hrId, RoleType.HR);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, BasicStatus.ACTIVE);
        if(usersOptional.isPresent()){
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                jobPosting.setStatus(BasicStatus.DELETED);
                jobPostingRepository.save(jobPosting);
                return true;
            }else{
                throw new RuntimeException("Job Posting Id not found.");
            }

        }else{
            throw new RuntimeException("HR ID not found.");
        }
    }

    @Override
    public boolean share(Integer hrId, Integer jobPostingId) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(hrId, RoleType.HR);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, BasicStatus.ACTIVE);
        if(usersOptional.isPresent()){
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                jobPosting.setShare(BasicStatus.PUBLIC);
                jobPostingRepository.save(jobPosting);
                return true;
            }else{
                throw new RuntimeException("Job Posting Id not found.");
            }

        }else{
            throw new RuntimeException("HR ID not found.");
        }
    }

    @Override
    public JobPostingViewDto getByHr(Integer hrId, Integer jobPostingId) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(hrId, RoleType.HR);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, BasicStatus.ACTIVE);
        JobPostingViewDto jobPostingViewDto = new JobPostingViewDto();
        if(usersOptional.isPresent()){
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                jobPostingViewDto.setId(jobPosting.getId());
                jobPostingViewDto.setTitle(jobPosting.getTitle());
                jobPostingViewDto.setWorkingType(jobPosting.getWorkingType());
                jobPostingViewDto.setLocation(jobPosting.getLocation());
                jobPostingViewDto.setDescription(jobPosting.getDescription());
                jobPostingViewDto.setRequirement(jobPosting.getRequirement());
                jobPostingViewDto.setSalary(jobPosting.getSalary());
                jobPostingViewDto.setCreateDate(jobPosting.getCreateDate());
                jobPostingViewDto.setUpdateDate(jobPosting.getUpdateDate());
                jobPostingViewDto.setShare(jobPosting.getShare());
                return jobPostingViewDto;
            }else{
                throw new RuntimeException("Job Posting Id not found.");
            }

        }else{
            throw new RuntimeException("HR ID not found.");
        }
    }

    @Override
    public List<JobPostingViewDto> getListByHr(Integer hrId, BasicStatus share) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(hrId, RoleType.HR);
        if(usersOptional.isPresent()){
            List<JobPosting> jobPostings = jobPostingRepository.findByUser_IdAndStatus(hrId,BasicStatus.ACTIVE);

            return jobPostings.stream()
                    .filter(jobPosting -> share == null || jobPosting.getStatus() == share)
                    .map(jobPosting -> modelMapper.map(jobPosting, JobPostingViewDto.class))
                    .collect(Collectors.toList());
        }else{
            throw new RuntimeException("HR ID not found.");
        }
    }

    @Override
    public List<JobPostingViewDto> getListPublic(Integer cvId, String title, String working, String location) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        Cv cv = cvOptional.get();
        CvBodyDto cvBodyDto = cv.deserialize();
        List<JobPosting> jobPostings = jobPostingRepository.findByShare(BasicStatus.PUBLIC);
        return jobPostings.stream()
                .filter(jobPosting ->
                        (title == null || jobPosting.getTitle().contains(title)) &&
                                (working == null || jobPosting.getWorkingType().contains(working)) &&
                                        (location == null || jobPosting.getLocation().contains(location)) &&
                                                (cvBodyDto.getSkills() == null || jobPosting.getRequirement().contains(convertSkillsListToString(cvBodyDto.getSkills())))
                )
                .map(jobPosting -> modelMapper.map(jobPosting, JobPostingViewDto.class))
                .collect(Collectors.toList());
    }

    private String convertSkillsListToString(List<SkillDto> skills) {
        if (skills == null || skills.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SkillDto skill : skills) {
            sb.append(skill.getDescription()).append(", ");
        }
        // Loại bỏ dấu phẩy và khoảng trắng cuối cùng
        return sb.substring(0, sb.length() - 2);
    }
}
