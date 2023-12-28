package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ExpertService;
import com.example.capstoneproject.service.HistoryService;
import com.example.capstoneproject.service.PriceOptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.capstoneproject.enums.RoleType.EXPERT;

@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    HistoryService historyService;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ReviewRequestRepository reviewRequestRepository;

    @Autowired
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    PriceOptionRepository priceOptionRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PriceOptionService priceOptionService;


    @Override
    public boolean updateExpert(Integer expertId, ExpertUpdateDto dto) {
        Users expertOptional = expertRepository.findExpertByIdAndRole_RoleName(expertId, EXPERT);
        if (Objects.nonNull(expertOptional)) {
            if (expertOptional instanceof Expert) {
                Expert expert = (Expert) expertOptional;

            if (dto != null) {
                if (dto.getAvatar() != null && !dto.getAvatar().equals(expert.getAvatar())) {
                    expert.setAvatar(dto.getAvatar());
                }
                if (dto.getName() != null && !dto.getName().equals(expert.getName())) {
                    expert.setName(dto.getName());
                }
                if (dto.getJobTitle() != null && !dto.getJobTitle().equals(expert.getJobTitle())) {
                    expert.setJobTitle(dto.getJobTitle());
                }
                if (dto.getCompany() != null && !dto.getCompany().equals(expert.getCompany())) {
                    expert.setCompany(dto.getCompany());
                }
                if (dto.getAbout() != null && !dto.getAbout().equals(expert.getAbout())) {
                    expert.setAbout(dto.getAbout());
                }
                if (dto.getExperiences() != null && !dto.getExperiences().equals(expert.getExperience())) {
                    expert.setExperience(dto.getExperiences());
                }
                if (dto.getBankAccountName() != null && !dto.getBankAccountName().equals(expert.getBankAccountName())) {
                    expert.setBankAccountName(dto.getBankAccountName());
                }

                if (dto.getBankAccountNumber() != null && !dto.getBankAccountNumber().equals(expert.getBankAccountNumber())) {
                    expert.setBankAccountNumber(dto.getBankAccountNumber());
                }

                if (dto.getBankName() != null && !dto.getBankName().equals(expert.getBankName())) {
                    expert.setBankName(dto.getBankName());
                }
                if (dto.getPrice() != null) {
                    priceOptionService.editPriceOption(expert.getId(),dto.getPrice());
                }
                if(dto.getCvId()!=null){
                    Optional<Cv> cvOptional = cvRepository.findById(dto.getCvId());
                    if(cvOptional.isPresent()){
                        Cv cv = cvOptional.get();
                        expert.setCvId(cv.getId());
                    }else{
                        throw new BadRequestException("Cv ID not found.");
                    }
                }
            }

            // Lưu lại cả Users và Expert
            expertRepository.save(expert);
            return true;
        }
        }
        return false;
    }

    @Override
    public ExpertConfigViewDto getExpertConfig(Integer expertId) {
        Users expertOptional = expertRepository.findExpertByIdAndRole_RoleName(expertId, EXPERT);
        if (Objects.nonNull(expertOptional)){
            if (expertOptional instanceof Expert){
                Expert expert = (Expert) expertOptional;
                ExpertConfigViewDto expertConfigViewDto = new ExpertConfigViewDto();
                expertConfigViewDto.setAvatar(expert.getAvatar());
                expertConfigViewDto.setName(expert.getName());
                expertConfigViewDto.setJobTitle(expert.getJobTitle());
                expertConfigViewDto.setCompany(expert.getCompany());
                expertConfigViewDto.setAbout(expert.getAbout());
                expertConfigViewDto.setExperiences(expert.getExperience());
                expertConfigViewDto.setBankAccountName(expert.getBankAccountName());
                expertConfigViewDto.setBankName(expert.getBankName());
                expertConfigViewDto.setBankAccountNumber(expert.getBankAccountNumber());
                List<PriceOption> priceOptions = priceOptionRepository.findAllByExpertId(expert.getId());
                if(!priceOptions.isEmpty()){
                    List<PriceOptionDto> priceOptionDtos = new ArrayList<>();
                    for(PriceOption priceOption: priceOptions){
                        PriceOptionDto priceOptionDto = new PriceOptionDto();
                        priceOptionDto.setDay(priceOption.getDay());
                        priceOptionDto.setPrice(priceOption.getPrice());
                        priceOptionDtos.add(priceOptionDto);
                    }
                    expertConfigViewDto.setPrice(priceOptionDtos);
                }
                if(expert.getCvId()!=null){
                    Optional<Cv> cvOptional = cvRepository.findById(expert.getCvId());
                    if(cvOptional.isPresent()){
                        Cv cv = cvOptional.get();
                        expertConfigViewDto.setCvId(cv.getId());
                        expertConfigViewDto.setCv(cv.getResumeName());
                    }
                }
                return expertConfigViewDto;

            }else{
                throw new BadRequestException("Cast from User to Expert fail.");
            }
        }else {
            throw new BadRequestException("Expert Id not found.");
        }
    }

    @Override
    public List<ExpertViewChooseDto> getExpertList(String search) {
        List<Expert> experts;
        if (search == null) {
            experts = expertRepository.findAllByRole_RoleNameAndPunishFalse(EXPERT);
        } else {
            experts = expertRepository.findAllByRole_RoleNameAndPunishFalse(EXPERT)
                    .stream()
                    .filter(expert -> isMatched(expert, search))
                    .collect(Collectors.toList());
        }
        List<ExpertViewChooseDto> result = experts.stream()
                .map(this::convertToExpertViewChooseDto)
                .filter(Objects::nonNull) // Filter out null values
                .filter(dto -> dto.getPrice() != null) // Filter out DTOs with null price
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public ExpertReviewViewDto getDetailExpert(Integer expertId) {
        Optional<Expert> expertOptional = expertRepository.findById(expertId);
        ExpertReviewViewDto expertReviewViewDto = new ExpertReviewViewDto();
        if(expertOptional.isPresent()){
            Expert expert = expertOptional.get();
            expertReviewViewDto.setId(expert.getId());
            expertReviewViewDto.setName(expert.getName());
            expertReviewViewDto.setAvatar(expert.getAvatar());
            expertReviewViewDto.setTitle(expert.getJobTitle());
            expertReviewViewDto.setStar(calculatorStar(expert.getId()));
            expertReviewViewDto.setDescription(expert.getAbout());
            expertReviewViewDto.setCompany(expert.getCompany());
            expertReviewViewDto.setCvId(expert.getCvId());
            List<PriceOption> priceOptions = priceOptionRepository.findAllByExpertId(expert.getId());
            // Collect prices excluding null values
            List<Long> validPrices = priceOptions.stream()
                    .map(PriceOption::getPrice)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!validPrices.isEmpty()) {
                long minPrice = Collections.min(validPrices);
                long maxPrice = Collections.max(validPrices);

                if (minPrice != maxPrice) {
                    expertReviewViewDto.setPriceMinMax(formatPrice(minPrice) + "-" + formatPrice(maxPrice));
                } else {
                    expertReviewViewDto.setPriceMinMax(formatPrice(minPrice));
                }
            }
            if(!priceOptions.isEmpty()){
                List<PriceOptionViewDto> priceOpionDtos = new ArrayList<>();
                for(PriceOption priceOption: priceOptions){
                    PriceOptionViewDto priceOptionViewDto = new PriceOptionViewDto();
                    priceOptionViewDto.setId(priceOption.getId());
                    priceOptionViewDto.setDay(priceOption.getDay());
                    priceOptionViewDto.setPrice(formatPrice(priceOption.getPrice()));
                    priceOpionDtos.add(priceOptionViewDto);
                }
                expertReviewViewDto.setPrice(priceOpionDtos);
            }
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
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, EXPERT);
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

    @Override
    public String update(HRBankRequest dto){
        Users users = usersRepository.findUsersById(dto.getId()).get();
        if (Objects.nonNull(users)){
            if (users instanceof Expert){
                Expert expert = (Expert) users;
                modelMapper.map(dto, expert);
                expertRepository.save(expert);
                return "Update succesfully";
            } else return "Update fail";
        }
        throw new BadRequestException("user not found");
    }

    @Override
    public Expert create(Expert dto) {
        Expert expert = new Expert();
        expert.setName(dto.getName());
        expert.setEmail(dto.getEmail());
        expert.setPrice(0.0);
        expert.setAvatar(dto.getAvatar());
        expert.setRole(dto.getRole());
        expert.setCreateDate(dto.getCreateDate());
        return expertRepository.save(expert);

    }

    private boolean isMatched(Expert expert, String search) {
        return (expert.getName().toLowerCase().contains(search.toLowerCase()) ||
                        expert.getCompany().toLowerCase().contains(search.toLowerCase()));
    }

    private ExpertViewChooseDto convertToExpertViewChooseDto(Expert expert) {
        List<PriceOption> priceOptions = priceOptionRepository.findAllByExpertId(expert.getId());

        if (priceOptions != null && !priceOptions.isEmpty()) {
            ExpertViewChooseDto viewChooseDto = new ExpertViewChooseDto();
            viewChooseDto.setId(expert.getId());
            viewChooseDto.setName(expert.getName());
            viewChooseDto.setJobTitle(expert.getJobTitle());
            viewChooseDto.setCompany(expert.getCompany());
            viewChooseDto.setStar(calculatorStar(expert.getId()));
            viewChooseDto.setAvatar(expert.getAvatar());
            viewChooseDto.setCompany(expert.getCompany());

            // Collect prices excluding null values
            List<Long> validPrices = priceOptions.stream()
                    .map(PriceOption::getPrice)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!validPrices.isEmpty()) {
                long minPrice = Collections.min(validPrices);
                long maxPrice = Collections.max(validPrices);

                if (minPrice != maxPrice) {
                    viewChooseDto.setPrice(formatPrice(minPrice) + "-" + formatPrice(maxPrice));
                } else {
                    viewChooseDto.setPrice(formatPrice(minPrice));
                }

                viewChooseDto.setExperience(expert.getExperience());
                viewChooseDto.setNumberReview(calculatorReview(expert.getId()));
                return viewChooseDto;
            }
        }

        return null;
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
