package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ExpertDto;
import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.ReviewRatingViewDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.ReviewRating;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ExpertService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReviewRatingRepository reviewRatingRepository;

    @Override
    public ExpertViewDto getProfileExpert(Integer expertId) {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        ExpertViewDto expertViewDto = new ExpertViewDto();
        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            expertViewDto.setId(expert.getId());
            expertViewDto.setName(expert.getName());
            expertViewDto.setAvatar(expert.getAvatar());
            expertViewDto.setPhone(expert.getPhone());
            expertViewDto.setPermissionWebsite(expert.getPersonalWebsite());
            expertViewDto.setEmail(expert.getEmail());
            expertViewDto.setLinkin(expert.getEmail());

            ExpertDto expertDto = new ExpertDto();
            expertDto.setTitle(expert.getTitle());
            expertDto.setDescription(expert.getDescription());
            expertDto.setPrice(expert.getPrice());
            List<ReviewRating> reviewRatings = reviewRatingRepository.findByReviewResponse_ReviewRequest_ExpertIdAndStatus(expert.getId(), BasicStatus.ACTIVE);

            // Chuyển đổi từ ReviewRating sang ReviewRatingViewDto và thêm vào danh sách ratings của expertDto
            List<ReviewRatingViewDto> reviewRatingViewDtoList = new ArrayList<>();
            for (ReviewRating reviewRating : reviewRatings) {
                ReviewRatingViewDto reviewRatingViewDto = new ReviewRatingViewDto();
                reviewRatingViewDto.setId(reviewRating.getId());
                reviewRatingViewDto.setScore(reviewRating.getScore());
                reviewRatingViewDto.setDateComment(reviewRating.getDateComment());
                reviewRatingViewDto.setComment(reviewRating.getComment());
                reviewRatingViewDto.setUser(modelMapper.map(reviewRating.getUser(), UsersDto.class));
                reviewRatingViewDtoList.add(reviewRatingViewDto);
            }

            expertDto.setRatings(reviewRatingViewDtoList);
            expertViewDto.setExpert(expertDto);
        } else {
            throw new RuntimeException("Expert ID not found.");
        }
        return expertViewDto;
    }

    @Override
    public boolean updateExpert(Integer expertId, ExpertUpdateDto dto) {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        if (expertOptional.isPresent()) {
            Expert expert = expertOptional.get();
            if (dto != null) {
                if (dto.getTitle() != null && !dto.getTitle().equals(expert.getTitle())) {
                    expert.setTitle(dto.getTitle());
                }
                if (dto.getDescription() != null && !dto.getDescription().equals(expert.getDescription())) {
                    expert.setDescription(dto.getDescription());
                }
                if (dto.getPrice() != expert.getPrice()) {
                    expert.setPrice(dto.getPrice());
                }
                expertRepository.save(expert);
            }

            return true;
        }
        return false;
    }

    @Override
    public List<ExpertViewDto> getExpertList() {
        List<Expert> experts = expertRepository.findByRole_RoleName(RoleType.EXPERT);
        List<ExpertViewDto> expertDTOList = new ArrayList<>();

        for (Expert expert : experts) {
            ExpertViewDto expertViewDto = new ExpertViewDto();
            expertViewDto.setId(expert.getId());
            expertViewDto.setName(expert.getName());
            expertViewDto.setAvatar(expert.getAvatar());
            expertViewDto.setPhone(expert.getPhone());
            expertViewDto.setPermissionWebsite(expert.getPersonalWebsite());
            expertViewDto.setEmail(expert.getEmail());
            expertViewDto.setLinkin(expert.getLinkin());

            ExpertDto expertDTO = new ExpertDto();
            expertDTO.setTitle(expert.getTitle());
            expertDTO.setDescription(expert.getDescription());
            expertDTO.setPrice(expert.getPrice());

            List<ReviewRating> reviewRatings = reviewRatingRepository.findByReviewResponse_ReviewRequest_ExpertIdAndStatus(expert.getId(),BasicStatus.ACTIVE);
            List<ReviewRatingViewDto> reviewRatingViewDtoList = new ArrayList<>();

            for (ReviewRating reviewRating : reviewRatings) {
                ReviewRatingViewDto reviewRatingViewDto = new ReviewRatingViewDto();
                reviewRatingViewDto.setId(reviewRating.getId());
                reviewRatingViewDto.setScore(reviewRating.getScore());
                reviewRatingViewDto.setDateComment(reviewRating.getDateComment());
                reviewRatingViewDto.setComment(reviewRating.getComment());
                reviewRatingViewDto.setUser(modelMapper.map(reviewRating.getUser(), UsersDto.class));
                reviewRatingViewDtoList.add(reviewRatingViewDto);
            }

            expertDTO.setRatings(reviewRatingViewDtoList);
            expertViewDto.setExpert(expertDTO);
            expertDTOList.add(expertViewDto);
        }

        return expertDTOList;
    }

}
