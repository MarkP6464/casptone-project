package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.responses.ReviewRequestSecondViewDto;
import com.example.capstoneproject.Dto.responses.ReviewRequestViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ReviewRequestMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.ReviewRequestService;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewRequestServiceImpl extends AbstractBaseService<ReviewRequest, ReviewRequestDto, Integer> implements ReviewRequestService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ReviewRequestMapper reviewRequestMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvService cvService;

    @Autowired
    PrettyTime prettyTime;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ReviewResponseService reviewResponseService;

    @Autowired
    HistoryRepository historyRepository;

    public ReviewRequestServiceImpl(ReviewRequestRepository reviewRequestRepository, ReviewRequestMapper reviewRequestMapper) {
        super(reviewRequestRepository, reviewRequestMapper, reviewRequestRepository::findById);
        this.reviewRequestRepository = reviewRequestRepository;
        this.reviewRequestMapper = reviewRequestMapper;
    }

    @Override
    public ReviewRequestDto createReviewRequest(Integer cvId, Integer expertId, ReviewRequestAddDto dto) throws JsonProcessingException {
        ReviewRequest reviewRequest = modelMapper.map(dto,ReviewRequest.class);
        Cv cv = cvService.getCvById(cvId);
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(expertId,RoleType.EXPERT);
        ReviewRequest saved;
        if(expertOptional.isPresent() && expertOptional.get().getPrice()!=null){
            if (cv != null) {
                List<History> histories = historyRepository.findAllByCv_IdAndCv_StatusOrderByTimestampDesc(cvId, BasicStatus.ACTIVE);
                if(histories!=null){
                    History history = histories.get(0);
                    if (usersOptional.isPresent()) {
                        Users users = usersOptional.get();
                        reviewRequest.setReceivedDate(dto.getDeadline());
                        reviewRequest.setNote(dto.getNote());
                        reviewRequest.setPrice(dto.getPrice());
                        reviewRequest.setStatus(StatusReview.Waiting);
                        reviewRequest.setExpertId(users.getId());
                        reviewRequest.setHistoryId(history.getId());
                        reviewRequest.setCv(cv);
                        saved = reviewRequestRepository.save(reviewRequest);
                        acceptReviewRequest(expertId, saved.getId());
                        sendEmail(users.getEmail(), "Review Request Created", "Your review request has been created successfully.");
                        return reviewRequestMapper.mapEntityToDto(saved);
                    } else {
                        throw new BadRequestException("Expert ID not found");
                    }
                }else{
                    throw new BadRequestException("Please syn previous when send request");
                }
            } else {
                throw new BadRequestException("CV ID not found");
            }
        }
        throw new BadRequestException("Please choose someone else, this expert does not have a specific price yet.");
    }

    public void acceptReviewRequest(Integer expertId, Integer requestId) throws JsonProcessingException {
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByExpertIdAndId(expertId,requestId);
        if (reviewRequestOptional.isPresent()) {
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            reviewResponseService.createReviewResponse(reviewRequest.getCv().getId(), reviewRequest.getId());
            sendEmail(reviewRequest.getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
        } else {
            throw new RuntimeException("Expert ID incorrect or Request ID incorrect");
        }
    }
//    @Override
//    public List<ReviewRequestDto> getAllReviewRequest(Integer expertId, ReviewStatus reviewStatus, String orderByDate) {
//        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByExpertIdAndStatus(expertId, reviewStatus);
//
//        return reviewRequests.stream()
//                .map(project -> {
//                    ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
//                    reviewRequestDto.setId(project.getId());
//                    reviewRequestDto.setReceivedDate(project.getReceivedDate());
//                    reviewRequestDto.setStatus(project.getStatus());
//                    reviewRequestDto.setNote(project.getNote());
//                    reviewRequestDto.setExpertId(project.getExpertId());
//                    reviewRequestDto.setCv(project.getCv());
//                    return reviewRequestDto;
//                })
//                .sorted(orderByDate.equalsIgnoreCase("desc") ? Comparator.comparing(ReviewRequestDto::getReceivedDate).reversed() : Comparator.comparing(ReviewRequestDto::getReceivedDate))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<ReviewRequestSecondViewDto> getListReviewRequest(Integer expertId, String sortBy, String sortOrder, String searchTerm) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByExpertId(expertId);

        if (reviewRequests != null && !reviewRequests.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            List<ReviewRequestViewDto> reviewRequestDtos = new ArrayList<>();

            for (ReviewRequest reviewRequest : reviewRequests) {
                ReviewRequestViewDto reviewRequestViewDto = new ReviewRequestViewDto();

                // Set properties from ReviewRequest to ReviewRequestDto
                reviewRequestViewDto.setId(reviewRequest.getId());
                reviewRequestViewDto.setResumeName(reviewRequest.getCv().getResumeName());
                reviewRequestViewDto.setAvatar(reviewRequest.getCv().getUser().getAvatar());
                reviewRequestViewDto.setName(reviewRequest.getCv().getUser().getName());
                reviewRequestViewDto.setNote(reviewRequest.getNote());
                reviewRequestViewDto.setPrice(reviewRequest.getPrice());

                if (reviewRequest.getStatus() == StatusReview.Waiting) {
                    Optional<ReviewResponse> reviewResponse = reviewResponseRepository.findByReviewRequest_Id(reviewRequest.getId());
                    if (reviewResponse.isPresent()) {
                        ReviewResponse response = reviewResponse.get();
                        if (response.getStatus() == StatusReview.Draft) {
                            reviewRequestViewDto.setStatus(StatusReview.Draft);
                        } else {
                            reviewRequestViewDto.setStatus(StatusReview.Waiting);
                        }
                    } else {
                        throw new BadRequestException("Request ID not found in review response");
                    }
                }else{
                    reviewRequestViewDto.setStatus(reviewRequest.getStatus());
                }

                reviewRequestViewDto.setReceivedDate(reviewRequest.getReceivedDate());
                reviewRequestViewDto.setDeadline(reviewRequest.getDeadline());
                reviewRequestDtos.add(reviewRequestViewDto);
            }

            // Sort the list based on the specified field and order if provided
            if (sortBy != null && !sortBy.trim().isEmpty() && sortOrder != null && !sortOrder.trim().isEmpty()) {
                sortReviewRequestList(reviewRequestDtos, sortBy, sortOrder);
            }

            // Format dates using PrettyTime after sorting
            for (ReviewRequestViewDto dto : reviewRequestDtos) {
                dto.setReceivedDate(dto.getReceivedDate());
                dto.setDeadline(dto.getDeadline());
            }

            // Apply search filter if searchTerm is provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                reviewRequestDtos = filterBySearchTerm(reviewRequestDtos, searchTerm);
            }

            List<ReviewRequestSecondViewDto> secondViewDto = new ArrayList<>();
            for (ReviewRequestViewDto reviewRequest : reviewRequestDtos) {
                ReviewRequestSecondViewDto reviewRequestViewSeDto = new ReviewRequestSecondViewDto();
                reviewRequestViewSeDto.setId(reviewRequest.getId());
                reviewRequestViewSeDto.setResumeName(reviewRequest.getResumeName());
                reviewRequestViewSeDto.setAvatar(reviewRequest.getAvatar());
                reviewRequestViewSeDto.setName(reviewRequest.getName());
                reviewRequestViewSeDto.setNote(reviewRequest.getNote());
                reviewRequestViewSeDto.setPrice(reviewRequest.getPrice());
                reviewRequestViewSeDto.setStatus(reviewRequest.getStatus());
                reviewRequestViewSeDto.setReceivedDate(prettyTime.format(reviewRequest.getReceivedDate()));
                reviewRequestViewSeDto.setDeadline(prettyTime.format(reviewRequest.getDeadline()));
                // Ánh xạ dữ liệu từ reviewRequest sang reviewRequestViewDto (giống như trước)
                secondViewDto.add(reviewRequestViewSeDto);
            }


            return secondViewDto;
        } else {
            throw new BadRequestException("Currently no results were found in your system.");
        }
    }

    @Override
    public List<ReviewRequestSecondViewDto> getListReviewRequestCandidate(Integer userId, String sortBy, String sortOrder, String searchTerm) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByCv_User_Id(userId);

        if (reviewRequests != null && !reviewRequests.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            List<ReviewRequestViewDto> reviewRequestDtos = new ArrayList<>();

            for (ReviewRequest reviewRequest : reviewRequests) {
                ReviewRequestViewDto reviewRequestViewDto = new ReviewRequestViewDto();

                // Set properties from ReviewRequest to ReviewRequestDto
                reviewRequestViewDto.setId(reviewRequest.getId());
                reviewRequestViewDto.setResumeName(reviewRequest.getCv().getResumeName());
                reviewRequestViewDto.setAvatar(reviewRequest.getCv().getUser().getAvatar());
                reviewRequestViewDto.setName(reviewRequest.getCv().getUser().getName());
                reviewRequestViewDto.setNote(reviewRequest.getNote());
                reviewRequestViewDto.setPrice(reviewRequest.getPrice());
                reviewRequestViewDto.setStatus(reviewRequest.getStatus());
                reviewRequestViewDto.setReceivedDate(reviewRequest.getReceivedDate());
                reviewRequestViewDto.setDeadline(reviewRequest.getDeadline());
                reviewRequestDtos.add(reviewRequestViewDto);
            }

            // Sort the list based on the specified field and order if provided
            if (sortBy != null && !sortBy.trim().isEmpty() && sortOrder != null && !sortOrder.trim().isEmpty()) {
                sortReviewRequestList(reviewRequestDtos, sortBy, sortOrder);
            }

            // Format dates using PrettyTime after sorting
            for (ReviewRequestViewDto dto : reviewRequestDtos) {
                dto.setReceivedDate(dto.getReceivedDate());
                dto.setDeadline(dto.getDeadline());
            }

            // Apply search filter if searchTerm is provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                reviewRequestDtos = filterBySearchTerm(reviewRequestDtos, searchTerm);
            }

            List<ReviewRequestSecondViewDto> secondViewDto = new ArrayList<>();
            for (ReviewRequestViewDto reviewRequest : reviewRequestDtos) {
                ReviewRequestSecondViewDto reviewRequestViewSeDto = new ReviewRequestSecondViewDto();
                reviewRequestViewSeDto.setId(reviewRequest.getId());
                reviewRequestViewSeDto.setResumeName(reviewRequest.getResumeName());
                reviewRequestViewSeDto.setAvatar(reviewRequest.getAvatar());
                reviewRequestViewSeDto.setName(reviewRequest.getName());
                reviewRequestViewSeDto.setNote(reviewRequest.getNote());
                reviewRequestViewSeDto.setPrice(reviewRequest.getPrice());
                reviewRequestViewSeDto.setStatus(reviewRequest.getStatus());
                reviewRequestViewSeDto.setReceivedDate(prettyTime.format(reviewRequest.getReceivedDate()));
                reviewRequestViewSeDto.setDeadline(prettyTime.format(reviewRequest.getDeadline()));
                // Ánh xạ dữ liệu từ reviewRequest sang reviewRequestViewDto (giống như trước)
                secondViewDto.add(reviewRequestViewSeDto);
            }


            return secondViewDto;
        } else {
            throw new BadRequestException("Currently no results were found in your system.");
        }
    }

    private List<ReviewRequestViewDto> filterBySearchTerm(List<ReviewRequestViewDto> reviewRequestDtos, String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // Case-insensitive search by ResumeName
            String searchTermLowerCase = searchTerm.toLowerCase();
            reviewRequestDtos = reviewRequestDtos.stream()
                    .filter(dto -> dto.getResumeName().toLowerCase().contains(searchTermLowerCase))
                    .collect(Collectors.toList());
        }
        return reviewRequestDtos;
    }

    private void sortReviewRequestList(List<ReviewRequestViewDto> reviewRequestDtos, String sortBy, String sortOrder) {
        Comparator<ReviewRequestViewDto> comparator = null;

        switch (sortBy) {
            case "price":
                comparator = Comparator.comparing(ReviewRequestViewDto::getPrice);
                break;
            case "receivedDate":
                comparator = Comparator.comparing(ReviewRequestViewDto::getReceivedDate);
                break;
            case "deadline":
                comparator = Comparator.comparing(ReviewRequestViewDto::getDeadline);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter");
        }

        if ("asc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        // Apply the comparator to the list
        Collections.sort(reviewRequestDtos, comparator);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRequestReviews() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByDeadline(currentDateTime);
        for (ReviewRequest reviewRequest : reviewRequests) {
            reviewRequest.setStatus(StatusReview.Overdue);
        }
        reviewRequestRepository.saveAll(reviewRequests);
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
