package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ReviewResponseMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ReviewResponseService;
import com.example.capstoneproject.service.TransactionService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReviewResponseServiceImpl implements ReviewResponseService {

    @Autowired
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ReviewResponseMapper reviewResponseMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    TransactionService transactionService;

    public static boolean isSubstringInString(String fullString, String substring) {
        int fullLength = fullString.length();
        int subLength = substring.length();

        int[][] dp = new int[fullLength + 1][subLength + 1];

        for (int i = 1; i <= fullLength; i++) {
            for (int j = 1; j <= subLength; j++) {
                if (fullString.charAt(i - 1) == substring.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[fullLength][subLength] == subLength;
    }

    @Override
    public void createReviewResponse(Integer historyId, Integer requestId) throws JsonProcessingException {
//        Cv cv = cvRepository.getById(cvId);
        Optional<History> historyOptional = historyRepository.findById(historyId);
        if(historyOptional.isPresent()){
            History history = historyOptional.get();
            Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByIdAndStatus(requestId, StatusReview.Processing);
            if (reviewRequestOptional.isPresent()) {
                // Lấy CvBody từ CV
                ReviewRequest reviewRequest = reviewRequestOptional.get();
                CvBodyReviewDto cvBodyDto = history.deserialize();

                // Tạo CvBodyReviewDto và thiết lập trường skills
                CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
                cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
                cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
                cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
                cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
                cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
                cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
                cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
                cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
                cvBodyReviewDto.setSummary(cvBodyDto.getSummary());
                cvBodyReviewDto.setName(cvBodyDto.getName());
                cvBodyReviewDto.setAddress(cvBodyDto.getAddress());
                cvBodyReviewDto.setPhone(cvBodyDto.getPhone());
                cvBodyReviewDto.setPersonalWebsite(cvBodyDto.getPersonalWebsite());
                cvBodyReviewDto.setEmail(cvBodyDto.getEmail());
                cvBodyReviewDto.setLinkin(cvBodyDto.getLinkin());

                // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);

                // Tạo một ReviewResponse mới và thiết lập các giá trị
                ReviewResponse reviewResponse = new ReviewResponse();
                reviewResponse.setFeedbackDetail(cvBodyReviewJson);
                reviewResponse.setReviewRequest(reviewRequest);

                // Lưu ReviewResponse để có ID
                reviewResponse = reviewResponseRepository.save(reviewResponse);

                // Thiết lập trường feedbackDetail của ReviewResponse
                reviewResponse.toCvBodyReview(cvBodyReviewDto);
                reviewResponseRepository.save(reviewResponse);
            }
        }else {
            throw new BadRequestException("History ID not found in CV");
        }

    }

    @Override
    public boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expertId, responseId);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getStatus()!= StatusReview.Done){
                CvBodyReviewDto cvBodyReviewDto = reviewResponse.deserialize();
                String sp = removeCommentTagsWithoutIdAndContent(dto.getText());
                cvBodyReviewDto.getExperiences().forEach(x -> {
                    if (isSubstringInString(x.getDescription(), sp)) {
                        try {
                            x.setDescription(dto.getText());
                            reviewResponse.toCvBodyReview(cvBodyReviewDto);
                        } catch (JsonProcessingException e) {
                            throw new BadRequestException(e);
                        }
                        reviewResponseRepository.save(reviewResponse);
                    }
                    String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                    x.setDescription(description);
                });
                cvBodyReviewDto.getInvolvements().forEach(x -> {
                    if (isSubstringInString(x.getDescription(), sp)) {
                        try {
                            x.setDescription(dto.getText());
                            reviewResponse.toCvBodyReview(cvBodyReviewDto);
                        } catch (JsonProcessingException e) {
                            throw new BadRequestException(e);
                        }
                        reviewResponseRepository.save(reviewResponse);
                    }
                    String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                    x.setDescription(description);
                });
                cvBodyReviewDto.getProjects().forEach(x -> {
                    if (isSubstringInString(x.getDescription(), sp)) {
                        try {
                            x.setDescription(dto.getText());
                            reviewResponse.toCvBodyReview(cvBodyReviewDto);
                        } catch (JsonProcessingException e) {
                            throw new BadRequestException(e);
                        }
                        reviewResponseRepository.save(reviewResponse);
                    }
                    String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                    x.setDescription(description);
                });
                cvBodyReviewDto.getSkills().forEach(x -> {
                    if (isSubstringInString(x.getDescription(), sp)) {
                        try {
                            x.setDescription(dto.getText());
                            reviewResponse.toCvBodyReview(cvBodyReviewDto);
                        } catch (JsonProcessingException e) {
                            throw new BadRequestException(e);
                        }
                        reviewResponseRepository.save(reviewResponse);
                    }
                    String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                    x.setDescription(description);
                });
                if (cvBodyReviewDto.getSummary() != null) {
                    if (isSubstringInString(cvBodyReviewDto.getSummary(), sp)) {
                        try {
                            cvBodyReviewDto.setSummary(dto.getText());
                            reviewResponse.toCvBodyReview(cvBodyReviewDto);
                        } catch (JsonProcessingException e) {
                            throw new BadRequestException(e);
                        }
                        reviewResponseRepository.save(reviewResponse);
                    }
                    String description = cvBodyReviewDto.getSummary().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");
                    cvBodyReviewDto.setSummary(description);
                }
                reviewResponse.toCvBodyReview(cvBodyReviewDto);
                reviewResponse.setStatus(StatusReview.Draft);
                reviewResponseRepository.save(reviewResponse);
                return true;
            }else{
                throw new BadRequestException("You dont cant edit this cv.");
            }
        } else {
            throw new BadRequestException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean deleteComment(Integer expertId, Integer responseId, String commentId) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expertId, responseId);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getStatus()!=StatusReview.Done){
                CvBodyReviewDto cvBodyReviewDto = reviewResponse.deserialize();
                cvBodyReviewDto.getExperiences().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "'[^>]*>(.*?)</comment>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì xóa comment đó
                    if (matcher.find()) {
                        description = description.replace(matcher.group(0), matcher.group(1));
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getProjects().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "'[^>]*>(.*?)</comment>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì xóa comment đó
                    if (matcher.find()) {
                        description = description.replace(matcher.group(0), matcher.group(1));
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getInvolvements().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "'[^>]*>(.*?)</comment>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì xóa comment đó
                    if (matcher.find()) {
                        description = description.replace(matcher.group(0), matcher.group(1));
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getSkills().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "'[^>]*>(.*?)</comment>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì xóa comment đó
                    if (matcher.find()) {
                        description = description.replace(matcher.group(0), matcher.group(1));
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                if (cvBodyReviewDto.getSummary() != null) {
                    String description = cvBodyReviewDto.getSummary();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "'[^>]*>(.*?)</comment>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì xóa comment đó
                    if (matcher.find()) {
                        description = description.replace(matcher.group(0), matcher.group(1));
                    }

                    // Cập nhật trường description với chuỗi mới
                    cvBodyReviewDto.setSummary(description);
                }
                reviewResponse.toCvBodyReview(cvBodyReviewDto);
                reviewResponseRepository.save(reviewResponse);
                return true;
            }else{
                throw new BadRequestException("You dont cant edit this cv.");
            }
        } else {
            throw new BadRequestException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean updateComment(Integer expertId, Integer responseId, String commentId, String newContent) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expertId, responseId);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getStatus()!=StatusReview.Done){
                CvBodyReviewDto cvBodyReviewDto = reviewResponse.deserialize();
                cvBodyReviewDto.getExperiences().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "' content='.*?'>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì thay đổi nội dung của comment
                    if (matcher.find()) {
                        String oldComment = matcher.group(0);
                        String newComment = "<comment id='" + commentId + "' content='" + newContent + "'>";
                        description = description.replace(oldComment, newComment);
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getInvolvements().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "' content='.*?'>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì thay đổi nội dung của comment
                    if (matcher.find()) {
                        String oldComment = matcher.group(0);
                        String newComment = "<comment id='" + commentId + "' content='" + newContent + "'>";
                        description = description.replace(oldComment, newComment);
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getProjects().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "' content='.*?'>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì thay đổi nội dung của comment
                    if (matcher.find()) {
                        String oldComment = matcher.group(0);
                        String newComment = "<comment id='" + commentId + "' content='" + newContent + "'>";
                        description = description.replace(oldComment, newComment);
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                cvBodyReviewDto.getSkills().forEach(x -> {
                    String description = x.getDescription();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "' content='.*?'>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì thay đổi nội dung của comment
                    if (matcher.find()) {
                        String oldComment = matcher.group(0);
                        String newComment = "<comment id='" + commentId + "' content='" + newContent + "'>";
                        description = description.replace(oldComment, newComment);
                    }

                    // Cập nhật trường description với chuỗi mới
                    x.setDescription(description);
                });
                if (cvBodyReviewDto.getSummary() != null) {
                    String description = cvBodyReviewDto.getSummary();

                    // Tạo regex pattern để tìm comment có id tương ứng
                    String regex = "<comment id='" + commentId + "' content='.*?'>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(description);

                    // Nếu tìm thấy match, thì thay đổi nội dung của comment
                    if (matcher.find()) {
                        String oldComment = matcher.group(0);
                        String newComment = "<comment id='" + commentId + "' content='" + newContent + "'>";
                        description = description.replace(oldComment, newComment);
                    }

                    // Cập nhật trường description với chuỗi mới
                    cvBodyReviewDto.setSummary(description);
                }
                reviewResponse.toCvBodyReview(cvBodyReviewDto);
                reviewResponseRepository.save(reviewResponse);
                return true;
            }else{
                throw new BadRequestException("You dont cant edit this cv.");
            }
        } else {
            throw new BadRequestException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean updateReviewResponse(Integer expertId, Integer responseId, ReviewResponseUpdateDto dto) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expertId, responseId);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getStatus()!= StatusReview.Done){
                if (dto.getOverall() != null && !dto.getOverall().equals(reviewResponse.getOverall())) {
                    reviewResponse.setOverall(dto.getOverall());
                    reviewResponse.setStatus(StatusReview.Draft);
                    reviewResponseRepository.save(reviewResponse);
                }
                return true;
            }else{
                throw new BadRequestException("You dont cant edit this cv.");
            }
        }
        return false;
    }

    @Override
    public boolean publicReviewResponse(Integer expertId, Integer responseId) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expertId, responseId);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getStatus()!=StatusReview.Done){
                reviewResponse.setStatus(StatusReview.Done);
                reviewResponseRepository.save(reviewResponse);
                Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findById(reviewResponse.getReviewRequest().getId());
                if(reviewRequestOptional.isPresent()){
                    ReviewRequest reviewRequest = reviewRequestOptional.get();
                    reviewRequest.setStatus(StatusReview.Done);
                    reviewRequestRepository.save(reviewRequest);
                    transactionService.requestToReviewSuccessFul(reviewRequest.getTransaction().getRequestId());
                }else{
                    throw new BadRequestException("Update fail status review request.");
                }
                sendEmail(reviewResponse.getReviewRequest().getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
                return true;
            }else{
                throw new BadRequestException("This review response had done");
            }
        }
        return false;
    }

    @Override
    public ReviewResponseDto receiveReviewResponse(Integer userId, Integer requestId) throws JsonProcessingException {
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByIdAndStatus(requestId, StatusReview.Done);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        if (reviewRequestOptional.isPresent()) {
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            if (Objects.equals(userId, reviewRequest.getCv().getUser().getId())) {
                Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_IdAndStatus(reviewRequest.getId(), StatusReview.Done);
                if (reviewResponseOptional.isPresent()) {
                    ReviewResponse reviewResponse = reviewResponseOptional.get();
                    reviewResponseDto.setId(reviewResponse.getId());
                    reviewResponseDto.setFeedbackDetail(reviewResponse.deserialize());
                    reviewResponseDto.setOverall(reviewResponse.getOverall());
                    if(reviewResponse.getScore()!=null){
                        reviewResponseDto.setScore(reviewResponse.getScore());
                    }
                    reviewResponseDto.setComment(reviewResponse.getComment());
                    return reviewResponseDto;
                }
            } else {
                throw new BadRequestException("UserId incorrect with requestId");
            }
        } else {
            throw new BadRequestException("Currently, this review request is still being processed, please come back later.");
        }
        return reviewResponseDto;
    }

    @Override
    public ReviewResponseDto getReviewResponse(Integer expertId, Integer responseId) throws JsonProcessingException {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expert.getId(), responseId);
            if (reviewResponseOptional.isPresent()) {
                ReviewResponse reviewResponse = reviewResponseOptional.get();
                reviewResponseDto.setId(reviewResponse.getId());
                reviewResponseDto.setOverall(reviewResponse.getOverall());
                if(reviewResponse.getScore()!=null){
                    reviewResponseDto.setScore(reviewResponse.getScore());
                }
                reviewResponseDto.setFeedbackDetail(reviewResponse.deserialize());
            }
            return reviewResponseDto;
        } else {
            throw new BadRequestException("Expert ID not found.");
        }
    }

    @Override
    public String sendReviewRating(Integer responseId, ReviewRatingAddDto dto) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByIdAndStatus(responseId, StatusReview.Done);
        LocalDate current = LocalDate.now();
        if(reviewResponseOptional.isPresent()){
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(reviewResponse.getScore()==null && reviewResponse.getComment()==null){
                reviewResponse.setScore(dto.getScore());
                reviewResponse.setComment(dto.getComment());
                reviewResponse.setDateComment(current);
                reviewResponseRepository.save(reviewResponse);
                return "Send feedback successful.";
            }else{
                throw new BadRequestException("You have already commented, it cannot be edited anymore.");
            }
        }else{
            throw new BadRequestException("Response ID not found.");
        }
    }

    public String removeCommentTagsWithoutIdAndContent(String input) {
        // Tạo regex pattern để tìm thẻ <comment> không chứa id và content
        String regex = "<comment>(.*?)</comment>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Sử dụng StringBuilder để xây dựng chuỗi mới
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        // Duyệt qua tất cả các thẻ <comment> không chứa id và content
        while (matcher.find()) {
            // Lấy vị trí bắt đầu và kết thúc của thẻ
            int start = matcher.start();
            int end = matcher.end();

            // Thêm nội dung trước thẻ vào chuỗi kết quả
            result.append(input, lastEnd, start);

            // Thêm nội dung bên trong thẻ vào chuỗi kết quả
            result.append(matcher.group(1));

            // Cập nhật vị trí lastEnd
            lastEnd = end;
        }

        // Thêm nội dung sau cùng vào chuỗi kết quả
        result.append(input, lastEnd, input.length());

        return result.toString();
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
            throw new BadRequestException("Failed to send email.");
        }
    }

}
