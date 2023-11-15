package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.mapper.TransactionMapper;
import com.example.capstoneproject.repository.TransactionRepository;
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
        if (!Objects.nonNull(usersService.getUsersById(transactionDto.getUserId()))){
            throw new ForbiddenException("User not found");
        }
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis()) + "_InvoiceID";
        String orderInfo = "Dang ky thanh vien VIP cua KitchenZ";
//            String domain = "https://cvbuilder.monoinfinity.net";
        String domain = "http://localhost:8080";

        String returnURL = domain + "/transaction/query-transaction";
        String notifyURL = domain + "/transaction/query-transaction";

        //GET this from HOSTEL OWNER
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        jo.addProperty("uid", transactionDto.getUserId());
        jo.addProperty("transactionId", requestId);
        jo.addProperty("expenditure", transactionDto.getExpenditure());
        jo.addProperty("transactionType", transactionDto.getTransactionType().toString());
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
        String tid = s.get("transactionid").getAsString();
        String uid = s.get("uid").getAsString();
        Long expenditure = s.get("expenditure").getAsLong();
        String transactionType = s.get("transactionType").getAsString();
        Transaction transaction = transactionRepository.findByRequestId(tid);
        if (code.equals(0)) {
            if (Objects.nonNull(transaction)){
                transaction.setStatus(TransactionStatus.SUCCESSFULLY);
            }else {
                throw new InternalServerException("Cannot find transaction status");
            }
            Users user = usersService.getUsersById(Integer.parseInt(uid));
            if (Objects.nonNull(user)){
                if (transactionType.equals("QUOTA")) {
                    user.setQuota( (user.getQuota() + expenditure/ ratio));
                }else {
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
    public TransactionDto requestToWithdraw(TransactionDto dto){
        String requestId = String.valueOf(System.currentTimeMillis());
        Transaction transaction = new Transaction(null, dto.getSentId(), requestId,  null, null,
            TransactionType.WITHDRAW, MoneyType.QUOTA, dto.getConversionAmount() * ratio, dto.getConversionAmount(), 0L, TransactionStatus.PENDING, usersService.getUsersById(dto.getUserId()));
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
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto requestToReviewFail(String requestId){
        Transaction transaction = transactionRepository.findByRequestId(requestId);
        transaction.setStatus(TransactionStatus.FAIL);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto requestToReviewSuccessFul(String requestId){
        Transaction transaction = transactionRepository.findByRequestId(requestId);
        transaction.setStatus(TransactionStatus.FAIL);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

}
