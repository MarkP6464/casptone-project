package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionStatus;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping
    public List<TransactionDto> getAll(@RequestParam String sentId, @RequestParam(required = false) Long receiverId){
        List<TransactionDto> list = null;
        list = Objects.nonNull(receiverId) ? transactionService.getAll(sentId, receiverId) : transactionService.getAll(sentId);
        return list;
    }

    @PostMapping("/input-quota")
    public RedirectView createTransaction(@RequestBody TransactionDto transactionDto) throws Exception {
            transactionDto.setMoneyType(MoneyType.QUOTA);
            String returnUrl = transactionService.create(transactionDto);
            return new RedirectView(returnUrl);
    }

    @PostMapping("/input-credit")
    public RedirectView addCredit(@RequestBody TransactionDto transactionDto) throws Exception {
        transactionDto.setMoneyType(MoneyType.CREDIT);
        String returnUrl = transactionService.create(transactionDto);
        return new RedirectView(returnUrl);
    }

    @PostMapping(value = "/query-transaction")
    public ResponseEntity queryPayment(@RequestParam String orderId, @RequestParam String requestId) throws Exception {
            TransactionDto transaction = transactionService.savePaymentStatus(orderId, requestId);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity withdraw(@RequestBody TransactionDto transactionDto) throws Exception {
        TransactionDto transaction = transactionService.requestToWithdraw(transactionDto);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PostMapping(value = "/approve-withdraw-request")
    public ResponseEntity approve(@RequestBody String id) throws Exception {
        TransactionDto transaction = transactionService.approveWithdrawRequest(id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @PostMapping(value = "/reject-withdraw-request")
    public ResponseEntity reject(@RequestBody String id) throws Exception {
        TransactionDto transaction = transactionService.approveWithdrawRequest(id);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @GetMapping(value = "/view-withdraw-request")
    public ResponseEntity viewWithdrawList() throws Exception {
        List<TransactionDto> list =  transactionService.viewWithdrawList();
        return ResponseEntity.status(HttpStatus.OK).body(list);
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
