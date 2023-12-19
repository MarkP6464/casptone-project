package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.Admin;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.enums.TypeChart;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.TransactionRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.capstoneproject.service.impl.ReviewRequestServiceImpl.formatPrice;

@Service
public class AdminConfigurationServiceImpl implements AdminConfigurationService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ModelMapper modelMapper;

    public Boolean isAdmin(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (user instanceof Admin){
            return true;
        }
        return false;
    }

    @Override
    public AdminConfigurationResponse getByAdminId(Integer id) throws JsonProcessingException {
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                return admin.getConfiguration();
            } else throw new BadRequestException("id not valid to token");
        }else throw new BadRequestException("Not found configuration by id");
    }

    @Override
    public AdminConfigurationRatioResponse update(AdminConfigurationRatioResponse dto) throws JsonProcessingException {
        AdminConfigurationRatioResponse adminConfigurationRatioResponse = new AdminConfigurationRatioResponse();
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
                modelMapper.map(dto, adminConfigurationResponse);
                admin.setConfiguration(adminConfigurationResponse);
                Admin admin1 = usersRepository.save(admin);
                adminConfigurationRatioResponse.setMoneyRatio(1.0);
                adminConfigurationRatioResponse.setVipYearRatio(admin1.getConfiguration().getVipYearRatio().longValue());
                adminConfigurationRatioResponse.setVipMonthRatio(admin1.getConfiguration().getVipMonthRatio().longValue());
                return  adminConfigurationRatioResponse;
            }
        }
        throw new BadRequestException("id not valid to token");
    }

    @Override
    public AdminConfigurationApiResponse updateApi(AdminConfigurationApiResponse dto) {
        AdminConfigurationApiResponse adminConfigurationApiResponse = new AdminConfigurationApiResponse();
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
                modelMapper.map(dto, adminConfigurationResponse);
                admin.setConfiguration(adminConfigurationResponse);
                usersRepository.save(admin);
                Admin admin1 = usersRepository.save(admin);
                adminConfigurationApiResponse.setApiKey(admin1.getConfiguration().getApiKey());
                return  adminConfigurationApiResponse;
            }
        }
        throw new BadRequestException("id not valid to token");
    }

    @Override
    public AdminConfigDashResponse getChart(Integer adminId, AdminDateChartResponse dto, TypeChart chart) {
        AdminConfigDashResponse adminConfigDashResponse = new AdminConfigDashResponse();
        List<Users> users = usersRepository.findAllByStatusAndIdNot(BasicStatus.ACTIVE, adminId);
        LocalDate current = LocalDate.now();
        List<Users> users1 = usersRepository.findAllByStatusAndIdNotAndLastActive(BasicStatus.ACTIVE, adminId, current);
        Double income = transactionRepository.sumExpenditureByTransactionTypeAndStatus(TransactionType.ADD, TransactionStatus.SUCCESSFUL);
        long transformIncome = income.longValue();
        adminConfigDashResponse.setTotalUser(users.size());
        adminConfigDashResponse.setUserLogin(users1.size());
        adminConfigDashResponse.setIncome(formatPrice(transformIncome));
        List<AdminChartResponse> chartResponses = new ArrayList<>();

        LocalDate startDate, endDate;

        // Kiểm tra và gán giá trị cho startDate và endDate
        if (dto.getStart() != null && dto.getEnd() != null) {
            if (dto.getStart().isAfter(dto.getEnd())) {
                throw new BadRequestException("startDate is after endDate");
            }
            startDate = dto.getStart();
            endDate = dto.getEnd();
        } else {
            // Xử lý trường hợp startDate hoặc endDate là null
            startDate = current.minusDays(7);
            endDate = current;
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        // Kiểm tra khoảng thời gian hợp lệ
        if (daysBetween < 7 || daysBetween > 30) {
            throw new BadRequestException("Range must be between 7 and 30 days");
        }

        if (chart.equals(TypeChart.Account)) {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                // Kiểm tra ngày không ở tương lai và từ năm 2023 trở đi
                if (!date.isAfter(current) && date.getYear() >= 2023) {
                    List<Users> entities = usersRepository.findAllByCreateDate(date);
                    AdminChartResponse adminChartResponse = new AdminChartResponse();
                    adminChartResponse.setDate(date);
                    int value = entities.size();
                    Long value1 = (long) value;
                    adminChartResponse.setAmount(value1);
                    chartResponses.add(adminChartResponse);
                }
            }
        } else {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                // Kiểm tra ngày không ở tương lai và từ năm 2023 trở đi
                if (!date.isAfter(current) && date.getYear() >= 2023) {
                    Double entities = transactionRepository.sumExpenditureByTransactionTypeAndStatusAndDate(
                            TransactionType.ADD,
                            TransactionStatus.SUCCESSFUL,
                            date.toString()
                    );
                    AdminChartResponse adminChartResponse = new AdminChartResponse();
                    adminChartResponse.setDate(date);
                    Long value = entities.longValue();
                    adminChartResponse.setAmount(value);
                    chartResponses.add(adminChartResponse);
                }
            }
        }

        adminConfigDashResponse.setChart(chartResponses);

        return adminConfigDashResponse;
    }




//    @Override
//    public AdminConfigurationRatioResponse update(AdminConfigurationRatioResponse dto) throws JsonProcessingException {
//        Users user = usersRepository.findUsersById(1).get();
//        if (Objects.nonNull(user)){
//            if (user instanceof Admin) {
//                Admin admin = (Admin) user;
//                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
//                modelMapper.map(dto, adminConfigurationResponse);
//                admin.setConfiguration(adminConfigurationResponse);
//                usersRepository.save(admin);
////                return  adminConfigurationResponse;
//            }
//        }
//        throw new BadRequestException("id not valid to token");
//    }
}
