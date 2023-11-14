package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.ApplicationLogRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.capstoneproject.enums.BasicStatus.*;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    PrettyTime prettyTime;

    @Autowired
    ApplicationLogRepository applicationLogRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Override
    public boolean create(Integer hrId, JobPostingDto dto) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        JobPosting jobPosting = new JobPosting();
        LocalDateTime currentDateTime = LocalDateTime.now();
        if(usersOptional.isPresent()){
            UsersDto users = usersOptional.get();
            jobPosting.setTitle(dto.getTitle());
            jobPosting.setWorkingType(dto.getWorkingType());
            jobPosting.setLocation(dto.getLocation());
            jobPosting.setDescription(dto.getDescription());
            jobPosting.setRequirement(dto.getRequirement());
            jobPosting.setCompanyName(dto.getCompanyName());
            jobPosting.setAvatar(dto.getAvatar());
            jobPosting.setAbout(dto.getAbout());
            jobPosting.setBenefit(dto.getBenefit());
            jobPosting.setSkill(dto.getSkill());
            jobPosting.setView(0);
            if(dto.getApplyAgain()==null){
                jobPosting.setApplyAgain(0);
            }
            jobPosting.setApplyAgain(dto.getApplyAgain());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setDeadline(dto.getDeadline());
            jobPosting.setCreateDate(currentDateTime);
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

                    if (dto.getCompanyName() != null && !dto.getCompanyName().equals(jobPosting.getCompanyName())) {
                        jobPosting.setCompanyName(dto.getCompanyName());
                    }
                    if (dto.getAvatar() != null && !dto.getAvatar().equals(jobPosting.getAvatar())) {
                        jobPosting.setAvatar(dto.getAvatar());
                    }
                    if (dto.getAbout() != null && !dto.getAbout().equals(jobPosting.getAbout())) {
                        jobPosting.setAbout(dto.getAbout());
                    }
                    if (dto.getBenefit() != null && !dto.getBenefit().equals(jobPosting.getBenefit())) {
                        jobPosting.setBenefit(dto.getBenefit());
                    }
                    if (dto.getSkill() != null && !dto.getSkill().equals(jobPosting.getSkill())) {
                        jobPosting.setSkill(dto.getSkill());
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
//                    jobPostingViewDto.setCreateDate(jobPosting.getCreateDate());
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
    public List<JobPostingViewDetailDto> getListByHr(Integer hrId, String sortBy, String sortOrder, String searchTerm) {
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        LocalDate current = LocalDate.now();
        if(usersOptional.isPresent()){
            UsersDto users = usersOptional.get();
            List<JobPosting> jobPostings = jobPostingRepository.findByUser_IdAndStatus(users.getId(), ACTIVE);

            List<JobPostingViewOverDto> result = new ArrayList<>();
            for (JobPosting jobPosting : jobPostings) {
                JobPostingViewOverDto jobPostingViewDto = new JobPostingViewOverDto();
                jobPostingViewDto.setId(jobPosting.getId());
                jobPostingViewDto.setTitle(jobPosting.getTitle());
                jobPostingViewDto.setView(jobPosting.getView());
                if(jobPosting.getStatus() == BasicStatus.PUBLIC && jobPosting.getDeadline().isBefore(current)){
                    jobPostingViewDto.setStatus(StatusReview.Overdue);
                }else if(jobPosting.getStatus() == BasicStatus.PUBLIC){
                    jobPostingViewDto.setStatus(StatusReview.Published);
                }else if(jobPosting.getStatus() == BasicStatus.DRAFT){
                    jobPostingViewDto.setStatus(StatusReview.Draft);
                }else {
                    jobPostingViewDto.setStatus(StatusReview.Unpublish);
                }
                jobPostingViewDto.setTimestamp(jobPosting.getCreateDate());
                jobPostingViewDto.setApplication(applicationLogRepository.countByJobPostingId(jobPosting.getId()));
                result.add(jobPostingViewDto);
            }

            // Sort the list based on the specified field and order if provided
            if (sortBy != null && !sortBy.trim().isEmpty() && sortOrder != null && !sortOrder.trim().isEmpty()) {
                sortJobPostingList(result, sortBy, sortOrder);
            }

            // Apply search filter if searchTerm is provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                result = filterBySearchTerm(result, searchTerm);
            }

            List<JobPostingViewDetailDto> jobPostingViewDetailDtos = new ArrayList<>();
            for (JobPostingViewOverDto jobPostingViewOverDto : result) {
                JobPostingViewDetailDto jobPostingViewDetailDto = new JobPostingViewDetailDto();
                jobPostingViewDetailDto.setId(jobPostingViewOverDto.getId());
                jobPostingViewDetailDto.setTitle(jobPostingViewOverDto.getTitle());
                jobPostingViewDetailDto.setStatus(jobPostingViewOverDto.getStatus());
                jobPostingViewDetailDto.setView(jobPostingViewOverDto.getView());
                jobPostingViewDetailDto.setApplication(jobPostingViewOverDto.getApplication());
                jobPostingViewDetailDto.setTimestamp(prettyTime.format(jobPostingViewOverDto.getTimestamp()));

                jobPostingViewDetailDtos.add(jobPostingViewDetailDto);
            }

            return jobPostingViewDetailDtos;
        }else{
            throw new BadRequestException("HR ID not found.");
        }
    }

    @Override
    public List<JobPostingViewOverCandidateDto> getJobPostingsByCandidate(String title, String location) {
        return null;
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

    private void sortJobPostingList(List<JobPostingViewOverDto> jobPostings, String sortBy, String sortOrder) {
        Comparator<JobPostingViewOverDto> comparator = null;

        switch (sortBy) {
            case "view":
                comparator = Comparator.comparing(JobPostingViewOverDto::getView);
                break;
            case "application":
                comparator = Comparator.comparing(JobPostingViewOverDto::getApplication);
                break;
            case "createdate":
                comparator = Comparator.comparing(JobPostingViewOverDto::getTimestamp);
                break;
            case "title":
                comparator = Comparator.comparing(JobPostingViewOverDto::getTitle);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter");
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        Collections.sort(jobPostings, comparator);
    }

    private List<JobPostingViewOverDto> filterBySearchTerm(List<JobPostingViewOverDto> jobPostings, String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchTermLowerCase = searchTerm.toLowerCase();
            jobPostings = jobPostings.stream()
                    .filter(dto -> dto.getTitle().toLowerCase().contains(searchTermLowerCase))
                    .collect(Collectors.toList());
        }
        return jobPostings;
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
        List<JobPosting> jobPostings = jobPostingRepository.findAllByDeadline(currentDate);
        for (JobPosting jobPosting : jobPostings) {
            jobPosting.setShare(BasicStatus.PRIVATE);
        }
        jobPostingRepository.saveAll(jobPostings);
    }
}
