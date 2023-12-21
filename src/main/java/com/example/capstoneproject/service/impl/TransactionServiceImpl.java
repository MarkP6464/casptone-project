package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AddMoneyTransactionDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.request.ImageDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.ExpertConfigViewDto;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.Dto.responses.TransactionViewDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.TransactionMapper;
import com.example.capstoneproject.repository.TransactionRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.TransactionService;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.allinone.models.QueryStatusTransactionResponse;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.allinone.processor.allinone.QueryStatusTransaction;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;

    @Autowired
    UsersService usersService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    AdminConfigurationService adminConfigurationService;

    @Override
    public List<TransactionDto> getAll(String id){
        Integer receivedID = Integer.parseInt(id);
        List<Transaction> list = transactionRepository.findBySentIdOrUser_Id(id, receivedID);
        List<TransactionDto> dtos = list.stream().map(x -> {
            TransactionDto dto = transactionMapper.toDto(x);
            dto.setUserId(receivedID);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<TransactionDto> getAllTransactionType(String id){
        Integer receivedID = Integer.parseInt(id);
        List<Transaction> list = transactionRepository.findBySentIdOrUser_IdAndTransactionType(id, receivedID, TransactionType.WITHDRAW);
        List<TransactionDto> dtos = list.stream().map(x -> {
            TransactionDto dto = transactionMapper.toDto(x);
            dto.setUserId(receivedID);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<TransactionDto> getAllSuccessfull(String id){
        Integer receivedID = Integer.parseInt(id);
        List<Transaction> list = transactionRepository.findBySentIdOrUser_IdAndStatusIsAndTransactionTypeNot(id, receivedID, TransactionStatus.SUCCESSFUL, TransactionType.WITHDRAW);
        List<TransactionDto> dtos = list.stream().map(x -> {
            TransactionDto dto = transactionMapper.toDto(x);
            dto.setUserId(receivedID);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public List<TransactionDto> showAll(){
        List<Transaction> list = transactionRepository.findAll();
        List<TransactionDto> dtos = list.stream().map(x -> {
            TransactionDto dto = transactionMapper.toDto(x);
            dto.setUserId(x.getUser().getId());
            return dto;
        }).collect(Collectors.toList());
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
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis()) + "_InvoiceID";
        String orderInfo = "CvBuilder";
        String domain = null;
        if(MoneyType.SUBSCRIPTION.equals(transactionDto.getMoneyType())){
            domain = "https://cvbuilder.monoinfinity.net/paymentcallback/hr";
        }else {
            domain = "https://cvbuilder.monoinfinity.net/paymentcallback";
        }

        String returnURL = domain;
        String notifyURL = domain;

        //GET this from HOSTEL OWNER
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        jo.addProperty("uid", transactionDto.getUserId());
        jo.addProperty("transactionId", requestId);
        jo.addProperty("expenditure", transactionDto.getExpenditure());
        String extraData = gson.toJson(jo);

        PartnerInfo partnerInfo = new PartnerInfo(partnerCode, accessKey, secretKey);
        Environment environment = Environment.selectEnv("dev", Environment.ProcessType.PAY_GATE);
        AdminConfigurationResponse config = adminConfigurationService.getByAdminId(1);
//        Double ratio = config.getMoneyRatio();
        environment.setPartnerInfo(partnerInfo);
        CaptureMoMoResponse captureWalletMoMoResponse = CaptureMoMo.process(environment, orderId, requestId, String.valueOf(transactionDto.getExpenditure().longValue()), orderInfo, returnURL, notifyURL, extraData);
        if (Objects.isNull(captureWalletMoMoResponse)){
            throw new InternalServerException("Momo service is not available");
        }
        if (captureWalletMoMoResponse.getMessage().equals("Success")){
            Transaction transaction = new Transaction(null, "Momo", requestId, transactionDto.getMomoId(), transactionDto.getResponseMessage(), TransactionType.ADD, transactionDto.getMoneyType(), transactionDto.getExpenditure(), transactionDto.getExpenditure() / 1, 0L, TransactionStatus.PROCESSING, usersService.getUsersById(transactionDto.getUserId()));
            transactionRepository.save(transaction);
        }
        String redirectLink = captureWalletMoMoResponse.getPayUrl().toString();

        return redirectLink;
    }

    @Override
    public AddMoneyTransactionDto saveTransactionStatus(String orderId, String requestId) throws Exception {

        PartnerInfo partnerInfo = new PartnerInfo(partnerCode, accessKey, secretKey);
        Environment environment = Environment.selectEnv("dev", Environment.ProcessType.PAY_GATE);
        environment.setPartnerInfo(partnerInfo);

        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, requestId);
        if (Objects.isNull(queryStatusTransactionResponse)){
            throw new InternalServerException("Momo service is not available");
        }
        JsonObject s  = new Gson().fromJson(new String(queryStatusTransactionResponse.getExtraData()), JsonObject.class);
        Integer code = queryStatusTransactionResponse.getErrorCode();
        String tid = s.get("transactionId").getAsString();
        String uid = s.get("uid").getAsString();
        Double expenditure = s.get("expenditure").getAsDouble();
        Transaction transaction = transactionRepository.findByRequestId(tid);
        AdminConfigurationResponse config = adminConfigurationService.getByAdminId(1);
        Double ratio = 1.0;
        if (TransactionStatus.PROCESSING.equals(transaction.getStatus())){
            if (code.equals(0)) {
                if (Objects.nonNull(transaction)){
                    transaction.setStatus(TransactionStatus.SUCCESSFUL);
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
                        if (config.getVipMonthRatio().equals(expenditure)){
                            hr.setExpiredDay(hr.getExpiredDay().plusDays(30));
                        } else if (config.getVipYearRatio().equals(expenditure)){
                            hr.setExpiredDay(hr.getExpiredDay().plusDays(365));
                        }
                        hr.setVip(true);
                        usersRepository.save(hr);
                    } else {
                        user.setAccountBalance((user.getAccountBalance() + expenditure / ratio));
                    }
                }
            } else {
                if (Objects.nonNull(transaction)){
                    transaction.setStatus(TransactionStatus.FAIL);
                }else {
                    throw new InternalServerException("Cannot find transaction status");
                }
            }
        }else {
            throw new BadRequestException("transaction status is not available to update");
        }

        transaction.setMomoId(queryStatusTransactionResponse.getTransId());
        transaction = transactionRepository.save(transaction);
        AddMoneyTransactionDto addMoneyTransactionDto = new AddMoneyTransactionDto();
        addMoneyTransactionDto.setMomoResponse(queryStatusTransactionResponse);
        addMoneyTransactionDto.setConversionAmount(expenditure / ratio);
        return addMoneyTransactionDto;
    }

    @Override
    public TransactionDto requestToWithdraw(TransactionResponse dto) throws JsonProcessingException {
        Users user = usersService.getUsersById(dto.getUserId());
        if (dto.getExpenditure().compareTo(user.getAccountBalance()) > 0){
            throw new BadRequestException("Account balance is not enough!");
        }
        Expert expert = null;
        if (user instanceof Expert){
            expert =  (Expert) user;
        }
        if (Objects.isNull(expert.getBankAccountNumber()) || Objects.isNull(expert.getBankAccountName())){
            throw new BadRequestException("Please setting your bank account to withdraw!!");
        }
        AdminConfigurationResponse config = adminConfigurationService.getByAdminId(1);
        Double ratio = 1.0;
        String requestId = String.valueOf(System.currentTimeMillis());
        Transaction transaction = new Transaction(null, "1", requestId,  null, "Withdraw request",
            TransactionType.WITHDRAW, MoneyType.CREDIT, dto.getExpenditure(), dto.getExpenditure() , 0L, TransactionStatus.PROCESSING, usersService.getUsersById(dto.getUserId()));
        transaction.setBankAccountName(expert.getBankAccountName());
        transaction.setBankName(expert.getBankName());
        transaction.setBankAccountNumber(expert.getBankAccountNumber());
        transaction = transactionRepository.save(transaction);
        expert.setAccountBalance(expert.getAccountBalance() - dto.getExpenditure());
        usersRepository.save(expert);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto approveWithdrawRequest(String id) throws FileNotFoundException {
        Transaction transaction = transactionRepository.findByRequestId(id);
        if (Objects.nonNull(transaction)){
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setUpdateDate(LocalDateTime.now());
            ApplicationLogServiceImpl.sendEmail(transaction.getUser().getEmail(), "Withdraw request approved.",
                    "Your withdraw request was approved! Thank you.");
            transactionRepository.save(transaction);
        } else throw new ResourceNotFoundException("transaction id not found!!");
        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionViewDto> viewWithdrawList() {
        List<Transaction> transactions = transactionRepository.findBySentIdOrUser_IdAndTransactionType("1", 1, TransactionType.WITHDRAW);
        List<TransactionViewDto> transactionViews = new ArrayList<>();
        for(Transaction transaction: transactions){
            TransactionViewDto transactionView = new TransactionViewDto();
            transactionView.setId(transaction.getId());
            transactionView.setSentId(transaction.getSentId());
            transactionView.setRequestId(transaction.getRequestId());
            transactionView.setMomoId(transaction.getMomoId());
            transactionView.setResponseMessage(transaction.getResponseMessage());
            transactionView.setTransactionType(transaction.getTransactionType());
            transactionView.setMoneyType(transaction.getMoneyType());
            transactionView.setExpenditure(transaction.getExpenditure());
            transactionView.setConversionAmount(transaction.getConversionAmount());
            transactionView.setProof(transaction.getProof());
            transactionView.setStatus(transaction.getStatus());
            transactionView.setCreatedDate(transaction.getCreatedDate());
            UsersDto usersDto = new UsersDto();
            usersDto.setId(transaction.getUser().getId());
            usersDto.setName(transaction.getUser().getName());
            transactionView.setReceiver(usersDto);
            HRBankRequest bank = new HRBankRequest();
            Users users = transaction.getUser();
            if (Objects.nonNull(users)) {
                if (users instanceof Expert expert) {
                    bank.setId(expert.getId());
                    bank.setBankName(expert.getBankName());
                    bank.setBankAccountName(expert.getBankAccountName());
                    bank.setBankAccountNumber(expert.getBankAccountNumber());
                    transactionView.setBank(bank);
                }
            }
            transactionViews.add(transactionView);
        }
        return transactionViews;
    }

    @Override
    public String uploadImageConfirm(Long transactionId, ImageDto dto) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if(transactionOptional.isPresent()){
            Transaction transaction = transactionOptional.get();
            transaction.setProof(dto.getImage());
            transactionRepository.save(transaction);
            return "Upload image successful.";
        }else{
            throw new BadRequestException("Transaction is not exist.");
        }
    }

    @Override
    public TransactionDto requestToReview(Integer sentId, Integer receiveId, Double amount){

        //giam tien user
        Users user = usersService.getUsersById(sentId);
        if (user.getAccountBalance() < amount.longValue()){
            throw new BadRequestException("Account Balance is not enough!!");
        }
        user.setAccountBalance((user.getAccountBalance() - amount));
        usersRepository.save(user);

        String requestId = String.valueOf(System.currentTimeMillis());
        Transaction transaction = new Transaction(null, sentId.toString(), requestId,  null, "Review request",
                TransactionType.TRANSFER, MoneyType.CREDIT, amount, 0.0, 0L, TransactionStatus.PROCESSING, usersService.getUsersById(receiveId));
        transaction = transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

    @Override
    public Transaction getById(Long id){
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (Objects.nonNull(transaction)) {
            return transaction.get();
        } else throw new BadRequestException("Not found transaction");
    }

    @Override
    public TransactionDto requestToReviewFail(String requestId){
        Transaction transaction = transactionRepository.findById(Long.parseLong(requestId)).get();
        if (TransactionStatus.PROCESSING.equals(transaction.getStatus())) {
            transaction.setStatus(TransactionStatus.FAIL);
            transaction.setUpdateDate(LocalDateTime.now().plusHours(7));
            transaction = transactionRepository.save(transaction);

            //tra tien cho candidate
            Users user = usersService.getUsersById(Integer.parseInt(transaction.getSentId()));
            user.setAccountBalance((user.getAccountBalance() + transaction.getConversionAmount()));
            usersRepository.save(user);
            return transactionMapper.toDto(transaction);
        } else throw new BadRequestException("transaction status is not available to update");
    }

    @Override
    public TransactionDto requestToReviewSuccessFul(String requestId){
        Transaction transaction = transactionRepository.findById(Long.parseLong(requestId)).get();
        if (TransactionStatus.PROCESSING.equals(transaction.getStatus())) {
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setUpdateDate(LocalDateTime.now().plusHours(7));
            transaction = transactionRepository.save(transaction);

            //cong tien cho expert
            Users user = usersService.getUsersById(transaction.getUser().getId());
            user.setAccountBalance( (user.getAccountBalance() + transaction.getConversionAmount()));
            usersRepository.save(user);
            return transactionMapper.toDto(transaction);
        } else throw new BadRequestException("transaction status is not available to update");
    }

    @Override
    public TransactionDto chargePerRequest(Integer userId, String message){
        Users users = usersService.getUsersById(userId);
        if (Double.compare(users.getAccountBalance(), 1000L) >= 0 ){
            String requestId = String.valueOf(System.currentTimeMillis());
            Transaction transaction = new Transaction(null, String.valueOf(userId), requestId, null, message, TransactionType.SERVICE, MoneyType.CREDIT, 1000.0, 1.0,null, TransactionStatus.SUCCESSFUL, usersService.getUsersById(1));
            Transaction transaction1 = transactionRepository.save(transaction);
            users.setAccountBalance(users.getAccountBalance()-1000L);
            usersRepository.save(users);
            return transactionMapper.toDto(transaction1);
        }else throw  new BadRequestException("account balance is not enough!!");
    }
}
