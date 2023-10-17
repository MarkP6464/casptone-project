package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.mapper.ReviewResponseMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.ReviewRequestRepository;
import com.example.capstoneproject.repository.ReviewResponseRepository;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
            cvBodyReviewDto.setPermissionWebsite(cv.getUser().getPermissionWebsite());
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

            // Lưu lại ReviewResponse sau khi đã thiết lập feedbackDetail
            reviewResponseRepository.save(reviewResponse);

            // Trả về ReviewResponseDto nếu cần
        }
    }

//    @Override
//    public boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException {
//        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByReviewRequest_ExpertIdAndIdAndStatus(expertId, responseId, ReviewStatus.DRAFT);
//        if(reviewResponseOptional.isPresent()){
//            ReviewResponse reviewResponse = reviewResponseOptional.get();
//            CvBodyReviewDto cvBodyReviewDto = reviewResponse.deserialize();
//            cvBodyReviewDto.getExperiences().forEach(x -> {
//                String description = x.getDescription();
//                if (isSubstringInString(description, dto.getText())) {
//                    // Tìm vị trí bắt đầu của text trong description
//                    int startIndex = description.indexOf(dto.getText());
//                    if (startIndex >= 0) {
//                        // Tạo chuỗi mới với đoạn text đã được thay thế
//                        String newDescription = description.substring(0, startIndex) +
//                                "<comment id='" + UUID.randomUUID() + "' content='" + dto.getComment() + "'>" + dto.getText() + "</comment>" +
//                                description.substring(startIndex + dto.getText().length());
//
//                        // Thay thế tất cả dấu ngoặc kép bằng dấu nháy đơn
//                        newDescription = newDescription.replace("\"", "'");
//
//                        // Cập nhật trường description với chuỗi mới
//                        x.setDescription(newDescription);
//
//                    }
//                }
//            });
//            reviewResponse.toCvBodyReview(cvBodyReviewDto);
//            reviewResponseRepository.save(reviewResponse);
//            return true;
//
//        }else{
//            throw new RuntimeException("ReviewResponse not found or not in DRAFT status.");
//        }
//    }

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



}
