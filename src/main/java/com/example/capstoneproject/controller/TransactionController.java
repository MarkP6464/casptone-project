package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AddMoneyTransactionDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.request.ImageDto;
import com.example.capstoneproject.Dto.request.WithdrawRequest;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.Dto.responses.TransactionViewDto;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.repository.TransactionRepository;
import com.example.capstoneproject.service.TransactionService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.allinone.models.QueryStatusTransactionResponse;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.allinone.processor.allinone.QueryStatusTransaction;
import com.mservice.shared.constants.RequestType;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/get-all/{user-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert', 'read:hr', 'read:admin')")
    public List<TransactionDto> getAll(@PathVariable("user-id") String sentId){
        List<TransactionDto> list = transactionService.getAllSuccessfull(sentId);
        return list;
    }

    @GetMapping("/show-all")
    @PreAuthorize("hasAnyAuthority('read:admin')")
    public List<TransactionDto> showAll(){
        List<TransactionDto> list = transactionService.showAll();
        return list;
    }

    @PostMapping("/input-credit")
    @PreAuthorize("hasAnyAuthority('create:candidate', 'create:hr', 'create:expert')")
    public String addCredit(@RequestBody TransactionResponse dto) throws Exception {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setMoneyType(MoneyType.CREDIT);
        transactionDto.setExpenditure(dto.getExpenditure());
        transactionDto.setConversionAmount(dto.getConversionAmount());
        transactionDto.setUserId(dto.getUserId());
        transactionDto.setTransactionType(TransactionType.ADD);
        transactionDto.setResponseMessage("Deposite money " + transactionDto.getExpenditure() + " VND");
        String returnUrl = transactionService.create(transactionDto);
        return returnUrl;
    }

    @GetMapping(value = "/query-transaction")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert', 'read:hr', 'read:admin')")
    public ResponseEntity queryPayment(@RequestParam String orderId, @RequestParam String requestId) throws Exception {
        AddMoneyTransactionDto transaction = transactionService.saveTransactionStatus(orderId, requestId);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PostMapping(value = "/withdraw")
    @PreAuthorize("hasAnyAuthority('create:expert')")
    public ResponseEntity withdraw(@RequestBody WithdrawRequest request) throws Exception {
        TransactionResponse dto = new TransactionResponse();
        dto.setUserId(request.getReceiverId());
        dto.setExpenditure(request.getExpenditure());
        TransactionDto transaction = transactionService.requestToWithdraw(dto);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PostMapping(value = "/approve-withdraw-request/{request-id}")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity approve(@PathVariable("request-id") String id ) throws Exception {
        TransactionDto transaction = transactionService.approveWithdrawRequest(id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @GetMapping(value = "/view-withdraw-request")
    public ResponseEntity viewWithdrawList() throws Exception {
        List<TransactionViewDto> list =  transactionService.viewWithdrawList();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PutMapping("/{transaction-id}/admin/withdraw/image")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity<?> updateImageTransaction(@PathVariable("transaction-id") Long transactionId, @RequestBody ImageDto dto) throws Exception {
        return ResponseEntity.ok(transactionService.uploadImageConfirm(transactionId, dto));
    }

//
//    @GetMapping("/create")
//    public Payment create(@RequestParam String uid){
//        Users users = userRepository.findByUid(uid);
//        PaymentStatus theLastStatus = users.getPayments().get(users.getPayments().size() - 1).getStatus();
//        Payment payment = null;
//        if (theLastStatus.equals(PaymentStatus.EXPIRED)){
//            payment =  new Payment();
//            payment.setAmount(60000L);
//            payment.setStatus(PaymentStatus.INCOMPLETE);
//            payment.setUser(users);
//            payment = paymentRepository.save(payment);
//        } else if (theLastStatus.equals(PaymentStatus.INCOMPLETE)){
//            payment = users.getPayments().get(users.getPayments().size() - 1);
//        }
//        return payment;
//    }
//
//    @GetMapping("/update")
//    public Payment update(@RequestParam String id){
//        Payment payment = paymentRepository.getById(Long.valueOf(id));
//        payment.setStatus(PaymentStatus.COMPLETED);
//        return paymentRepository.save(payment);
//    }
//
//    @GetMapping("/list")
//    public List<Payment> list(){
//        return paymentRepository.findAll();
//    }
}
