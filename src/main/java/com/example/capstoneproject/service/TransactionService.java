package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.entity.Transaction;
import com.mservice.allinone.models.QueryStatusTransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAll(String id);

    List<TransactionDto> getAll(String id, Long receiverId);

    String create(TransactionDto transactionDto) throws Exception;

    TransactionDto savePaymentStatus(String orderId, String requestId) throws Exception;

    TransactionDto requestToWithdraw(TransactionDto dto);

    TransactionDto approveWithdrawRequest(String id);

    List<TransactionDto> viewWithdrawList();

    TransactionDto requestToReview(Integer sentId, Integer receiveId, Double amount);

    TransactionDto requestToReviewFail(String requestId);

    TransactionDto requestToReviewSuccessFul(String requestId);
}