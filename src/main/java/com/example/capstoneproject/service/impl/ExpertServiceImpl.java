package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ExpertDto;
import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.ReviewRatingViewDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ExpertService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public ExpertViewDto getProfileExpert(Integer expertId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        ExpertViewDto expertViewDto = new ExpertViewDto();
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(users.getId() ,RoleType.EXPERT);
            if(!expertOptional.isPresent()){
                Expert expertSave = new Expert();
                expertSave.setId(users.getId());
                expertSave.setUsers(users);
                expertRepository.save(expertSave);
            }
            Expert expert = expertOptional.get();
            expertViewDto.setId(users.getId());
            expertViewDto.setName(users.getName());
            expertViewDto.setAvatar(users.getAvatar());
            expertViewDto.setPhone(users.getPhone());
            expertViewDto.setPermissionWebsite(users.getPersonalWebsite());
            expertViewDto.setEmail(users.getEmail());
            expertViewDto.setLinkin(users.getEmail());
            ExpertDto expertDto = new ExpertDto();
            expertDto.setId(expert.getId());
            expertDto.setTitle(expert.getTitle());
            expertDto.setDescription(expert.getDescription());
            if (expert.getPrice() != null) {
                expertDto.setPrice(expert.getPrice());
            }
//            List<ReviewRating> reviewRatings = reviewRatingRepository.findByReviewResponse_ReviewRequest_ExpertIdAndStatus(expert.getId(), BasicStatus.ACTIVE);

            // Chuyển đổi từ ReviewRating sang ReviewRatingViewDto và thêm vào danh sách ratings của expertDto
            List<ReviewRatingViewDto> reviewRatingViewDtoList = new ArrayList<>();
//            for (ReviewRating reviewRating : reviewRatings) {
//                ReviewRatingViewDto reviewRatingViewDto = new ReviewRatingViewDto();
//                reviewRatingViewDto.setId(reviewRating.getId());
//                reviewRatingViewDto.setScore(reviewRating.getScore());
//                reviewRatingViewDto.setDateComment(reviewRating.getDateComment());
//                reviewRatingViewDto.setComment(reviewRating.getComment());
//                reviewRatingViewDto.setUser(modelMapper.map(reviewRating.getUser(), UsersDto.class));
//                reviewRatingViewDtoList.add(reviewRatingViewDto);
//            }

            expertDto.setRatings(reviewRatingViewDtoList);
            expertViewDto.setExpert(expertDto);

        }else {
            throw new BadRequestException("Expert ID not found");
        }
        return expertViewDto;
    }

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

                expertRepository.save(expert);
            }

            return true;
        }
        return false;
    }

    @Override
    public List<ExpertViewChooseDto> getExpertList(String search) {
//        List<Expert> experts = expertRepository.findAllByUsers_Role_RoleName(RoleType.EXPERT);
//        List<ExpertViewDto> expertDTOList = new ArrayList<>();
//        for (Expert expert : experts) {
//            Optional<Expert> expertOptional = expertRepository.findByIdAndUsers_Role_RoleName(expert.getId() ,RoleType.EXPERT);
//            if(!expertOptional.isPresent()){
//                Expert expertSave = new Expert();
//                expertSave.setId(expert.getId());
//                expertSave.setUsers(expert);
//                expertRepository.save(expertSave);
//            }
//            Expert expert = expertOptional.get();
//            ExpertViewDto expertViewDto = new ExpertViewDto();
//            expertViewDto.setId(users1.getId());
//            expertViewDto.setName(users1.getName());
//            expertViewDto.setAvatar(users1.getAvatar());
//            expertViewDto.setPhone(users1.getPhone());
//            expertViewDto.setPermissionWebsite(users1.getPersonalWebsite());
//            expertViewDto.setEmail(users1.getEmail());
//            expertViewDto.setLinkin(users1.getLinkin());
//
//            ExpertDto expertDTO = new ExpertDto();
//            expertDTO.setId(expert.getId());
//            expertDTO.setTitle(expert.getTitle());
//            expertDTO.setDescription(expert.getDescription());
//            if (expert.getPrice() != null) {
//                expertDTO.setPrice(expert.getPrice());
//            }
//
////            List<ReviewRating> reviewRatings = reviewRatingRepository.findByReviewResponse_ReviewRequest_ExpertIdAndStatus(expert.getId(),BasicStatus.ACTIVE);
//            List<ReviewRatingViewDto> reviewRatingViewDtoList = new ArrayList<>();
//
////            for (ReviewRating reviewRating : reviewRatings) {
////                ReviewRatingViewDto reviewRatingViewDto = new ReviewRatingViewDto();
////                reviewRatingViewDto.setId(reviewRating.getId());
////                reviewRatingViewDto.setScore(reviewRating.getScore());
////                reviewRatingViewDto.setDateComment(reviewRating.getDateComment());
////                reviewRatingViewDto.setComment(reviewRating.getComment());
////                reviewRatingViewDto.setUser(modelMapper.map(reviewRating.getUser(), UsersDto.class));
////                reviewRatingViewDtoList.add(reviewRatingViewDto);
////            }
//
//            expertDTO.setRatings(reviewRatingViewDtoList);
//            expertViewDto.setExpert(expertDTO);
//            expertDTOList.add(expertViewDto);
//        }
//
//        return expertDTOList;
        List<Expert> experts = expertRepository.findAllByUsers_Role_RoleName(RoleType.EXPERT);

        List<ExpertViewChooseDto> result = experts.stream()
                .filter(expert -> isMatched(expert, search))
                .map(this::convertToExpertViewChooseDto)
                .collect(Collectors.toList());

        return result.isEmpty()
                ? throwBadRequestException()
                : result;
    }

    private boolean isMatched(Expert expert, String search) {
        return search == null || search.isEmpty() ||
                expert.getTitle().contains(search) ||
                expert.getUsers().getName().contains(search) ||
                expert.getCompany().contains(search);
    }

    private ExpertViewChooseDto convertToExpertViewChooseDto(Expert expert) {
        ExpertViewChooseDto viewChooseDto = new ExpertViewChooseDto();
        viewChooseDto.setId(expert.getId());
        viewChooseDto.setTitle(expert.getTitle());
        viewChooseDto.setName(expert.getUsers().getName());
        viewChooseDto.setAvatar(expert.getUsers().getAvatar());
        viewChooseDto.setCompany(expert.getCompany());
        viewChooseDto.setPrice(expert.getPrice());
        viewChooseDto.setExperience(expert.getExperience());
        viewChooseDto.setNumberReview(expert.getNumberReview());
        return viewChooseDto;
    }

    private List<ExpertViewChooseDto> throwBadRequestException() {
        throw new BadRequestException("Please come back later. Currently, the system cannot find any experts.");
    }

}
