package com.example.capstoneproject.service;

import com.example.capstoneproject.entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role findRole(Integer id);
}
