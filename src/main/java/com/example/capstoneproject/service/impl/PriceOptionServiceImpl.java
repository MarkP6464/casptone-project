package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.PriceOptionDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.PriceOption;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.ExpertRepository;
import com.example.capstoneproject.repository.PriceOptionRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.PriceOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceOptionServiceImpl implements PriceOptionService {

    @Autowired
    PriceOptionRepository priceOptionRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    UsersRepository usersRepository;

    @Override
    public String createPriceOption(Integer expertId, PriceOptionDto dto) {
        Optional<Users> expertOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        if(expertOptional.isPresent()){
            PriceOption priceOption = new PriceOption();
            Users expert = expertOptional.get();
            Long validatedPrice = validateAndProcessPrice(dto.getPrice());
            if(dto.getDay()==null){
                throw new BadRequestException("Date cannot be empty.");
            }
            priceOption.setDay(dto.getDay());
            priceOption.setPrice(validatedPrice);
            priceOption.setExpert((Expert) expert);
            priceOptionRepository.save(priceOption);
            return "Create option successful";
        }else{
            throw new BadRequestException("Expert ID not found.");
        }
    }

    @Override
    public String updatePriceOption(Integer expertId, Integer optionId, PriceOptionDto dto) {
        Optional<PriceOption> priceOptionOptional = priceOptionRepository.findByExpertIdAndId(expertId, optionId);
        if(priceOptionOptional.isPresent()){
            PriceOption priceOption = priceOptionOptional.get();
            priceOption.setDay(dto.getDay());
            priceOption.setPrice(dto.getPrice());
            priceOptionRepository.save(priceOption);
            return "Update option successful";
        }else{
            throw new BadRequestException("Expert ID dont have option.");
        }
    }

    @Transactional
    @Override
    public void editPriceOption(Integer expertId, List<PriceOptionDto> dtoList) {
        Optional<Users> expertOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);

        if (expertOptional.isPresent()) {
            Users expert = expertOptional.get();

            List<PriceOption> newPriceOptions = new ArrayList<>();

            for (PriceOptionDto priceOptionDto : dtoList) {
                Long validatedPrice = validateAndProcessPriceUpdate(priceOptionDto.getPrice());

                // Check if both price and day are valid before creating the PriceOption entity
                if (validatedPrice != null && priceOptionDto.getDay() != null) {
                    PriceOption priceOption = new PriceOption();
                    priceOption.setDay(priceOptionDto.getDay());
                    priceOption.setPrice(validatedPrice);
                    priceOption.setExpert((Expert) expert);
                    newPriceOptions.add(priceOption);
                }
            }
            priceOptionRepository.deleteByExpert(expertId);

            // Save the new list to the database only if all prices and days are valid
            if (newPriceOptions.size() == dtoList.size()) {
                priceOptionRepository.saveAll(newPriceOptions);
            } else {
                throw new BadRequestException("Invalid price or day found. None of the PriceOptions were saved.");
            }

        } else {
            throw new BadRequestException("Expert ID not found.");
        }
    }

    private Long validateAndProcessPrice(Long price) {
        // Nếu giá trị là null
        if (price == null) {
            // Ném BadRequestException
            throw new BadRequestException("Price cannot be empty.");
        }

        // Nếu giá trị nhỏ hơn 1000, hoặc có chứa số 0 ở đầu
        if (price < 1000 || String.valueOf(price).startsWith("0")) {
            // Xử lý giá trị, có thể đặt một giá trị mặc định hoặc thông báo lỗi
            throw new BadRequestException("Invalid price. Price must be greater than or equal to 1000.");
        }

        // Trả về giá trị đã được kiểm tra và xử lý
        return price;
    }

    // Function to validate and process the price
    private Long validateAndProcessPriceUpdate(Long price) {
        if (price == null || price < 1000 || String.valueOf(price).startsWith("0")) {
            return null;  // Return null if the price is invalid
        }
        return price;
    }

}
