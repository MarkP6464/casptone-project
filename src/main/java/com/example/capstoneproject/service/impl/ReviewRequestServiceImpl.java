package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.ReviewRequestMapper;
import com.example.capstoneproject.repository.ReviewRequestRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.ReviewRequestService;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class ReviewRequestServiceImpl extends AbstractBaseService<ReviewRequest, ReviewRequestDto, Integer> implements ReviewRequestService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    ReviewRequestMapper reviewRequestMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvService cvService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ReviewResponseService reviewResponseService;

    public ReviewRequestServiceImpl(ReviewRequestRepository reviewRequestRepository, ReviewRequestMapper reviewRequestMapper) {
        super(reviewRequestRepository, reviewRequestMapper, reviewRequestRepository::findById);
        this.reviewRequestRepository = reviewRequestRepository;
        this.reviewRequestMapper = reviewRequestMapper;
    }

    @Override
    public ReviewRequestDto createReviewRequest(Integer cvId, Integer expertId, ReviewRequestAddDto dto) {
        ReviewRequest reviewRequest = modelMapper.map(dto,ReviewRequest.class);
        Cv cv = cvService.getCvById(cvId);
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(expertId, RoleType.EXPERT);
        ReviewRequest saved;
        if (cv != null) {
            if (usersOptional.isPresent()) {
                Users users = usersOptional.get();
                reviewRequest.setReceivedDate(dto.getReceivedDate());
                reviewRequest.setNote(dto.getNote());
                reviewRequest.setStatus(ReviewStatus.PROCESSING);
                reviewRequest.setExpertId(users.getId());
                reviewRequest.setCv(cv);
                saved = reviewRequestRepository.save(reviewRequest);
                sendEmail(users.getEmail(), "Review Request Created", "Your review request has been created successfully.");
            } else {
                throw new RuntimeException("Expert ID not found");
            }
        } else {
            throw new RuntimeException("CV ID not found");
        }
        return reviewRequestMapper.mapEntityToDto(saved);
    }

    @Override
    public ReviewRequestDto rejectReviewRequest(Integer expertId, Integer requestId) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(expertId, RoleType.EXPERT);
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findReviewRequestByExpertIdAndIdAndStatus(expertId,requestId,ReviewStatus.PROCESSING);
        ReviewRequest saved;
            if (usersOptional.isPresent()) {
                if (reviewRequestOptional.isPresent()) {
                    ReviewRequest reviewRequest = reviewRequestOptional.get();
                    reviewRequest.setStatus(ReviewStatus.REJECT);
                    saved = reviewRequestRepository.save(reviewRequest);
                    sendEmail(reviewRequest.getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                } else {
                    throw new RuntimeException("Expert ID incorrect or Request ID incorrect");
                }
            } else {
                throw new RuntimeException("Expert ID not found");
            }
        return reviewRequestMapper.mapEntityToDto(saved);
    }

    @Override
    public ReviewRequestDto acceptReviewRequest(Integer expertId, Integer requestId) throws JsonProcessingException {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(expertId, RoleType.EXPERT);
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findReviewRequestByExpertIdAndIdAndStatus(expertId,requestId,ReviewStatus.PROCESSING);
        ReviewRequest saved;
        if (usersOptional.isPresent()) {
            if (reviewRequestOptional.isPresent()) {
                ReviewRequest reviewRequest = reviewRequestOptional.get();
                reviewRequest.setStatus(ReviewStatus.ACCEPT);
                saved = reviewRequestRepository.save(reviewRequest);
                reviewResponseService.createReviewResponse(reviewRequest.getCv().getId(), reviewRequest.getId());
                sendEmail(reviewRequest.getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
            } else {
                throw new RuntimeException("Expert ID incorrect or Request ID incorrect");
            }
        } else {
            throw new RuntimeException("Expert ID not found");
        }
        return reviewRequestMapper.mapEntityToDto(saved);
    }

    @Override
    public List<ReviewRequestDto> getAllReviewRequest(Integer expertId, ReviewStatus reviewStatus, String orderByDate) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByExpertIdAndStatus(expertId, reviewStatus);

        return reviewRequests.stream()
                .map(project -> {
                    ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
                    reviewRequestDto.setId(project.getId());
                    reviewRequestDto.setReceivedDate(project.getReceivedDate());
                    reviewRequestDto.setStatus(project.getStatus());
                    reviewRequestDto.setNote(project.getNote());
                    reviewRequestDto.setExpertId(project.getExpertId());
                    reviewRequestDto.setCv(project.getCv());
                    return reviewRequestDto;
                })
                .sorted(orderByDate.equalsIgnoreCase("desc") ? Comparator.comparing(ReviewRequestDto::getReceivedDate).reversed() : Comparator.comparing(ReviewRequestDto::getReceivedDate))
                .collect(Collectors.toList());
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
