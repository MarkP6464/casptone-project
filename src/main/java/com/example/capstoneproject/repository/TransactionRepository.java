package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySentId(String id);

    List<Transaction> findAllByTransactionTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

    List<Transaction> findBySentIdAndUser_Id(String id, Long receiveId);

    Transaction findByRequestId(String id);

    Transaction getById(String id);
}
