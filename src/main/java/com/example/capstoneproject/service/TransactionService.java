package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.AddMoneyTransactionDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.request.ImageDto;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.Dto.responses.TransactionViewDto;
import com.example.capstoneproject.entity.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mservice.allinone.models.QueryStatusTransactionResponse;

import java.io.FileNotFoundException;
import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAll(String id);

    List<TransactionDto> getAllTransactionType(String id);

    List<TransactionDto> getAllSuccessfull(String id);

    List<TransactionDto> showAll();

    List<TransactionDto> getAll(String id, Long receiverId);

    String create(TransactionDto transactionDto) throws Exception;

    AddMoneyTransactionDto saveTransactionStatus(String orderId, String requestId) throws Exception;

    TransactionDto requestToWithdraw(TransactionResponse dto) throws JsonProcessingException;

    TransactionDto approveWithdrawRequest(String id) throws FileNotFoundException;

    List<TransactionViewDto> viewWithdrawList();

    String uploadImageConfirm(Long transactionId, ImageDto dto);

    TransactionDto requestToReview(Integer sentId, Integer receiveId, Double amount);

    Transaction getById(Long id);

    TransactionDto requestToReviewFail(String requestId);

    TransactionDto requestToReviewSuccessFul(String requestId);
    TransactionDto chargePerRequest(Integer userId, String message);
}
