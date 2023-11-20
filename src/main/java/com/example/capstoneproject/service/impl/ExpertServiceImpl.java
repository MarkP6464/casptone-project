package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ExpertReviewRatingViewDto;
import com.example.capstoneproject.Dto.responses.ExpertReviewViewDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.ReviewResponse;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ExpertService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public boolean updateExpert(Integer expertId, ExpertUpdateDto dto) {
        Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(expertId, RoleType.EXPERT);
        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            if (dto != null) {
                if (dto.getTitle() != null && !dto.getTitle().equals(expert.getTitle())) {
                    expert.setTitle(dto.getTitle());
                }
                if (dto.getDescription() != null && !dto.getDescription().equals(expert.getDescription())) {
                    expert.setDescription(dto.getDescription());
                }
                if (dto.getPrice() != null && !dto.getPrice().equals(expert.getPrice())) {
                    expert.setPrice(dto.getPrice());
                }
                if (dto.getCompany() != null && !dto.getCompany().equals(expert.getCompany())) {
                    expert.setCompany(dto.getCompany());
                }
                expertRepository.save(expert);
            }

            return true;
        }
        return false;
    }

    @Override
    public List<ExpertViewChooseDto> getExpertList(String search) {
        List<Expert> experts = expertRepository.findAllByUsers_Role_RoleName(RoleType.EXPERT);

        List<ExpertViewChooseDto> result = experts.stream()
                .filter(expert -> isMatched(expert, search))
                .map(this::convertToExpertViewChooseDto)
                .collect(Collectors.toList());

        return result.isEmpty()
                ? throwBadRequestException()
                : result;
    }

    @Override
    public ExpertReviewViewDto getDetailExpert(Integer expertId) {
        Optional<Expert> expertOptional = expertRepository.findById(expertId);
        ExpertReviewViewDto expertReviewViewDto = new ExpertReviewViewDto();
        if(expertOptional.isPresent()){
            Expert expert = expertOptional.get();
            expertReviewViewDto.setId(expert.getId());
            expertReviewViewDto.setName(expert.getUsers().getName());
            expertReviewViewDto.setAvatar(expert.getUsers().getAvatar());
            expertReviewViewDto.setTitle(expert.getTitle());
            expertReviewViewDto.setStar(calculatorStar(expert.getId()));
            expertReviewViewDto.setDescription(expert.getDescription());
            expertReviewViewDto.setCompany(expert.getCompany());
            expertReviewViewDto.setPrice(expert.getPrice());
            expertReviewViewDto.setExperience(expert.getExperience());
            expertReviewViewDto.setNumberReview(calculatorReview(expert.getId()));

            List<ReviewResponse> reviewResponses = reviewResponseRepository.findAllByReviewRequest_ExpertId(expertId);
            if(reviewResponses==null){
                throw new BadRequestException("The system currently cannot find any reviews. Please come back later.");
            }else{
                List<ExpertReviewRatingViewDto> comments = new ArrayList<>();

                for (ReviewResponse reviewResponse : reviewResponses){
                    if (reviewResponse.getComment() != null){
                        ExpertReviewRatingViewDto commentDto = new ExpertReviewRatingViewDto();
                        commentDto.setId(reviewResponse.getReviewRequest().getCv().getUser().getId());
                        commentDto.setName(reviewResponse.getReviewRequest().getCv().getUser().getName());
                        commentDto.setAvatar(reviewResponse.getReviewRequest().getCv().getUser().getAvatar());
                        commentDto.setComment(reviewResponse.getComment());
                        commentDto.setScore(reviewResponse.getScore());
                        commentDto.setDateComment(reviewResponse.getDateComment());
                        comments.add(commentDto);
                    }
                }
                expertReviewViewDto.setComments(comments);
            }
            return expertReviewViewDto;
        }else {
            throw new BadRequestException("Expert ID not found");
        }
    }

    @Override
    public void punishExpert(Integer expertId) {
        Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(expertId, RoleType.EXPERT);
        if(expertOptional.isPresent()){
            LocalDate current = LocalDate.now();
            Expert expert = expertOptional.get();
            expert.setPunish(true);
            expert.setPunishDate(current);
            expertRepository.save(expert);
        }else{
            throw new BadRequestException("Expert ID not found");
        }
    }

    @Override
    public void unPunishExpert() {
        List<Expert> punishedExperts = expertRepository.findByPunishIsTrue();
        LocalDate current = LocalDate.now();

        for (Expert expert : punishedExperts) {
            LocalDate newPunishDate = expert.getPunishDate().plusWeeks(2);

            if (newPunishDate.isAfter(current) || newPunishDate.isEqual(current)) {
                expert.setPunish(false);
                expert.setPunishDate(null);
                expertRepository.save(expert);
            }
        }

        //        Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(expertId, RoleType.EXPERT);
//        if(expertOptional.isPresent()){
//            LocalDate current = LocalDate.now();
//            Expert expert = expertOptional.get();
//            LocalDate newPunishDate = expert.getPunishDate().plusWeeks(2);
//            if (newPunishDate.isAfter(current) || newPunishDate.isEqual(current)) {
//                expert.setPunish(false);
//                expert.setPunishDate(newPunishDate);
//                expertRepository.save(expert);
//                return true;
//            } else {
//                return false;
//            }
//        }else{
//            throw new BadRequestException("Expert ID not found");
//        }
    }

    private boolean isMatched(Expert expert, String search) {
        return (search == null || search.isEmpty()) &&
                !expert.isPunish() &&
                (expert.getTitle().contains(search) ||
                        expert.getUsers().getName().contains(search) ||
                        expert.getCompany().contains(search));
    }

    private ExpertViewChooseDto convertToExpertViewChooseDto(Expert expert) {
        ExpertViewChooseDto viewChooseDto = new ExpertViewChooseDto();
        viewChooseDto.setId(expert.getId());
        viewChooseDto.setTitle(expert.getTitle());
        viewChooseDto.setName(expert.getUsers().getName());
        viewChooseDto.setStar(calculatorStar(expert.getId()));
        viewChooseDto.setAvatar(expert.getUsers().getAvatar());
        viewChooseDto.setCompany(expert.getCompany());
        viewChooseDto.setPrice(expert.getPrice());
        viewChooseDto.setExperience(expert.getExperience());
        viewChooseDto.setNumberReview(calculatorReview(expert.getId()));
        return viewChooseDto;
    }

    private Double calculatorStar(Integer expertId) {
        List<ReviewResponse> reviewResponses = reviewResponseRepository.findAllByReviewRequest_ExpertId(expertId);

        if (reviewResponses == null || reviewResponses.isEmpty()) {
            return 0.0;
        } else {
            double totalScore = 0.0;
            int validScoreCount = 0;

            for (ReviewResponse response : reviewResponses) {
                Double score = response.getScore();
                if (score != null) {
                    totalScore += score;
                    validScoreCount++;
                }
            }

            if (validScoreCount == 0) {
                return 0.0;
            } else {
                double averageScore = totalScore / validScoreCount;
                return Math.round(averageScore * 100.0) / 100.0;
            }
        }
    }

    private Integer calculatorReview(Integer expertId){
        List<ReviewResponse> reviewResponses = reviewResponseRepository.findAllByReviewRequest_ExpertId(expertId);

        if (reviewResponses == null || reviewResponses.isEmpty()) {
            return 0;
        } else{
            int doneReviewCount = 0;

            for (ReviewResponse response : reviewResponses) {
                StatusReview status = response.getStatus();
                if (status != null && status == StatusReview.Done) {
                    doneReviewCount++;
                }
            }

            return doneReviewCount;
        }
    }

    private List<ExpertViewChooseDto> throwBadRequestException() {
        throw new BadRequestException("Please come back later. Currently, the system cannot find any experts.");
    }

}
