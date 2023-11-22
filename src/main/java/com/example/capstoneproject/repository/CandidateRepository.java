package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Candidate;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Optional<Candidate> findByIdAndRole_RoleName(Integer candidateId, RoleType roleName);
    List<Candidate> findAllByPublishTrueAndRole_RoleName(RoleType roleName);
}
