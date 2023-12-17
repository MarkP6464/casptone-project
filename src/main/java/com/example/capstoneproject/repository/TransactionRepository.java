package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySentId(String id);

//    @Query("SELECT t FROM Transaction t WHERE t.user.id = :receiverId AND t.id = :transactionId")
    Optional<Transaction> findByUser_IdAndId(@Param("receiverId") Integer receiverId, @Param("transactionId") Long transactionId);

    @Query("SELECT SUM(t.expenditure) FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.transactionType = :transactionType " +
            "AND t.status = :status")
    Double sumExpenditureByUserIdAndTransactionTypeAndStatus(
            @Param("userId") Integer userId,
            @Param("transactionType") TransactionType transactionType,
            @Param("status") TransactionStatus status
    );

    List<Transaction> findAllByTransactionTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

    List<Transaction> findBySentIdAndUser_Id(String id, Long receiveId);

    List<Transaction> findBySentIdOrUser_Id(String id, Integer userId);

    Transaction findByRequestId(String id);

    Optional<Transaction> findById(Long id);
}
