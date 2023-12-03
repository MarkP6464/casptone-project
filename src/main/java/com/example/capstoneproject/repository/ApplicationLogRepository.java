package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationLogRepository extends JpaRepository<ApplicationLog, Integer> {
    List<ApplicationLog> findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(Integer userId, Integer postingId);

    List<ApplicationLog> findAllByJobPosting_IdOrderByTimestampDesc(Integer postingId);
    List<ApplicationLog> findAllByJobPosting_User_IdOrderByTimestamp(Integer id);


    List<ApplicationLog> findAllByUser_IdOrderByTimestamp(Integer id);

    @Query("SELECT COUNT(al) FROM ApplicationLog al WHERE al.jobPosting.id = :jobPostingId")
    int countByJobPostingId(@Param("jobPostingId") Integer jobPostingId);
}
