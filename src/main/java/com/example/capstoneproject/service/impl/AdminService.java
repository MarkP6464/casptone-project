package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.entity.Admin;
import com.example.capstoneproject.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin findAdmin() {
        List<Admin> users = adminRepository.findAdmin();
        Admin admin = users.stream().findFirst().get();
        return admin;
    }
}
