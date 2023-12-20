package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.PublicDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.mapper.HRMapper;
import com.example.capstoneproject.repository.HRRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HRServiceImpl implements HRService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    HRRepository hrRepository;

    @Autowired
    HRMapper hrMapper;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AdminConfigurationService adminConfigurationService;

    @Override
    public Boolean checkVip(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (Objects.nonNull(user)){
            if (user instanceof HR){
                HR hr = (HR) user;
                if (Objects.nonNull(hr.getVip())){
                    if (hr.getVip() == Boolean.TRUE){
                        List<PublicDto> publicDtos = new ArrayList<>();
                        String publish = hr.getUnJob(); // Chuỗi "1, 3, 4"
                        String[] idsArray = publish.split(", ");

                        for (String id1 : idsArray) {
                            PublicDto publicDto = new PublicDto();
                            publicDto.setId(Integer.parseInt(id1.trim()));
                            publicDtos.add(publicDto);
                        }
                        for(PublicDto public1: publicDtos){
                            Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(public1.getId(), BasicStatus.ACTIVE, StatusReview.Unpublish);
                            if(jobPostingOptional.isPresent()){
                                JobPosting jobPosting = jobPostingOptional.get();
                                jobPosting.setShare(StatusReview.Published);
                                jobPostingRepository.save(jobPosting);
                            }
                        }
                        hr.setUnJob(null);
                        hrRepository.save(hr);
                        return true;
                    }else{
                        List<JobPosting> jobPostings = jobPostingRepository.findAllByUser_IdAndStatusAndShare(hr.getId(),BasicStatus.ACTIVE, StatusReview.Published);
                        if(jobPostings!=null){
                            for(JobPosting jobPosting: jobPostings){
                                jobPosting.setShare(StatusReview.Unpublish);
                                jobPostingRepository.save(jobPosting);
                            }
                            String concatenatedIds = jobPostings.stream()
                                    .map(jobPosting -> String.valueOf(jobPosting.getId()))
                                    .collect(Collectors.joining(", "));
                            hr.setUnJob(concatenatedIds);
                        }
                        hrRepository.save(hr);
                        throw new BadRequestException("Please buy subscription to buy this feature!");
                    }
                } else throw new InternalServerException("Vip is null");
            }
        }
        throw new BadRequestException("Not found HR by id");
    }

    @Override
    public HRDto get(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (Objects.nonNull(user)){
            if (user instanceof HR){
                HR hr = (HR) user;
                if (Objects.nonNull(hr)){
                    return hrMapper.toDto(hr);
                }else {
                    throw new BadRequestException("HR Not found");
                }
            }
        }
        throw new BadRequestException("Not found HR by id");
    }

    @Override
    public HRDto update(HRResponse dto){
        Users users = usersRepository.findUsersById(dto.getId()).get();
        if (Objects.nonNull(users)){
            if (users instanceof HR){
                HR hr = (HR) users;
                hrMapper.toEntity(dto, hr);
                return hrMapper.toDto(hrRepository.save(hr));
            }
        }
        throw new BadRequestException("user not found");
    }

    @Override
    public HR create(HR dto) {
        HR hr = new HR();
        hr.setName(dto.getName());
        hr.setEmail(dto.getEmail());
        hr.setAvatar(dto.getAvatar());
        hr.setRole(dto.getRole());
        hr.setCreateDate(dto.getCreateDate());
        hr.setSubscription(false);
        hr.setCompanyName("ABC Company");
        hr.setCompanyLocation("ABC Location");
        hr.setCompanyDescription("ABC Description");
        hr.setCompanyLogo(dto.getCompanyLogo());
        hr.setVip(false);
        return hrRepository.save(hr);
    }

    @Override
    public String register(TransactionResponse dto) throws Exception {
        Users users = usersRepository.findUsersById(dto.getUserId()).get();
        Double expenditure = dto.getExpenditure();
        if (Objects.nonNull(users)) {
            if (users instanceof HR) {
                HR hr = (HR) users;
                TransactionDto transactionDto = new TransactionDto();
                AdminConfigurationResponse adminConfigurationDto = adminConfigurationService.getByAdminId(1);
                if (adminConfigurationDto.getVipMonthRatio().equals(expenditure) || adminConfigurationDto.getVipYearRatio().equals(expenditure)) {
                    if (adminConfigurationDto.getVipMonthRatio().equals(expenditure)) {
                            transactionDto.setResponseMessage("Extend 1 month subscription");
                            transactionDto.setExpenditure(expenditure);
                            transactionDto.setTransactionType(TransactionType.ADD);
                            transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }else if (adminConfigurationDto.getVipYearRatio().equals(expenditure)) {
                            transactionDto.setResponseMessage("Extend 1 year subscription");
                            transactionDto.setExpenditure(expenditure);
                            transactionDto.setTransactionType(TransactionType.ADD);
                            transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }
                    transactionDto.setUserId(dto.getUserId());
                    String s = transactionService.create(transactionDto);
                    return s;
                } else {
                    throw new BadRequestException("Not a valid register subscription plan");
                }
            } else {
                throw new ForbiddenException("You are not HR!");
            }
        } else throw new BadRequestException("Not found user id");
    }
    @Scheduled(cron = "0 0 0 * * * ")
    public void checkSubscription(){
        LocalDate currentDate = LocalDate.now();
        List<HR> hrList = hrRepository.findAllByStatusAndVipTrue(BasicStatus.ACTIVE);
        for (HR hr : hrList) {
            if (currentDate.compareTo(hr.getExpiredDay()) >= 7L){
                ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription is going to expired!"
                        , "Dear user " + hr.getName() + ", your subscription is going to expired on " + hr.getExpiredDay()
                                + "Please extend your subscription. CvBuilder reminder mail!");
            } else if (currentDate.compareTo(hr.getExpiredDay()) == 0L){
                hr.setVip(Boolean.FALSE);
                hrRepository.save(hr);
                ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription expired!"
                        , "Dear user " + hr.getName() + ", your subscription is expired! Thanks for your using.");
            }
        }
    }
}
