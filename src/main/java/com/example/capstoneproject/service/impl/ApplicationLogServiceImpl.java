package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.example.capstoneproject.Dto.responses.HistoryViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ApplicationLogService;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    CoverLetterRepository coverLetterRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId, NoteDto dto) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, StatusReview.Published);
        ApplicationLog applicationLog = new ApplicationLog();
        LocalDate currentDate = LocalDate.now();


        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if (usersOptional.isPresent()){
            Users user = usersOptional.get();
            applicationLog.setUser(user);
        }else throw new InternalServerException("Not found user");
        if(cvOptional.isPresent()) {
            Cv cv = cvOptional.get();
            HistoryViewDto hisCvId = historyService.create(userId, cvId);
            applicationLog.setCv(hisCvId.getId());
        }else throw new InternalServerException("Not found cv");
        Cv cv = cvOptional.get();
        Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findByCv_User_IdAndId(userId, coverLetterId);
        if(coverLetterOptional.isPresent()){
            CoverLetter coverLetter = coverLetterOptional.get();

            //save cover letter history
            HistoryCoverLetter historyCoverLetter = new HistoryCoverLetter();
            historyCoverLetter = modelMapper.map(coverLetter, HistoryCoverLetter.class);
            historyCoverLetter.setId(null);
            historyCoverLetter.setCoverLetter(coverLetter);
            historyCoverLetter = historyCoverLetterRepository.save(historyCoverLetter);

            applicationLog.setCoverLetter(historyCoverLetter.getId());
        }
        applicationLog.setNote(dto.getNote());
        applicationLog.setTimestamp(LocalDate.now());

        if (jobPostingOptional.isPresent()){
            JobPosting jobPosting = jobPostingOptional.get();
            applicationLog.setJobPosting(jobPosting);
            List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(userId,postingId);
            ApplicationLog applicationLogCheck = applicationLogs.get(0);
            LocalDate countDate = applicationLogCheck.getTimestamp();
            Integer condition = jobPosting.getApplyAgain();
            LocalDate resultDate = countDate.plusDays(condition);
            if(resultDate.isBefore(currentDate) || resultDate.isEqual(currentDate)){
                applicationLog.setJobPosting(jobPosting);
                applicationLog.setNote(dto.getNote());
                Optional<Users> usersOptional1 = usersRepository.findUsersById(userId);
                if (usersOptional1.isPresent()){
                    Users user = usersOptional1.get();
                    applicationLog.setUser(user);
                }
                applicationLogRepository.save(applicationLog);
                sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                return true;
            }else{
                throw new BadRequestException("Please apply after date " + resultDate);
            }
        } else throw new InternalServerException("Not found Job posting");
    }

    public static void sendEmail(String toEmail, String subject, String message) {
        // Cấu hình thông tin SMTP
        String host = "smtp.gmail.com";
        String username = "cvbuilder.ai@gmail.com";
        String password = "cvbtldosldixpkeh";

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
    public List<ApplicationLogResponse> getAll(Integer postId){
        List<ApplicationLogResponse> newList = null;
        List<ApplicationLog> list = applicationLogRepository.findAllByJobPosting_IdOrderByTimestampDesc(postId);
        if (!list.isEmpty()){
            ApplicationLogResponse applicationLogResponse = new ApplicationLogResponse();
             newList = list.stream().map(x -> {
                applicationLogResponse.setEmail(x.getUser().getEmail());
                applicationLogResponse.setCandidateName(x.getUser().getUsername());
                applicationLogResponse.setApplyDate(x.getTimestamp());
                applicationLogResponse.setNote(x.getNote());

                HistoryDto history = historyService.getHistoryById(x.getCv());
                if (Objects.nonNull(history)){
                    HashMap map = new HashMap();
                    map.put("id", history.getId());
                    Optional<Cv> cv = cvRepository.findById(history.getCvId());
                    if (Objects.isNull(cv)){
                        throw new InternalServerException("cvId not match with historyId");
                    }
                    map.put("resumeName", cv.get().getResumeName());
                    applicationLogResponse.getCvs().add(map);
                }
                Optional<HistoryCoverLetter> historyCoverLetterOptional = historyCoverLetterRepository.findById(x.getCv());
                if (historyCoverLetterOptional.isPresent()){
                    HistoryCoverLetter historyCoverLetter = historyCoverLetterOptional.get();
                    HashMap map = new HashMap();
                    map.put("id", historyCoverLetter.getId());
                    map.put("title", historyCoverLetter.getTitle());
                    applicationLogResponse.getCoverLetters().add(map);
                }
                return applicationLogResponse;
            }).collect(Collectors.toList());
        }
        return newList;
    }

    @Override
    public List<ApplicationLogResponse> getAllByHrID(Integer hrId){
        List<ApplicationLogResponse> newList = null;
        List<ApplicationLog> list = applicationLogRepository.findAllByJobPosting_User_IdOrderByTimestamp(hrId);
        if (!list.isEmpty()){
            ApplicationLogResponse applicationLogResponse = new ApplicationLogResponse();
            newList = list.stream().map(x -> {
                applicationLogResponse.setEmail(x.getUser().getEmail());
                applicationLogResponse.setCandidateName(x.getUser().getUsername());
                applicationLogResponse.setApplyDate(x.getTimestamp());
                applicationLogResponse.setNote(x.getNote());

                HistoryDto history = historyService.getHistoryById(x.getCv());
                if (Objects.nonNull(history)){
                    HashMap map = new HashMap();
                    map.put("id", history.getId());
                    Optional<Cv> cv = cvRepository.findById(history.getCvId());
                    if (Objects.isNull(cv)){
                        throw new InternalServerException("cvId not match with historyId");
                    }
                    map.put("resumeName", cv.get().getResumeName());
                    applicationLogResponse.getCvs().add(map);
                }
                Optional<HistoryCoverLetter> historyCoverLetterOptional = historyCoverLetterRepository.findById(x.getCv());
                if (historyCoverLetterOptional.isPresent()){
                    HistoryCoverLetter historyCoverLetter = historyCoverLetterOptional.get();
                    HashMap map = new HashMap();
                    map.put("id", historyCoverLetter.getId());
                    map.put("title", historyCoverLetter.getTitle());
                    applicationLogResponse.getCoverLetters().add(map);
                }
                return applicationLogResponse;
            }).collect(Collectors.toList());
        }
        return newList;
    }
}
