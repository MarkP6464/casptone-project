package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.entity.ApplicationLog;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.repository.ApplicationLogRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.service.ApplicationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

@Service
public class ApplicationLogServiceImpl implements ApplicationLogService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ApplicationLogRepository applicationLogRepository;
    @Override
    public boolean applyCvToPost(Integer userId, Integer cvId, Integer postingId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        ApplicationLog applicationLog = new ApplicationLog();
        LocalDate currentDate = LocalDate.now();
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, BasicStatus.PUBLIC);
            if(jobPostingOptional.isPresent()){
                JobPosting jobPosting = jobPostingOptional.get();
                applicationLog.setTimestamp(currentDate);
//                applicationLog.setCv(cv);
                applicationLog.setJobPosting(jobPosting);
                applicationLogRepository.save(applicationLog);
                sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                return true;
            }else{
                throw new RuntimeException("Posting ID not exist");
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
