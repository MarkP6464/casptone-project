package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CustomerDto;
import com.example.capstoneproject.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends AbstractMapper<Customer, CustomerDto> {
    public CustomerMapper() {
        super(Customer.class, CustomerDto.class);
    }
}
