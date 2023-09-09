package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.Dto.ContactViewDto;
import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.stereotype.Service;

@Service
public interface ContactService extends BaseService<ContactDto, Integer> {
    boolean updateContact(Integer id, ContactDto dto);

    ContactViewDto GetContactById(int id);
}
