package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CustomerDto;
import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.entity.Customer;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.mapper.CustomerMapper;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.repository.CustomerRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.service.CustomerService;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomerServiceImpl extends AbstractBaseService<Customer, CustomerDto, Integer> implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        super(customerRepository, customerMapper, customerRepository::findById);
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        } else {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }
    }
}
