package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CustomerViewDto;
import com.example.capstoneproject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/{customerId}/contact")
    public CustomerViewDto getContact(@PathVariable("customerId") int customerId) {
        return customerService.getContactById(customerId);
    }
}
