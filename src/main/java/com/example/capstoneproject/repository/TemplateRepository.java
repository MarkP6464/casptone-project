package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.entity.Template;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    @Query("SELECT c FROM Template c WHERE c.Status = :status")
    List<Template> findTemplatesByStatus(@Param("status") CvStatus status);

    Optional<Template> findById(int templateId);
}
