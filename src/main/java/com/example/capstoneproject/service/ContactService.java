package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.stereotype.Service;

@Service
public interface ContactService extends BaseService<ContactDto, Integer> {
    ContactDto update(Integer id, ContactDto dto);

    ContactDto GetContactById(int id);
}
