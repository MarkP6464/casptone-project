package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
//import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.JobPostingService;
import com.example.capstoneproject.service.JobPostingViewService;
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
    JobPostingViewRepository jobPostingViewRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    PrettyTime prettyTime;

    @Autowired
    ApplicationLogRepository applicationLogRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    JobPostingViewService jobPostingViewService;

    @Override
    public boolean createDraft(Integer hrId, JobPostingAddDto dto) {
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
            jobPosting.setSkill(String.join(",", dto.getSkill()));
            if(dto.getApplyAgain()==null){
                jobPosting.setApplyAgain(0);
            }
            jobPosting.setApplyAgain(dto.getApplyAgain());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setDeadline(dto.getDeadline());
            jobPosting.setCreateDate(currentDateTime);
            jobPosting.setBan(false);
            jobPosting.setStatus(ACTIVE);
            jobPosting.setShare(StatusReview.Draft);
            jobPosting.setUser(modelMapper.map(users, Users.class));
            jobPostingRepository.save(jobPosting);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPublic(Integer hrId, JobPostingAddDto dto){
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
            jobPosting.setSkill(String.join(",", dto.getSkill()));
            if(dto.getApplyAgain()==null){
                jobPosting.setApplyAgain(0);
            }
            jobPosting.setApplyAgain(dto.getApplyAgain());
            jobPosting.setSalary(dto.getSalary());
            jobPosting.setDeadline(dto.getDeadline());
            jobPosting.setCreateDate(currentDateTime);
            jobPosting.setBan(false);
            jobPosting.setStatus(ACTIVE);
            jobPosting.setShare(StatusReview.Published);
            jobPosting.setUser(modelMapper.map(users, Users.class));
            jobPostingRepository.save(jobPosting);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Integer hrId, Integer jobPostingId, JobPostingAddDto dto) {
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
                        jobPosting.setSkill(String.join(",", dto.getSkill()));
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
                    jobPosting.setShare(StatusReview.Published);
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
    public boolean unShare(Integer hrId, Integer jobPostingId){
        Optional<UsersDto> usersOptional = Optional.ofNullable(modelMapper.map(usersRepository.findUsersById(hrId), UsersDto.class));
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByUser_IdAndIdAndStatus(hrId, jobPostingId, ACTIVE);
        if (usersOptional.isPresent()) {
            UsersDto users = usersOptional.get();
            if (jobPostingOptional.isPresent()) {
                JobPosting jobPosting = jobPostingOptional.get();
                if(Objects.equals(users.getId(), jobPosting.getUser().getId())){
                    jobPosting.setShare(StatusReview.Unpublish);
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
                    List<JobPostingView> jobPostingViews = jobPostingViewRepository.findAllByJobPosting_Id(jobPostingId);
                    jobPostingViewDto.setId(jobPosting.getId());
                    jobPostingViewDto.setTitle(jobPosting.getTitle());
                    jobPostingViewDto.setCompanyName(jobPosting.getCompanyName());
                    jobPostingViewDto.setAvatar(jobPosting.getAvatar());
                    jobPostingViewDto.setAbout(jobPosting.getAbout());
                    jobPostingViewDto.setBenefit(jobPosting.getBenefit());
                    jobPostingViewDto.setSkill(jobPosting.getSkill().split(","));
                    jobPostingViewDto.setView(jobPostingViews.size());
                    jobPostingViewDto.setWorkingType(jobPosting.getWorkingType());
                    jobPostingViewDto.setLocation(jobPosting.getLocation());
                    jobPostingViewDto.setDescription(jobPosting.getDescription());
                    jobPostingViewDto.setRequirement(jobPosting.getRequirement());
                    jobPostingViewDto.setSalary(jobPosting.getSalary());
                    jobPostingViewDto.setCreateDate(LocalDate.from(jobPosting.getCreateDate()));
                    jobPostingViewDto.setUpdateDate(jobPosting.getUpdateDate());
                    jobPostingViewDto.setDeadline(jobPosting.getDeadline());
                    jobPostingViewDto.setStatus(jobPosting.getStatus());
                    jobPostingViewDto.setShare(jobPosting.getShare());
                    jobPostingViewDto.setApply(jobPosting.getApplyAgain());
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
    public JobPostingViewUserDetailDto getByUser(Integer userId, Integer jobPostingId) {
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndShareAndStatusAndBanIsFalse(jobPostingId,StatusReview.Published, ACTIVE);
        JobPostingViewUserDetailDto jobPostingViewDto = new JobPostingViewUserDetailDto();
        if(jobPostingOptional.isPresent()){
            List<JobPostingView> jobPostingViews = jobPostingViewRepository.findAllByJobPosting_Id(jobPostingId);
            JobPosting jobPosting = jobPostingOptional.get();
            jobPostingViewDto.setId(jobPosting.getId());
            jobPostingViewDto.setTitle(jobPosting.getTitle());
            jobPostingViewDto.setCompanyName(jobPosting.getCompanyName());
            jobPostingViewDto.setAvatar(jobPosting.getAvatar());
            jobPostingViewDto.setAbout(jobPosting.getAbout());
            jobPostingViewDto.setBenefit(jobPosting.getBenefit());
            jobPostingViewDto.setSkill(jobPosting.getSkill().split(","));
            jobPostingViewDto.setView(jobPostingViews.size());
            jobPostingViewDto.setWorkingType(jobPosting.getWorkingType());
            jobPostingViewDto.setLocation(jobPosting.getLocation());
            jobPostingViewDto.setDescription(jobPosting.getDescription());
            jobPostingViewDto.setRequirement(jobPosting.getRequirement());
            jobPostingViewDto.setSalary(jobPosting.getSalary());
            jobPostingViewDto.setCreateDate(LocalDate.from(jobPosting.getCreateDate()));
            jobPostingViewDto.setUpdateDate(jobPosting.getUpdateDate());
            jobPostingViewDto.setStatus(jobPosting.getStatus());
            jobPostingViewDto.setDeadline(jobPosting.getDeadline());
            jobPostingViewDto.setShare(jobPosting.getShare());
            Optional<Like> likeOptional = likeRepository.findByUser_IdAndJobPosting_Id(userId, jobPosting.getId());
            if(likeOptional.isPresent()){
                jobPostingViewDto.setLiked(true);
            }else{
                jobPostingViewDto.setLiked(false);
            }

            jobPostingViewService.createJobPostingView(userId,jobPostingId);
            return jobPostingViewDto;
        }else{
            throw new BadRequestException("Job Posting Id not found.");
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
                List<JobPostingView> jobPostingViews = jobPostingViewRepository.findAllByJobPosting_Id(jobPosting.getId());
                JobPostingViewOverDto jobPostingViewDto = new JobPostingViewOverDto();
                jobPostingViewDto.setId(jobPosting.getId());
                jobPostingViewDto.setTitle(jobPosting.getTitle());
                jobPostingViewDto.setView(jobPostingViews.size());
                if(jobPosting.getDeadline().isBefore(current)){
                    jobPostingViewDto.setStatus(StatusReview.Overdue);
                }else {
                    jobPostingViewDto.setStatus(jobPosting.getShare());
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
    public List<JobPostingViewOverCandidateLikeDto> getJobPostingsByCandidate(Integer userId, String title, String location) {
        List<JobPostingViewOverCandidateLikeDto> jobPostingLike = new ArrayList<>();
        List<JobPosting> jobPostings = jobPostingRepository.findByShareAndStatusAndBanIsFalse(StatusReview.Published, ACTIVE);
        if(jobPostings!=null){
            for(JobPosting jobPosting: jobPostings){
                JobPostingViewOverCandidateLikeDto jobPostingLikeAdd = new JobPostingViewOverCandidateLikeDto();
                if(jobPosting.getShare()== StatusReview.Published && jobPosting.getStatus()== BasicStatus.ACTIVE){
                    jobPostingLikeAdd.setId(jobPosting.getId());
                    jobPostingLikeAdd.setTitle(jobPosting.getTitle());
                    jobPostingLikeAdd.setCompanyName(jobPosting.getCompanyName());
                    jobPostingLikeAdd.setAvatar(jobPosting.getAvatar());
                    jobPostingLikeAdd.setLocation(jobPosting.getLocation());
                    jobPostingLikeAdd.setSkill(jobPosting.getSkill().split(","));
                    jobPostingLikeAdd.setSalary(jobPosting.getSalary());
                    jobPostingLikeAdd.setCreateDate(prettyTime.format(jobPosting.getCreateDate()));
                    Optional<Like> likeOptional = likeRepository.findByUser_IdAndJobPosting_Id(userId, jobPosting.getId());
                    if(likeOptional.isPresent()){
                        jobPostingLikeAdd.setLiked(true);
                    }else{
                        jobPostingLikeAdd.setLiked(false);
                    }
                    jobPostingLike.add(jobPostingLikeAdd);
                }
            }
            jobPostingLike = filterBySearchJobPostingsByCandidate(jobPostingLike, title, location);
            return jobPostingLike;
        }else{
            throw new BadRequestException("Currently, the system cannot find the job postings.");
        }
    }

    private List<JobPostingViewOverCandidateLikeDto> filterBySearchJobPostingsByCandidate(
            List<JobPostingViewOverCandidateLikeDto> jobPostings, String title, String location) {
        if ((title != null && !title.trim().isEmpty()) || (location != null && !location.trim().isEmpty())) {
            String titleSearchTermLowerCase = (title != null) ? title.toLowerCase() : "";
            String locationSearchTermLowerCase = (location != null) ? location.toLowerCase() : "";

            jobPostings = jobPostings.stream()
                    .filter(dto ->
                            (titleSearchTermLowerCase.isEmpty() || dto.getTitle().toLowerCase().contains(titleSearchTermLowerCase)) &&
                                    (locationSearchTermLowerCase.isEmpty() || dto.getLocation().toLowerCase().contains(locationSearchTermLowerCase)))
                    .collect(Collectors.toList());
        }
        return jobPostings;
    }

    @Override
    public List<JobPostingViewDto> getListPublic(Integer userId, Integer cvId, String title, String working, String location) throws JsonProcessingException {
        List<JobPosting> jobPostings = jobPostingRepository.findByShareAndStatusAndBanIsFalse(StatusReview.Published, ACTIVE);
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

    @Override
    public List<CandidateOverViewDto> getAllCandidateFilterCV(Integer postingId) throws JsonProcessingException {
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatus(postingId, ACTIVE);
        List<CvMatchingDto> cvMatching = new ArrayList<>();
        List<CandidateOverViewDto> listCandidate = new ArrayList<>();
        Set<Integer> existingCandidateIds = new HashSet<>(); // To keep track of existing candidate IDs
        if(jobPostingOptional.isPresent()){
            JobPosting jobPosting = jobPostingOptional.get();
            if(!jobPosting.getSkill().isEmpty()){
                String requirement = jobPosting.getSkill();
                List<Candidate> candidates = candidateRepository.findAllByPublishTrueAndRole_RoleName(RoleType.CANDIDATE);
                for(Candidate candidate: candidates){
                    List<Cv> cvs = cvRepository.findAllByUser_IdAndStatusAndSearchableIsTrue(candidate.getId(), ACTIVE);
                    for(Cv cv:cvs){
                        CvBodyDto cvBodyDto = cv.deserialize();
                        StringBuilder cvSkillBuilder = new StringBuilder();
                        for (SkillDto x : cvBodyDto.getSkills()) {
                            if (x.getIsDisplay()) {
                                cvSkillBuilder.append(x.getDescription());
                            }
                        }
                        String cvSkill = cvSkillBuilder.toString();
                        CvMatchingDto matching = new CvMatchingDto();
                        matching.setCandidateId(cv.getUser().getId());
                        matching.setCvId(cv.getId());
                        matching.setName(cv.getUser().getName());
                        matching.setAvatar(cv.getUser().getAvatar());
                        if(cv.getJobDescription()!=null){
                            matching.setJobTitle(cv.getJobDescription().getTitle());
                        }
                        matching.setCompany(cv.getCompanyName());
                        matching.setAbout(cv.getUser().getAbout());
                        matching.setScore(findSimilarityRatio(cvSkill,requirement));
                        cvMatching.add(matching);
                    }
                }

            }else{
                throw new BadRequestException("Please choice other job posting. Because this job posting dont have requirement.");
            }

            // Sort cvMatching by score in descending order
            cvMatching.sort(Comparator.comparingDouble(CvMatchingDto::getScore).reversed());

            // Populate listCandidate and avoid duplicates
            for (CvMatchingDto cvMatch : cvMatching) {
                if (!existingCandidateIds.contains(cvMatch.getCandidateId()) && cvMatch.getScore() >= 1) {
                    CandidateOverViewDto candidate = new CandidateOverViewDto();
                    candidate.setId(cvMatch.getCandidateId());
                    candidate.setName(cvMatch.getName());
                    candidate.setAvatar(cvMatch.getAvatar());
                    candidate.setJobTitle(cvMatch.getJobTitle());
                    candidate.setCompany(cvMatch.getCompany());
                    candidate.setAbout(cvMatch.getAbout());
                    listCandidate.add(candidate);

                    // Add the candidate ID to the set to track existing candidates
                    existingCandidateIds.add(cvMatch.getCandidateId());
                }
            }

        }else{
            throw new BadRequestException("Job posting ID not found.");
        }
        return listCandidate;
    }

    @Override
    public String updateBan(Integer adminId, Integer postingId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatus(postingId, ACTIVE);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                if(!jobPosting.getBan()){
                    jobPosting.setBan(true);
                    jobPostingRepository.save(jobPosting);
                    return "Ban successful";
                }else{
                    return "This job posting has now been banned. So you can't ban it";
                }
            }else{
                throw new BadRequestException("Job Posting Id not found.");
            }
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
    }

    @Override
    public String updateUnBan(Integer adminId, Integer postingId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatus(postingId, ACTIVE);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                if(jobPosting.getBan()){
                    jobPosting.setBan(false);
                    jobPostingRepository.save(jobPosting);
                    return "UnBan successful";
                }else{
                    return "This job posting has not been banned yet. So you cannot unban";
                }

            }else{
                throw new BadRequestException("Job Posting Id not found.");
            }
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
    }

    @Override
    public List<JobPostingAdminViewDto> getListAdminPosting(Integer adminId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            List<JobPostingAdminViewDto> JobPostingAdminViews = new ArrayList<>();
            List<JobPosting> jobPostings = jobPostingRepository.findByStatusAndShareOrStatusAndBanIsTrue(ACTIVE,StatusReview.Published, ACTIVE);
            if(jobPostings!=null){
                for(JobPosting jobPosting:jobPostings){
                    JobPostingAdminViewDto jobPostingAdminView = new JobPostingAdminViewDto();
                    jobPostingAdminView.setId(jobPosting.getId());
                    jobPostingAdminView.setJobTitle(jobPosting.getTitle());
                    jobPostingAdminView.setCompany(jobPosting.getCompanyName());
                    if(jobPosting.getBan()){
                        jobPostingAdminView.setStatus("Banned");
                    }else{
                        jobPostingAdminView.setStatus("UnBanned");
                    }
                    jobPostingAdminView.setOwner(jobPosting.getUser().getName());
                    jobPostingAdminView.setCreateDate(jobPosting.getCreateDate());
                    JobPostingAdminViews.add(jobPostingAdminView);
                }
            }
            return JobPostingAdminViews;
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
    }

    @Override
    public List<JobPostingResponse> getListGeneration(String search) {
        List<JobPosting> allJobPostings = jobPostingRepository.findByShareAndStatusAndBanIsFalse(StatusReview.Published, ACTIVE);

        // Apply a filter condition based on your requirements

        return allJobPostings.stream()
                .filter(jobPosting -> !shouldExclude(jobPosting, search))
                .map(this::mapToJobPostingResponse)
                .collect(Collectors.toList());
    }

    private boolean shouldExclude(JobPosting jobPosting, String search) {
        return search != null && !jobPosting.getTitle().toLowerCase().contains(search.toLowerCase());
    }

    private JobPostingResponse mapToJobPostingResponse(JobPosting jobPosting) {
        JobPostingResponse response = new JobPostingResponse();
        response.setId(jobPosting.getId());
        response.setTitle(jobPosting.getTitle());
        response.setCompanyName(jobPosting.getCompanyName());
        response.setDescription(jobPosting.getDescription());
        return response;
    }




    public double findSimilarityRatio(String sentence1, String sentence2) {

        HashMap<String, Integer> firstSentenceMap = new HashMap<>();
        HashMap<String, Integer> secondSentenceMap = new HashMap<>();

        String[] firstSentenceWords = sentence1.split("\\W+"); // Sử dụng regex để tách các từ
        String[] secondSentenceWords = sentence2.split("\\W+"); // Sử dụng regex để tách các từ

        for (String word : firstSentenceWords) {
            if (firstSentenceMap.containsKey(word)) {
                firstSentenceMap.put(word, firstSentenceMap.get(word) + 1);
            }
            else {
                firstSentenceMap.put(word, 1);
            }
        }

        for (String word : secondSentenceWords) {
            if (secondSentenceMap.containsKey(word)) {
                secondSentenceMap.put(word, secondSentenceMap.get(word) + 1);
            }
            else {
                secondSentenceMap.put(word, 1);
            }
        }

        double totalWords = 0;
        double totalHits = 0;

        if (firstSentenceWords.length >= secondSentenceWords.length) {
            totalWords = firstSentenceWords.length;
            for (Map.Entry<String, Integer> entry : firstSentenceMap.entrySet()) {
                String key = entry.getKey();

                if (secondSentenceMap.containsKey(key)) {
                    totalHits = totalHits + Math.min(secondSentenceMap.get(key), firstSentenceMap.get(key));
                }
            }
        }
        else {
            totalWords = secondSentenceWords.length;
            for (Map.Entry<String, Integer> entry : secondSentenceMap.entrySet()) {
                String key = entry.getKey();

                if (firstSentenceMap.containsKey(key)) {
                    totalHits = totalHits + Math.min(secondSentenceMap.get(key), firstSentenceMap.get(key));
                }
            }

        }

        return (totalHits/totalWords)*100;
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
            jobPosting.setShare(StatusReview.Overdue);
        }
        jobPostingRepository.saveAll(jobPostings);
    }
}
