package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.constant.PaymentConstant;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.mapper.TransactionMapper;
import com.example.capstoneproject.repository.TransactionRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.TransactionService;
import com.example.capstoneproject.service.UsersService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.allinone.models.QueryStatusTransactionResponse;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.allinone.processor.allinone.QueryStatusTransaction;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;
    @Value("${quota.ratio}")
    private Long ratio;

    @Autowired
    UsersService usersService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    UsersRepository usersRepository;

    @Override
    public List<TransactionDto> getAll(String id){
        List<Transaction> list = transactionRepository.findBySentId(id);
        List<TransactionDto> dtos = list.stream().map(x -> transactionMapper.toDto(x)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<TransactionDto> getAll(String id, Long receiverId){
        List<Transaction> list = transactionRepository.findBySentIdAndUser_Id(id, receiverId);
        List<TransactionDto> dtos = list.stream().map(x -> transactionMapper.toDto(x)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public String create(TransactionDto transactionDto) throws Exception {

        Users user = usersService.getUsersById(transactionDto.getUserId());
        if (!Objects.nonNull(user)){
            throw new ForbiddenException("User not found");
        }
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis()) + "_InvoiceID";
        String orderInfo = "CvBuilder";
        String domain = "https://api-cvbuilder.monoinfinity.net";

        String returnURL = domain + "/api/v1/transaction/query-transaction";
        String notifyURL = domain + "/api/v1/transaction/query-transaction";

        //GET this from HOSTEL OWNER
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        jo.addProperty("uid", transactionDto.getUserId());
        jo.addProperty("transactionId", requestId);
        jo.addProperty("expenditure", transactionDto.getExpenditure());
        String extraData = gson.toJson(jo);

        PartnerInfo partnerInfo = new PartnerInfo(partnerCode, accessKey, secretKey);
        Environment environment = Environment.selectEnv("dev", Environment.ProcessType.PAY_GATE);
        environment.setPartnerInfo(partnerInfo);
        CaptureMoMoResponse captureWalletMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, String.valueOf(transactionDto.getExpenditure()), orderInfo, returnURL, notifyURL, extraData);
        if (captureWalletMoMoResponse.getMessage().equals("Success")){
            Transaction transaction = new Transaction(null, "Momo", requestId, transactionDto.getMomoId(), "", TransactionType.ADD, transactionDto.getMoneyType(), transactionDto.getExpenditure(), transactionDto.getExpenditure() / ratio, 0L, TransactionStatus.PENDING, usersService.getUsersById(transactionDto.getUserId()));
            transactionRepository.save(transaction);
        }
        String redirectLink = captureWalletMoMoResponse.getPayUrl().toString();

        return redirectLink;
    }

    @Override
    public TransactionDto savePaymentStatus(String orderId, String requestId) throws Exception {

        PartnerInfo partnerInfo = new PartnerInfo(partnerCode, accessKey, secretKey);
        Environment environment = Environment.selectEnv("dev", Environment.ProcessType.PAY_GATE);
        environment.setPartnerInfo(partnerInfo);

        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);

        JsonObject s  = new Gson().fromJson(new String(queryStatusTransactionResponse.getExtraData()), JsonObject.class);
        Integer code = queryStatusTransactionResponse.getErrorCode();
        String tid = s.get("transactionId").getAsString();
        String uid = s.get("uid").getAsString();
        Long expenditure = s.get("expenditure").getAsLong();
        Transaction transaction = transactionRepository.findByRequestId(tid);
        if (code.equals(0)) {
            if (Objects.nonNull(transaction)){
                transaction.setStatus(TransactionStatus.SUCCESSFULLY);
            }else {
                throw new InternalServerException("Cannot find transaction status");
            }
            Users user = usersService.getUsersById(Integer.parseInt(uid));
            if (Objects.nonNull(user)){
                if (user instanceof HR){
                    HR hr = (HR) user;
                    if (LocalDate.now().isAfter(hr.getExpiredDay())){
                        hr.setExpiredDay(LocalDate.now());
                    }
                    if (PaymentConstant.vipAMonthRatio.equals(expenditure)){
                        hr.setExpiredDay(hr.getExpiredDay().plusDays(30));
                    } else if (PaymentConstant.vipAYearRatio.equals(expenditure)){
                        hr.setExpiredDay(hr.getExpiredDay().plusDays(365));
                    }
                    hr.setVip(true);
                    usersRepository.save(hr);
                } else {
                    user.setAccountBalance((user.getAccountBalance() + expenditure));
                }
            }
        } else {
            if (Objects.nonNull(transaction)){
                transaction.setStatus(TransactionStatus.FAIL);
            }else {
                throw new InternalServerException("Cannot find transaction status");
            }
        }
        transaction.setMomoId(queryStatusTransactionResponse.getTransId());
        transaction.setResponseMessage(queryStatusTransactionResponse.getLocalMessage());
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto requestToWithdraw(TransactionResponse dto){
        Users user = usersService.getUsersById(dto.getUserId());
        if (dto.getExpenditure().compareTo(user.getAccountBalance()) > 0){
            throw new BadRequestException("Account balance is not enough!");
        }
        String requestId = String.valueOf(System.currentTimeMillis());
        Transaction transaction = new Transaction(null, dto.getSentId(), requestId,  null, null,
            TransactionType.WITHDRAW, MoneyType.CREDIT, dto.getConversionAmount() * ratio, dto.getConversionAmount(), 0L, TransactionStatus.PENDING, usersService.getUsersById(dto.getUserId()));
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto approveWithdrawRequest(String id){
        Transaction transaction = transactionRepository.findByRequestId(id);
        transaction.setStatus(TransactionStatus.SUCCESSFULLY);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionDto> viewWithdrawList() {
        List<Transaction> list = transactionRepository.findAllByTransactionTypeAndStatus(TransactionType.WITHDRAW, TransactionStatus.PENDING);
        List<TransactionDto> dtos = list.stream().map(x -> transactionMapper.toDto(x)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public TransactionDto requestToReview(Integer sentId, Integer receiveId, Double amount){
        String requestId = String.valueOf(System.currentTimeMillis());
        Transaction transaction = new Transaction(null, sentId.toString(), requestId,  null, null,
                TransactionType.TRANSFER, MoneyType.CREDIT, Double.valueOf(amount).longValue(), 0L, 0L, TransactionStatus.PENDING, usersService.getUsersById(receiveId));
        transaction = transactionRepository.save(transaction);

        //giam tien user
        Users user = usersService.getUsersById(sentId);
        user.setAccountBalance((long) (user.getAccountBalance() - amount));
        usersRepository.save(user);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto requestToReviewFail(String requestId){
        Transaction transaction = transactionRepository.findByRequestId(requestId);
        transaction.setStatus(TransactionStatus.FAIL);
        transaction = transactionRepository.save(transaction);

        //tra tien cho candidate
        Users user = usersService.getUsersById(Integer.parseInt(transaction.getSentId()));
        user.setAccountBalance( (user.getAccountBalance() + transaction.getExpenditure()));
        usersRepository.save(user);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto requestToReviewSuccessFul(String requestId){
        Transaction transaction = transactionRepository.findByRequestId(requestId);
        transaction.setStatus(TransactionStatus.SUCCESSFULLY);
        transaction = transactionRepository.save(transaction);

        //cong tien cho expert
        Users user = usersService.getUsersById(transaction.getUser().getId());
        user.setAccountBalance( (user.getAccountBalance() + transaction.getExpenditure()));
        usersRepository.save(user);
        return transactionMapper.toDto(transaction);
    }

}
