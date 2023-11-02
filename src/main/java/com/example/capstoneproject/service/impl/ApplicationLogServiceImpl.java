package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ApplicationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
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
    CoverLetterRepository coverLetterRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        ApplicationLog applicationLog = new ApplicationLog();
        LocalDate currentDate = LocalDate.now();
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findByUser_IdAndId(userId,coverLetterId);
            if(coverLetterOptional.isPresent()){
                CoverLetter coverLetter = coverLetterOptional.get();
                CoverLetterApplyDto coverLetterApplyDto = new CoverLetterApplyDto();
                coverLetterApplyDto.toCoverLetterBody(modelMapper.map(coverLetter, CoverLetterDto.class));
                Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, BasicStatus.PUBLIC);
                if(jobPostingOptional.isPresent()){
                    JobPosting jobPosting = jobPostingOptional.get();
                    Integer condition = jobPosting.getApplyAgain();
                    List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(userId,postingId);
                    if(applicationLogs!=null){
                        ApplicationLog applicationLogCheck = applicationLogs.get(0);
                        LocalDate countDate = applicationLogCheck.getTimestamp();
                        LocalDate resultDate = countDate.plusDays(condition);
                        if(resultDate.isBefore(currentDate) || resultDate.isEqual(currentDate)){
                            applicationLog.setTimestamp(currentDate);
                            CvBodyDto cvBodyDto = cv.deserialize();
                            CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
                            cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
                            cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
                            cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
                            cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
                            cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
                            cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
                            cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
                            cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
                            cvBodyReviewDto.setSummary(cv.getSummary());
                            cvBodyReviewDto.setName(cv.getUser().getName());
                            cvBodyReviewDto.setAddress(cv.getUser().getAddress());
                            cvBodyReviewDto.setPhone(cv.getUser().getPhone());
                            cvBodyReviewDto.setPersonalWebsite(cv.getUser().getPersonalWebsite());
                            cvBodyReviewDto.setEmail(cv.getUser().getEmail());
                            cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());
                            // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
                            ObjectMapper objectMapper = new ObjectMapper();
                            String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
                            applicationLog.setCv(cvBodyReviewJson);
                            applicationLog.setCoverLetter(coverLetterApplyDto.getCoverLetterApply());
                            applicationLog.setJobPosting(jobPosting);
                            Optional<Users> usersOptional = usersRepository.findUsersById(userId);
                            if (usersOptional.isPresent()){
                                Users user = usersOptional.get();
                                applicationLog.setUser(user);
                            }
                            applicationLogRepository.save(applicationLog);
                            sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                            return true;
                        }else{
                            throw new RuntimeException("Please apply after date " + resultDate);
                        }
                    }else{
                        applicationLog.setTimestamp(currentDate);
                        CvBodyDto cvBodyDto = cv.deserialize();
                        CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
                        cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
                        cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
                        cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
                        cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
                        cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
                        cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
                        cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
                        cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
                        cvBodyReviewDto.setSummary(cv.getSummary());
                        cvBodyReviewDto.setName(cv.getUser().getName());
                        cvBodyReviewDto.setAddress(cv.getUser().getAddress());
                        cvBodyReviewDto.setPhone(cv.getUser().getPhone());
                        cvBodyReviewDto.setPersonalWebsite(cv.getUser().getPersonalWebsite());
                        cvBodyReviewDto.setEmail(cv.getUser().getEmail());
                        cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());
                        // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
                        applicationLog.setCv(cvBodyReviewJson);
                        applicationLog.setCoverLetter(coverLetterApplyDto.getCoverLetterApply());
                        applicationLog.setJobPosting(jobPosting);
                        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
                        if (usersOptional.isPresent()){
                            Users user = usersOptional.get();
                            applicationLog.setUser(user);
                        }
                        applicationLogRepository.save(applicationLog);
                        sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                        return true;
                    }
                }else{
                    throw new RuntimeException("Posting ID not exist");
                }
            }else{
                Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, BasicStatus.PUBLIC);
                if(jobPostingOptional.isPresent()){
                    JobPosting jobPosting = jobPostingOptional.get();
                    Integer condition = jobPosting.getApplyAgain();
                    List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(userId,postingId);
                    if(applicationLogs!=null){
                        ApplicationLog applicationLogCheck = applicationLogs.get(0);
                        LocalDate countDate = applicationLogCheck.getTimestamp();
                        LocalDate resultDate = countDate.plusDays(condition);
                        if(resultDate.isBefore(currentDate) || resultDate.isEqual(currentDate)){
                            applicationLog.setTimestamp(currentDate);
                            CvBodyDto cvBodyDto = cv.deserialize();
                            CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
                            cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
                            cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
                            cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
                            cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
                            cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
                            cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
                            cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
                            cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
                            cvBodyReviewDto.setSummary(cv.getSummary());
                            cvBodyReviewDto.setName(cv.getUser().getName());
                            cvBodyReviewDto.setAddress(cv.getUser().getAddress());
                            cvBodyReviewDto.setPhone(cv.getUser().getPhone());
                            cvBodyReviewDto.setPersonalWebsite(cv.getUser().getPersonalWebsite());
                            cvBodyReviewDto.setEmail(cv.getUser().getEmail());
                            cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());
                            // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
                            ObjectMapper objectMapper = new ObjectMapper();
                            String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
                            applicationLog.setCv(cvBodyReviewJson);
                            applicationLog.setJobPosting(jobPosting);
                            Optional<Users> usersOptional = usersRepository.findUsersById(userId);
                            if (usersOptional.isPresent()){
                                Users user = usersOptional.get();
                                applicationLog.setUser(user);
                            }
                            applicationLogRepository.save(applicationLog);
                            sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                            return true;
                        }else{
                            throw new RuntimeException("Please apply after date " + resultDate);
                        }
                    }else{
                        applicationLog.setTimestamp(currentDate);
                        CvBodyDto cvBodyDto = cv.deserialize();
                        CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
                        cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
                        cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
                        cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
                        cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
                        cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
                        cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
                        cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
                        cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
                        cvBodyReviewDto.setSummary(cv.getSummary());
                        cvBodyReviewDto.setName(cv.getUser().getName());
                        cvBodyReviewDto.setAddress(cv.getUser().getAddress());
                        cvBodyReviewDto.setPhone(cv.getUser().getPhone());
                        cvBodyReviewDto.setPersonalWebsite(cv.getUser().getPersonalWebsite());
                        cvBodyReviewDto.setEmail(cv.getUser().getEmail());
                        cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());
                        // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
                        applicationLog.setCv(cvBodyReviewJson);
                        applicationLog.setJobPosting(jobPosting);
                        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
                        if (usersOptional.isPresent()){
                            Users user = usersOptional.get();
                            applicationLog.setUser(user);
                        }
                        applicationLogRepository.save(applicationLog);
                        sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                        return true;
                    }
                }else{
                    throw new RuntimeException("Posting ID not exist");
                }
            }
        }else{
            throw new RuntimeException("CV ID not exist in User ID.");
        }
    }
    private void sendEmail(String toEmail, String subject, String message) {
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
}
