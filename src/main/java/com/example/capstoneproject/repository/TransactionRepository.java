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

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.status = :transactionStatus " +
            "AND t.transactionType = :transactionType")
    List<Transaction> findAllByTransactionTypeAndStatus(TransactionType transactionType, TransactionStatus transactionStatus);

    List<Transaction> findBySentIdAndUser_Id(String id, Long receiveId);

    List<Transaction> findBySentIdOrUser_Id(String id, Integer userId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (t.sentId = :id  OR t.user.id = :userId)" +
            "AND t.status = :transactionStatus " +
            "AND t.transactionType <> :type")
    List<Transaction> findBySentIdOrUser_IdAndStatusIsAndTransactionTypeNot(String id, Integer userId, TransactionStatus transactionStatus, TransactionType type);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (t.sentId = :id  OR t.user.id = :userId)" +
            "AND t.transactionType = :transactionType")
    List<Transaction> findBySentIdOrUser_IdAndTransactionType(String id, Integer userId, TransactionType transactionType);

    Transaction findByRequestId(String id);

    Optional<Transaction> findById(Long id);

    @Query("SELECT COALESCE(SUM(t.expenditure), 0) FROM Transaction t WHERE t.transactionType = :transactionType AND t.status = :status")
    Double sumExpenditureByTransactionTypeAndStatus(@Param("transactionType") TransactionType transactionType, @Param("status") TransactionStatus status);

    @Query("SELECT COALESCE(SUM(t.expenditure), 0) FROM Transaction t WHERE t.transactionType = :transactionType AND t.status = :status AND DATE_FORMAT(t.createdDate, '%Y-%m-%d') = :targetDate")
    Double sumExpenditureByTransactionTypeAndStatusAndDate(
            @Param("transactionType") TransactionType transactionType,
            @Param("status") TransactionStatus status,
            @Param("targetDate") String targetDate
    );


    Optional<Transaction> findById(Integer transactionId);





}
