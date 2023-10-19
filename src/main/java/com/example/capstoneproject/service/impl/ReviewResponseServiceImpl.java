package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.mapper.ReviewResponseMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.ExpertRepository;
import com.example.capstoneproject.repository.ReviewRequestRepository;
import com.example.capstoneproject.repository.ReviewResponseRepository;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Override
    public void createReviewResponse(Integer cvId, Integer requestId) throws JsonProcessingException {
        Cv cv = cvRepository.getById(cvId);
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByIdAndStatus(requestId, ReviewStatus.ACCEPT);
        if (Objects.nonNull(cv) && reviewRequestOptional.isPresent()){
            // Lấy CvBody từ CV
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();

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
            cvBodyReviewDto.setSourceWorks(cvBodyDto.getSourceWorks());
            cvBodyReviewDto.setSummary(cv.getSummary());
            cvBodyReviewDto.setName(cv.getUser().getName());
            cvBodyReviewDto.setAddress(cv.getUser().getAddress());
            cvBodyReviewDto.setPhone(cv.getUser().getPhone());
            cvBodyReviewDto.setPermissionWebsite(cv.getUser().getPersonalWebsite());
            cvBodyReviewDto.setEmail(cv.getUser().getEmail());
            cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());

            // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);

            // Tạo một ReviewResponse mới và thiết lập các giá trị
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setStatus(ReviewStatus.DRAFT);
            reviewResponse.setFeedbackDetail(cvBodyReviewJson);
            reviewResponse.setReviewRequest(reviewRequest);

            // Lưu ReviewResponse để có ID
            reviewResponse = reviewResponseRepository.save(reviewResponse);

            // Thiết lập trường feedbackDetail của ReviewResponse
            reviewResponse.toCvBodyReview(cvBodyReviewDto);
            reviewResponseRepository.save(reviewResponse);
        }
    }

    @Override
    public boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId, responseId, ReviewStatus.DRAFT);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            CvBodyReviewDto cvBodyReviewDto = reviewResponse.deserialize();
            String sp = removeCommentTagsWithoutIdAndContent(dto.getText());
            cvBodyReviewDto.getExperiences().forEach(x -> {
                if(isSubstringInString(x.getDescription(), sp)){
                    try {
                        x.setDescription(dto.getText());
                        reviewResponse.toCvBodyReview(cvBodyReviewDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    reviewResponseRepository.save(reviewResponse);
                }
                String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                x.setDescription(description);
            });
            cvBodyReviewDto.getInvolvements().forEach(x -> {
                if(isSubstringInString(x.getDescription(), sp)){
                    try {
                        x.setDescription(dto.getText());
                        reviewResponse.toCvBodyReview(cvBodyReviewDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    reviewResponseRepository.save(reviewResponse);
                }
                String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                x.setDescription(description);
            });
            cvBodyReviewDto.getProjects().forEach(x -> {
                if(isSubstringInString(x.getDescription(), sp)){
                    try {
                        x.setDescription(dto.getText());
                        reviewResponse.toCvBodyReview(cvBodyReviewDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    reviewResponseRepository.save(reviewResponse);
                }
                String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                x.setDescription(description);
            });
            cvBodyReviewDto.getSkills().forEach(x -> {
                if(isSubstringInString(x.getDescription(), sp)){
                    try {
                        x.setDescription(dto.getText());
                        reviewResponse.toCvBodyReview(cvBodyReviewDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    reviewResponseRepository.save(reviewResponse);
                }
                String description = x.getDescription().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");

                x.setDescription(description);
            });
            if(cvBodyReviewDto.getSummary()!=null){
                if (isSubstringInString(cvBodyReviewDto.getSummary(), sp)) {
                    try {
                        cvBodyReviewDto.setSummary(dto.getText());
                        reviewResponse.toCvBodyReview(cvBodyReviewDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    reviewResponseRepository.save(reviewResponse);
                }
                String description = cvBodyReviewDto.getSummary().replace("<comment>", "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>");
                cvBodyReviewDto.setSummary(description);
            }
            reviewResponse.toCvBodyReview(cvBodyReviewDto);
            reviewResponseRepository.save(reviewResponse);
            return true;
        } else {
            throw new RuntimeException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean deleteComment(Integer expertId, Integer responseId, String commentId) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId, responseId, ReviewStatus.DRAFT);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
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
            if(cvBodyReviewDto.getSummary()!=null){
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
        } else {
            throw new RuntimeException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean updateComment(Integer expertId, Integer responseId, String commentId, String newContent) throws JsonProcessingException {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId, responseId, ReviewStatus.DRAFT);
        if (reviewResponseOptional.isPresent()) {
            ReviewResponse reviewResponse = reviewResponseOptional.get();
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
            if(cvBodyReviewDto.getSummary()!=null){
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
        } else {
            throw new RuntimeException("ReviewResponse not found or not in DRAFT status.");
        }
    }

    @Override
    public boolean updateReviewResponse(Integer expertId, Integer responseId, ReviewResponseUpdateDto dto) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId,responseId,ReviewStatus.DRAFT);
        if(reviewResponseOptional.isPresent()){
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            if(dto.getOverall()!=null && !dto.getOverall().equals(reviewResponse.getOverall())){
                reviewResponse.setOverall(dto.getOverall());
                reviewResponseRepository.save(reviewResponse);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean publicReviewResponse(Integer expertId, Integer responseId) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId,responseId,ReviewStatus.DRAFT);
        if(reviewResponseOptional.isPresent()){
            ReviewResponse reviewResponse = reviewResponseOptional.get();
            reviewResponse.setStatus(ReviewStatus.SEND);
            reviewResponseRepository.save(reviewResponse);
            sendEmail(reviewResponse.getReviewRequest().getCv().getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
            return true;
        }
        return false;
    }

    @Override
    public ReviewResponseDto receiveReviewResponse(Integer userId, Integer requestId) throws JsonProcessingException {
        Optional<ReviewRequest> reviewRequestOptional = reviewRequestRepository.findByIdAndStatus(requestId, ReviewStatus.ACCEPT);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        if(reviewRequestOptional.isPresent()){
            ReviewRequest reviewRequest = reviewRequestOptional.get();
            if(Objects.equals(userId, reviewRequest.getCv().getUser().getId())){
                Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_IdAndStatus(reviewRequest.getId(), ReviewStatus.SEND);
                if(reviewResponseOptional.isPresent()){
                    ReviewResponse reviewResponse = reviewResponseOptional.get();
                    reviewResponseDto.setId(reviewResponse.getId());
                    reviewResponseDto.setFeedbackDetail(reviewResponse.deserialize());
                    reviewResponseDto.setOverall(reviewResponse.getOverall());
                    return reviewResponseDto;
                }
            }else {
                throw new RuntimeException("UserId incorrect with requestId");
            }
        }else{
            throw new RuntimeException("RequestId incorrect");
        }
        return reviewResponseDto;
    }

    @Override
    public List<ReviewResponseDto> daftReviewResponse(Integer expertId, ReviewStatus status) throws JsonProcessingException {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);

        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            List<ReviewResponse> reviewResponses = reviewResponseRepository.findByReviewRequest_ExpertId(expert.getId());

            List<ReviewResponseDto> daftReviewResponses = new ArrayList<>();

            for (ReviewResponse response : reviewResponses) {
                if (status == null || response.getStatus() == status) {
                    ReviewResponseDto responseDto = new ReviewResponseDto();
                    responseDto.setId(response.getId());
                    responseDto.setOverall(response.getOverall());
                    responseDto.setFeedbackDetail(response.deserialize());
                    daftReviewResponses.add(responseDto);
                }
            }

            return daftReviewResponses;
        } else {
            throw new RuntimeException("Expert ID not found.");
        }
    }

    @Override
    public ReviewResponseDto getReviewResponse(Integer expertId, Integer responseId) throws JsonProcessingException {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndId(expert.getId(),responseId);
            if (reviewResponseOptional.isPresent()){
                ReviewResponse reviewResponse = reviewResponseOptional.get();
                reviewResponseDto.setId(reviewResponse.getId());
                reviewResponseDto.setOverall(reviewResponse.getOverall());
                reviewResponseDto.setFeedbackDetail(reviewResponse.deserialize());
            }
            return reviewResponseDto;
        } else {
            throw new RuntimeException("Expert ID not found.");
        }
    }


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
            throw new RuntimeException("Failed to send email.");
        }
    }

}
