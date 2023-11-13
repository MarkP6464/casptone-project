package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.capstoneproject.enums.BasicStatus.*;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Override
    public boolean create(Integer hrId, JobPostingDto dto) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        JobPosting jobPosting = new JobPosting();
        LocalDate currentDate = LocalDate.now();
        if(usersOptional.isPresent()){
            UsersDto users = usersOptional.get();
            jobPosting.setTitle(dto.getTitle());
            jobPosting.setWorkingType(dto.getWorkingType());
            jobPosting.setLocation(dto.getLocation());
            jobPosting.setDescription(dto.getDescription());
            jobPosting.setRequirement(dto.getRequirement());
            if(dto.getApplyAgain()==null){
                jobPosting.setApplyAgain(0);
            }
            jobPosting.setApplyAgain(dto.getApplyAgain());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setDeadline(dto.getDeadline());
            jobPosting.setCreateDate(currentDate);
            jobPosting.setStatus(ACTIVE);
            jobPosting.setShare(BasicStatus.PRIVATE);
            jobPosting.setUser(modelMapper.map(users, Users.class));
            jobPostingRepository.save(jobPosting);
            return true;

        }
        return false;
    }

    @Override
    public boolean update(Integer hrId, Integer jobPostingId, JobPostingDto dto) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, ACTIVE);
        LocalDate currentDate = LocalDate.now();
        if (usersOptional.isPresent()) {
            UsersDto users = usersOptional.get();
            if (jobPostingOptional.isPresent()) {
                JobPosting jobPosting = jobPostingOptional.get();
                if(Objects.equals(users.getId(), jobPosting.getUser().getId())){
                    if (dto.getTitle() != null && !dto.getTitle().equals(jobPosting.getTitle())) {
                        jobPosting.setTitle(dto.getTitle());
                    }
                    if (dto.getWorkingType() != null && !dto.getWorkingType().equals(jobPosting.getWorkingType())) {
                        jobPosting.setWorkingType(dto.getWorkingType());
                    }
                    if (dto.getLocation() != null && !dto.getLocation().equals(jobPosting.getLocation())) {
                        jobPosting.setLocation(dto.getLocation());
                    }
                    if (dto.getDescription() != null && !dto.getDescription().equals(jobPosting.getDescription())) {
                        jobPosting.setDescription(dto.getDescription());
                    }
                    if (dto.getRequirement() != null && !dto.getRequirement().equals(jobPosting.getDescription())) {
                        jobPosting.setRequirement(dto.getRequirement());
                    }
                    if (dto.getApplyAgain() != null && !dto.getApplyAgain().equals(jobPosting.getApplyAgain())) {
                        jobPosting.setApplyAgain(dto.getApplyAgain());
                    }
                    if (dto.getSalary() != null && !dto.getSalary().equals(jobPosting.getSalary())) {
                        jobPosting.setSalary(dto.getSalary());
                    }
                    if (dto.getDeadline() != null && !dto.getDeadline().equals(jobPosting.getDeadline())) {
                        jobPosting.setDeadline(dto.getDeadline());
                    }
                    jobPosting.setUpdateDate(currentDate);
                    jobPostingRepository.save(jobPosting);
                    return true;
                }
            } else {
                throw new BadRequestException("Job Posting Id not found.");
            }
        } else {
            throw new BadRequestException("HR ID not found.");
        }
        return false;
    }

    @Override
    public boolean delete(Integer hrId, Integer jobPostingId) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, ACTIVE);
        if (usersOptional.isPresent()) {
            UsersDto users = usersOptional.get();
            if (jobPostingOptional.isPresent()) {
                JobPosting jobPosting = jobPostingOptional.get();
                if(Objects.equals(users.getId(), jobPosting.getUser().getId())){
                    jobPosting.setStatus(DELETED);
                    jobPostingRepository.save(jobPosting);
                    return true;
                }
            } else {
                throw new BadRequestException("Job Posting Id not found.");
            }
        } else {
            throw new BadRequestException("HR ID not found.");
        }
        return false;
    }

    @Override
    public boolean share(Integer hrId, Integer jobPostingId) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, ACTIVE);
        if (usersOptional.isPresent()) {
            UsersDto users = usersOptional.get();
            if (jobPostingOptional.isPresent()) {
                JobPosting jobPosting = jobPostingOptional.get();
                if(Objects.equals(users.getId(), jobPosting.getUser().getId())){
                    jobPosting.setShare(PUBLIC);
                    jobPostingRepository.save(jobPosting);
                    return true;
                }
            } else {
                throw new BadRequestException("Job Posting Id not found.");
            }
        } else {
            throw new BadRequestException("HR ID not found.");
        }
        return false;
    }

    @Override
    public JobPostingViewDto getByHr(Integer hrId, Integer jobPostingId) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, ACTIVE);
        JobPostingViewDto jobPostingViewDto = new JobPostingViewDto();
        if(usersOptional.isPresent()){
            UsersDto users = usersOptional.get();
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                if(Objects.equals(users.getId(), jobPosting.getUser().getId())){
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
                }else {
                    throw new BadRequestException("User ID Capstone and User ID Posting mismatched.");
                }
            }else{
                throw new BadRequestException("Job Posting Id not found.");
            }

        }else{
            throw new BadRequestException("HR ID not found.");
        }
    }

    @Override
    public List<JobPostingViewDto> getListByHr(Integer hrId, BasicStatus share) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        if(usersOptional.isPresent()){
            UsersDto users = usersOptional.get();
            List<JobPosting> jobPostings = jobPostingRepository.findByUser_IdAndStatus(users.getId(), ACTIVE);

            return jobPostings.stream()
                    .filter(jobPosting -> share == null || jobPosting.getShare() == share)
                    .map(jobPosting -> modelMapper.map(jobPosting, JobPostingViewDto.class))
                    .collect(Collectors.toList());
        }else{
            throw new BadRequestException("HR ID not found.");
        }
    }

    @Override
    public List<JobPostingViewDto> getListPublic(Integer userId, Integer cvId, String title, String working, String location) throws JsonProcessingException {
        List<JobPosting> jobPostings = jobPostingRepository.findByShare(PUBLIC);
        if(userId!=null && cvId!=null){
            Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
            Cv cv = cvOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            return jobPostings.stream()
                    .filter(jobPosting ->
                            (title == null || jobPosting.getTitle().contains(title)) &&
                                    (working == null || jobPosting.getWorkingType().contains(working)) &&
                                    (location == null || jobPosting.getLocation().contains(location)) &&
                                    (cvBodyDto.getSkills() == null || jobPosting.getRequirement().contains(convertSkillsListToString(cvBodyDto.getSkills())))
                    )
                    .map(jobPosting -> modelMapper.map(jobPosting, JobPostingViewDto.class))
                    .collect(Collectors.toList());
        }else {
            return jobPostings.stream()
                    .filter(jobPosting ->
                            (title == null || jobPosting.getTitle().contains(title)) &&
                                    (working == null || jobPosting.getWorkingType().contains(working)) &&
                                    (location == null || jobPosting.getLocation().contains(location))
                    )
                    .map(jobPosting -> modelMapper.map(jobPosting, JobPostingViewDto.class))
                    .collect(Collectors.toList());
        }
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

    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào mỗi ngày lúc 00:00:00
    public void updateJobPostings() {
        LocalDate currentDate = LocalDate.now();
        List<JobPosting> jobPostings = jobPostingRepository.findByDeadline(currentDate);
        for (JobPosting jobPosting : jobPostings) {
            jobPosting.setShare(BasicStatus.PRIVATE);
        }
        jobPostingRepository.saveAll(jobPostings);
    }
}
