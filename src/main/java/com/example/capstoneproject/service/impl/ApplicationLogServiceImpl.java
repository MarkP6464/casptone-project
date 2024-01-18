package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvResumeDto;
import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.ApplicationLogStatus;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ApplicationLogService;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class ApplicationLogServiceImpl implements ApplicationLogService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ApplicationLogRepository applicationLogRepository;

    @Autowired
    HistoryCoverLetterRepository historyCoverLetterRepository;

    @Autowired
    HistoryService historyService;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    CoverLetterRepository coverLetterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId, NoteDto dto) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, StatusReview.Published);
        ApplicationLog applicationLog = new ApplicationLog();
        LocalDate currentDate = LocalDate.now();


        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            applicationLog.setUser(user);
        } else throw new InternalServerException("Not found user");
        if (cvOptional.isPresent()) {
            Cv cv = cvOptional.get();
            HistoryViewDto hisCvId = historyService.create(userId, cvId);
            Optional<History> historyOptional = historyRepository.findById(hisCvId.getId());
            if (historyOptional.isPresent()) {
                History history = historyOptional.get();
                applicationLog.setCv(history);
            }
        } else throw new InternalServerException("Not found cv");
        Cv cv = cvOptional.get();
        Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findByCv_User_IdAndIdAndStatus(userId, coverLetterId, BasicStatus.ACTIVE);
        if (coverLetterOptional.isPresent()) {
            CoverLetter coverLetter = coverLetterOptional.get();

            //save cover letter history
            HistoryCoverLetter historyCoverLetter = new HistoryCoverLetter();
            historyCoverLetter.setTitle(coverLetter.getTitle());
            historyCoverLetter.setDear(coverLetter.getDear());
            historyCoverLetter.setDate(coverLetter.getDate());
            historyCoverLetter.setCompany(coverLetter.getCompany());
            historyCoverLetter.setDescription(coverLetter.getDescription());
            historyCoverLetter.setCoverLetter(coverLetter);
            historyCoverLetter = historyCoverLetterRepository.save(historyCoverLetter);

            applicationLog.setCoverLetter(historyCoverLetter);
        }
        applicationLog.setNote(dto.getNote());
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        applicationLog.setTimestamp(currentTimestamp);

        if (jobPostingOptional.isPresent()) {
            JobPosting jobPosting = jobPostingOptional.get();
            List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(userId, postingId);
            applicationLog.setJobPosting(jobPosting);
            if (!applicationLogs.isEmpty()) {
                ApplicationLog applicationLogCheck = applicationLogs.get(0);
                Timestamp timestamp = applicationLogCheck.getTimestamp();
                LocalDate countDate = timestamp.toLocalDateTime().toLocalDate();
                Integer condition = jobPosting.getApplyAgain();
                LocalDate resultDate = countDate.plusDays(condition);
                if (resultDate.isBefore(currentDate) || resultDate.isEqual(currentDate)) {
                    applicationLog.setJobPosting(jobPosting);
                } else {
                    throw new BadRequestException("Please apply after date " + resultDate);
                }
            }
            applicationLog.setStatus(ApplicationLogStatus.RECEIVED);
            applicationLog.setNote(dto.getNote());
            applicationLogRepository.save(applicationLog);
//            sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
            return true;
        } else throw new InternalServerException("Not found Job posting");
    }

    public static void sendEmail(String toEmail, String subject, String message) {
        // Cấu hình thông tin SMTP
        String host = "smtp.gmail.com";
        String username = "acc3cuaminh@gmail.com";
        String password = "jwkspmuznvxbikcx";

        // Cấu hình các thuộc tính cho session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo một phiên gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(username));

            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            mimeMessage.setSubject(subject);

            mimeMessage.setText(message);

            Transport.send(mimeMessage);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email.");
        }
    }

    @Override
    public List<ApplicationLogFullResponse> getAll(Integer postId) {
        List<ApplicationLogFullResponse> newList = new ArrayList<>();
        List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByJobPosting_IdOrderByTimestampDesc(postId);
        if (applicationLogs != null) {
            for (ApplicationLog applicationLog : applicationLogs) {
                ApplicationLogFullResponse response = new ApplicationLogFullResponse();
                response.setCandidateName(applicationLog.getUser().getName());
                response.setApplyDate(applicationLog.getTimestamp());
                response.setNote(applicationLog.getNote());
                response.setEmail(applicationLog.getUser().getEmail());
                response.setStatus(applicationLog.getStatus());
                JobPostingNameViewDto jobPosting = new JobPostingNameViewDto();
                jobPosting.setId(applicationLog.getJobPosting().getId());
                jobPosting.setName(applicationLog.getJobPosting().getTitle());
                response.setJobPosting(jobPosting);
                if (applicationLog.getCv() != null) {
                    CvResumeDto resume = new CvResumeDto();
                    resume.setId(applicationLog.getCv().getId());
                    resume.setResumeName(applicationLog.getCv().getCv().getResumeName());
                    response.setCvs(resume);
                }
                if (applicationLog.getCoverLetter() != null) {
                    CoverLetterViewDto coverLetter = new CoverLetterViewDto();
                    coverLetter.setId(applicationLog.getCoverLetter().getId());
                    coverLetter.setTitle(applicationLog.getCoverLetter().getTitle());
                    response.setCoverLetters(coverLetter);
                }
                newList.add(response);

            }
        }
        return newList;
    }

    @Override
    public List<ApplicationLogFullResponse> getAllByHrID(Integer hrId) {
        List<ApplicationLogFullResponse> newList = new ArrayList<>();
        List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByJobPosting_User_IdOrderByTimestamp(hrId);
        if (applicationLogs != null) {
            for (ApplicationLog applicationLog : applicationLogs) {
                ApplicationLogFullResponse response = new ApplicationLogFullResponse();
                response.setCandidateName(applicationLog.getUser().getName());
                response.setApplyDate(applicationLog.getTimestamp());
                response.setNote(applicationLog.getNote());
                response.setEmail(applicationLog.getUser().getEmail());
                response.setStatus(applicationLog.getStatus());
                JobPostingNameViewDto jobPosting = new JobPostingNameViewDto();
                jobPosting.setId(applicationLog.getJobPosting().getId());
                jobPosting.setName(applicationLog.getJobPosting().getTitle());
                response.setJobPosting(jobPosting);
                if (applicationLog.getCv() != null) {
                    CvResumeDto resume = new CvResumeDto();
                    resume.setId(applicationLog.getCv().getId());
                    resume.setResumeName(applicationLog.getCv().getCv().getResumeName());
                    response.setCvs(resume);
                }
                if (applicationLog.getCoverLetter() != null) {
                    CoverLetterViewDto coverLetter = new CoverLetterViewDto();
                    coverLetter.setId(applicationLog.getCoverLetter().getId());
                    coverLetter.setTitle(applicationLog.getCoverLetter().getTitle());
                    response.setCoverLetters(coverLetter);
                }
                newList.add(response);

            }
        }
        return newList;
    }

    @Override
    public List<ApplicationLogCandidateResponse> getAllByCandidateId(Integer id) {
        List<ApplicationLogCandidateResponse> newList = new ArrayList<>();
        List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdOrderByTimestamp(id);
        if (applicationLogs != null) {
            for (ApplicationLog applicationLog : applicationLogs) {
                ApplicationLogCandidateResponse response = new ApplicationLogCandidateResponse();
                response.setCandidateName(applicationLog.getUser().getName());
                response.setCompany(applicationLog.getJobPosting().getCompanyName());
                response.setApplyDate(applicationLog.getTimestamp());
                response.setNote(applicationLog.getNote());
                response.setStatus(applicationLog.getStatus());
                JobPostingNameViewDto jobPosting = new JobPostingNameViewDto();
                jobPosting.setId(applicationLog.getJobPosting().getId());
                jobPosting.setName(applicationLog.getJobPosting().getTitle());
                response.setJobPosting(jobPosting);
                if (applicationLog.getCv() != null) {
                    CvResumeDto resume = new CvResumeDto();
                    resume.setId(applicationLog.getCv().getId());
                    resume.setResumeName(applicationLog.getCv().getCv().getResumeName());
                    response.setCvs(resume);
                }
                if (applicationLog.getCoverLetter() != null) {
                    CoverLetterViewDto coverLetter = new CoverLetterViewDto();
                    coverLetter.setId(applicationLog.getCoverLetter().getId());
                    coverLetter.setTitle(applicationLog.getCoverLetter().getTitle());
                    response.setCoverLetters(coverLetter);
                }
                newList.add(response);

            }
        }
        return newList;
    }

    @Override
    public ApplicationLogResponse updateDownloaded(Integer id) {
        ApplicationLogResponse response;
        Optional<ApplicationLog> optionalApplicationLog = applicationLogRepository.findById(id);
        if (optionalApplicationLog.isPresent()) {
            ApplicationLog entity = optionalApplicationLog.get();
            entity.setStatus(ApplicationLogStatus.DOWNLOADED);
            entity = applicationLogRepository.save(entity);
            response = modelMapper.map(entity, ApplicationLogResponse.class);
        } else throw new ResourceNotFoundException("Not found the log by id");
        return response;
    }


    @Override
    public ApplicationLogResponse updateSeen(Integer id) {
        ApplicationLogResponse response;
        Optional<ApplicationLog> optionalApplicationLog = applicationLogRepository.findById(id);
        if (optionalApplicationLog.isPresent()) {
            ApplicationLog entity = optionalApplicationLog.get();
            entity.setStatus(ApplicationLogStatus.SEEN);
            entity = applicationLogRepository.save(entity);
            response = modelMapper.map(entity, ApplicationLogResponse.class);
        } else throw new ResourceNotFoundException("Not found the log by id");
        return response;
    }
}
