package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CustomerDto;
import com.example.capstoneproject.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService extends BaseService<CustomerDto, Integer> {
    Customer getCustomerById(int customerId);
}
