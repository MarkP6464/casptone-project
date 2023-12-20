package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ReviewRequestMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewRequestServiceImpl extends AbstractBaseService<ReviewRequest, ReviewRequestDto, Integer> implements ReviewRequestService {

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    PriceOptionRepository priceOptionRepository;

    @Autowired
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ReviewRequestMapper reviewRequestMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvService cvService;

    @Autowired
    ExpertService expertService;

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

    @Autowired
    TransactionService transactionService;

    @Autowired
    HistoryService historyService;

    public ReviewRequestServiceImpl(ReviewRequestRepository reviewRequestRepository, ReviewRequestMapper reviewRequestMapper) {
        super(reviewRequestRepository, reviewRequestMapper, reviewRequestRepository::findById);
        this.reviewRequestRepository = reviewRequestRepository;
        this.reviewRequestMapper = reviewRequestMapper;
    }

    @Override
    public String createReviewRequest(Integer cvId, Integer expertId, Integer optionId, ReviewRequestAddDto dto) throws JsonProcessingException {
        ReviewRequest reviewRequest = modelMapper.map(dto,ReviewRequest.class);
        Cv cv = cvService.getCvById(cvId);
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId,RoleType.EXPERT);
        if(expertOptional.isPresent() && expertOptional.get().getPrice()!=null){
            Expert expert = expertOptional.get();
            if(expert.getPunish()){
                throw new BadRequestException("This expert is currently being punished, please submit a review request later.");
            }else{
                Optional<PriceOption> priceOptionOptional = priceOptionRepository.findByExpertIdAndId(expert.getId(), optionId);
                if(priceOptionOptional.isPresent()){
                    PriceOption price = priceOptionOptional.get();

                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

                    // Sử dụng Calendar để thêm 2 ngày
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentTimestamp);
                    calendar.add(Calendar.DAY_OF_MONTH, price.getDay());

                    // Lấy Timestamp sau khi cộng thêm
                    Timestamp newTimestamp = new Timestamp(calendar.getTimeInMillis());
                    if (cv != null) {
                        historyService.create(cv.getUser().getId(), cv.getId());
                        List<History> histories = historyRepository.findAllByCv_IdAndCv_StatusOrderByTimestampDesc(cvId, BasicStatus.ACTIVE);
                        if(histories!=null){
                            History history = histories.get(0);
                            if (usersOptional.isPresent()) {
                                Users users = usersOptional.get();
                                reviewRequest.setReceivedDate(currentTimestamp);
                                reviewRequest.setDeadline(newTimestamp);
                                reviewRequest.setNote(dto.getNote());
                                reviewRequest.setPrice(price.getPrice());
                                reviewRequest.setStatus(StatusReview.Waiting);
                                reviewRequest.setExpertId(users.getId());
                                reviewRequest.setHistoryId(history.getId());
                                reviewRequest.setCv(cv);
                                TransactionDto dto1 = transactionService.requestToReview(cv.getUser().getId(), reviewRequest.getExpertId(), Double.valueOf(reviewRequest.getPrice()));
                                reviewRequest.setTransaction(transactionService.getById(dto1.getId()));
                                reviewRequestRepository.save(reviewRequest);
                                reviewResponseService.createReviewResponse(reviewRequest.getHistoryId(), reviewRequest.getId());
//                                sendEmail(users.getEmail(), "Review Request Created", "Your review request has been created successfully.");
                                return "Send request successful.";
                            } else {
                                throw new BadRequestException("Expert ID not found");
                            }
                        }else{
                            throw new BadRequestException("Please syn previous when send request");
                        }
                    } else {
                        throw new BadRequestException("CV ID not found");
                    }
                }else{
                    throw new BadRequestException("That option id does not exist in this expert.");
                }
            }
        }
        throw new BadRequestException("Please choose someone else, this expert does not have a specific price yet.");
    }

    @Override
    public String acceptReviewRequest(Integer expertId, Integer requestId) throws JsonProcessingException {
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByExpertIdAndId(expertId,requestId);
        if (reviewRequestOptional.isPresent()) {
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            if(reviewRequest.getStatus()==StatusReview.Waiting){
                reviewRequest.setStatus(StatusReview.Accept);
                reviewRequestRepository.save(reviewRequest);

                Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_Id(reviewRequest.getId());
                if(reviewResponseOptional.isPresent()){
                    ReviewResponse response = reviewResponseOptional.get();
                    response.setStatus(StatusReview.Accept);
                    reviewResponseRepository.save(response);
                }
//                sendEmail(reviewRequest.getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                return "Accept successful";
            }else{
                return "You can only accept requests that have a status of Waiting.";
            }
        } else {
            throw new RuntimeException("Expert ID incorrect or Request ID incorrect");
        }
    }

    @Override
    public List<ReviewRequestSecondViewDto> getListReviewRequest(Integer expertId, String sortBy, String sortOrder, String searchTerm) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByExpertId(expertId);

        if (reviewRequests != null && !reviewRequests.isEmpty()) {
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

                if (reviewRequest.getStatus() == StatusReview.Processing) {
                    reviewRequestViewDto.setStatus(StatusReview.Draft);
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

            List<ReviewRequestSecondViewDto> reviewRequestReturn = new ArrayList<>();
            for(ReviewRequestViewDto reviewRequestViewDto: reviewRequestDtos){
                ReviewRequestSecondViewDto reviewRequestSecondViewDto = new ReviewRequestSecondViewDto();
                reviewRequestSecondViewDto.setId(reviewRequestViewDto.getId());
                reviewRequestSecondViewDto.setResumeName(reviewRequestViewDto.getResumeName());
                reviewRequestSecondViewDto.setAvatar(reviewRequestViewDto.getAvatar());
                reviewRequestSecondViewDto.setName(reviewRequestViewDto.getName());
                reviewRequestSecondViewDto.setNote(reviewRequestViewDto.getNote());
                reviewRequestSecondViewDto.setPrice(formatPrice(reviewRequestViewDto.getPrice()));
                reviewRequestSecondViewDto.setStatus(reviewRequestViewDto.getStatus());
                reviewRequestSecondViewDto.setReceivedDate(reviewRequestViewDto.getReceivedDate());
                reviewRequestSecondViewDto.setDeadline(reviewRequestViewDto.getDeadline());
                reviewRequestReturn.add(reviewRequestSecondViewDto);
            }

            return reviewRequestReturn;
        } else {
            throw new BadRequestException("Currently no results were found in your system.");
        }
    }

    @Override
    public List<ReviewRequestHistorySecondViewDto> getListReviewRequestHistory(Integer expertId, String sortBy, String sortOrder, String searchTerm) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByExpertIdAndStatus(expertId, StatusReview.Done);

        if (reviewRequests != null && !reviewRequests.isEmpty()) {
            List<ReviewRequestHistoryViewDto> reviewRequestDtos = new ArrayList<>();

            for (ReviewRequest reviewRequest : reviewRequests) {
                ReviewRequestHistoryViewDto reviewRequestViewDto = new ReviewRequestHistoryViewDto();

                // Set properties from ReviewRequest to ReviewRequestDto
                reviewRequestViewDto.setId(reviewRequest.getId());
                reviewRequestViewDto.setResumeName(reviewRequest.getCv().getResumeName());
                reviewRequestViewDto.setCandidate(reviewRequest.getCv().getUser().getName());
                reviewRequestViewDto.setPrice(reviewRequest.getPrice());
                Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_Id(reviewRequest.getId());
                if(reviewResponseOptional.isPresent()){
                    ReviewResponse response = reviewResponseOptional.get();
                    reviewRequestViewDto.setStar(response.getScore());
                    reviewRequestViewDto.setResponse(response.getComment());
                }
                reviewRequestViewDto.setReceivedDate(reviewRequest.getReceivedDate());
                reviewRequestDtos.add(reviewRequestViewDto);
            }

            // Sort the list based on the specified field and order if provided
            if (sortBy != null && !sortBy.trim().isEmpty() && sortOrder != null && !sortOrder.trim().isEmpty()) {
                sortReviewRequestListHistory(reviewRequestDtos, sortBy, sortOrder);
            }

            // Format dates using PrettyTime after sorting
            for (ReviewRequestHistoryViewDto dto : reviewRequestDtos) {
                dto.setReceivedDate(dto.getReceivedDate());
            }

            // Apply search filter if searchTerm is provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                reviewRequestDtos = filterBySearchTermHistory(reviewRequestDtos, searchTerm);
            }

            List<ReviewRequestHistorySecondViewDto> reviewRequestReturn = new ArrayList<>();
            for(ReviewRequestHistoryViewDto reviewRequestViewDto: reviewRequestDtos){
                ReviewRequestHistorySecondViewDto reviewRequestSecondViewDto = new ReviewRequestHistorySecondViewDto();
                reviewRequestSecondViewDto.setId(reviewRequestViewDto.getId());
                reviewRequestSecondViewDto.setResumeName(reviewRequestViewDto.getResumeName());
                reviewRequestSecondViewDto.setCandidate(reviewRequestViewDto.getCandidate());
                reviewRequestSecondViewDto.setPrice(formatPrice(reviewRequestViewDto.getPrice()));
                reviewRequestSecondViewDto.setStar(reviewRequestViewDto.getStar());
                reviewRequestSecondViewDto.setResponse(reviewRequestViewDto.getResponse());
                reviewRequestSecondViewDto.setReceivedDate(reviewRequestViewDto.getReceivedDate());
                reviewRequestReturn.add(reviewRequestSecondViewDto);
            }

            return reviewRequestReturn;
        } else {
            throw new BadRequestException("Currently no results were found in your system.");
        }
    }

    @Override
    public List<ReviewRequestCandidateSecondViewDto> getListReviewRequestCandidate(Integer userId, String sortBy, String sortOrder, String searchTerm) {
        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByCv_User_Id(userId);

        if (reviewRequests != null && !reviewRequests.isEmpty()) {
            List<ReviewRequestViewDto> reviewRequestDtos = new ArrayList<>();

            for (ReviewRequest reviewRequest : reviewRequests) {
                ReviewRequestViewDto reviewRequestViewDto = new ReviewRequestViewDto();

                // Set properties from ReviewRequest to ReviewRequestDto
                reviewRequestViewDto.setId(reviewRequest.getId());
                reviewRequestViewDto.setResumeName(reviewRequest.getCv().getResumeName());
                reviewRequestViewDto.setAvatar(reviewRequest.getCv().getUser().getAvatar());
                Optional<Users> usersOptional = usersRepository.findUsersById(reviewRequest.getExpertId());
                if(usersOptional.isPresent()){
                    Users users = usersOptional.get();
                    reviewRequestViewDto.setName(users.getName());
                }
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

            List<ReviewRequestCandidateSecondViewDto> reviewRequestReturn = new ArrayList<>();
            for(ReviewRequestViewDto reviewRequestViewDto: reviewRequestDtos){
                ReviewRequestCandidateSecondViewDto reviewRequestSecondViewDto = new ReviewRequestCandidateSecondViewDto();
                reviewRequestSecondViewDto.setId(reviewRequestViewDto.getId());
                reviewRequestSecondViewDto.setResumeName(reviewRequestViewDto.getResumeName());
                reviewRequestSecondViewDto.setExpert(reviewRequestViewDto.getName());
                reviewRequestSecondViewDto.setNote(reviewRequestViewDto.getNote());
                reviewRequestSecondViewDto.setPrice(formatPrice(reviewRequestViewDto.getPrice()));
                reviewRequestSecondViewDto.setStatus(reviewRequestViewDto.getStatus());
                reviewRequestSecondViewDto.setReceivedDate(reviewRequestViewDto.getReceivedDate());
                reviewRequestSecondViewDto.setDeadline(reviewRequestViewDto.getDeadline());
                reviewRequestReturn.add(reviewRequestSecondViewDto);
            }

            return reviewRequestReturn;
        } else {
            throw new BadRequestException("Currently no results were found in your system.");
        }
    }

    @Override
    public String rejectReviewRequest(Integer expertId, Integer requestId) {
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByExpertIdAndId(expertId,requestId);
        if (reviewRequestOptional.isPresent()) {
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            reviewRequest.setStatus(StatusReview.Reject);
            reviewRequestRepository.save(reviewRequest);

            Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_Id(reviewRequest.getId());
            if(reviewResponseOptional.isPresent()){
                ReviewResponse response = reviewResponseOptional.get();
                response.setStatus(StatusReview.Reject);
                reviewResponseRepository.save(response);
            }
//            sendEmail(reviewRequest.getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
            transactionService.requestToReviewFail(reviewRequest.getTransaction().getId().toString());
            return "Reject successful";
        } else {
            throw new RuntimeException("Expert ID incorrect or Request ID incorrect");
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

    private List<ReviewRequestHistoryViewDto> filterBySearchTermHistory(List<ReviewRequestHistoryViewDto> reviewRequestDtos, String searchTerm) {
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

    private void sortReviewRequestListHistory(List<ReviewRequestHistoryViewDto> reviewRequestDtos, String sortBy, String sortOrder) {
        Comparator<ReviewRequestHistoryViewDto> comparator = null;

        switch (sortBy) {
            case "price":
                comparator = Comparator.comparing(ReviewRequestHistoryViewDto::getPrice);
                break;
            case "receivedDate":
                comparator = Comparator.comparing(ReviewRequestHistoryViewDto::getReceivedDate);
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
            reviewRequestRepository.save(reviewRequest);
            if(reviewRequestRepository.countByExpertIdAndStatus(reviewRequest.getExpertId(), StatusReview.Overdue)>=3){
                expertService.punishExpert(reviewRequest.getExpertId());
            }
        }
        expertService.unPunishExpert();
    }
//    private void sendEmail(String toEmail, String subject, String message) {
//        // Cấu hình thông tin SMTP
//        String host = "smtp.gmail.com";
//        String username = "cvbuilder.ai@gmail.com";
//        String password = "cvbtldosldixpkeh";
//
//        // Cấu hình các thuộc tính cho session
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.starttls.enable", "true");
//
//        // Tạo một phiên gửi email
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            MimeMessage mimeMessage = new MimeMessage(session);
//
//            mimeMessage.setFrom(new InternetAddress(username));
//
//            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//
//            mimeMessage.setSubject(subject);
//
//            mimeMessage.setText(message);
//
//            Transport.send(mimeMessage);
//
//            System.out.println("Email sent successfully.");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to send email.");
//        }
//    }

    public static String formatPrice(long price) {
        String priceStr = String.valueOf(price);

        int length = priceStr.length();
        StringBuilder formattedPrice = new StringBuilder();

        for (int i = length - 1; i >= 0; i--) {
            formattedPrice.insert(0, priceStr.charAt(i));

            // Insert a dot after every 3 digits, but not at the beginning
            if ((length - i) % 3 == 0 && i != 0) {
                formattedPrice.insert(0, ".");
            }
        }

        return formattedPrice.toString();
    }

}
