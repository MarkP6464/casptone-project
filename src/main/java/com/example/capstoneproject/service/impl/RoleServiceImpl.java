package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.entity.Role;
import com.example.capstoneproject.repository.RoleRepository;
import com.example.capstoneproject.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findRole(Integer id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isPresent()){
            return roleOptional.get();
        }else{
            return null;
        }
    }
}
