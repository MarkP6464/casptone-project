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
            priceOption.setDay(dto.getDay());
            priceOption.setPrice(dto.getPrice());
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
    public void editPriceOption(Integer expertId, List<PriceOptionDto> dto) {
        Optional<Users> expertOptional = usersRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);

        if (expertOptional.isPresent()) {
            Users expert = expertOptional.get();

            List<PriceOption> priceOptions1 = priceOptionRepository.findAllByExpertId(expert.getId());
            if(!priceOptions1.isEmpty()){
                // Xóa tất cả PriceOption của Expert
                Integer test  = expert.getId();
                priceOptionRepository.deleteByExpert(expert.getId());
            }

            // Tạo danh sách mới từ danh sách DTO
            List<PriceOption> priceOptions = dto.stream()
                    .map(priceOptionDto -> {
                        PriceOption priceOption = new PriceOption();
                        priceOption.setDay(priceOptionDto.getDay());
                        priceOption.setPrice(priceOptionDto.getPrice());
                        priceOption.setExpert((Expert) expert);
                        return priceOption;
                    })
                    .collect(Collectors.toList());

            // Lưu danh sách mới vào cơ sở dữ liệu
            priceOptionRepository.saveAll(priceOptions);

        } else {
            throw new BadRequestException("Expert ID not found.");
        }
    }


}
