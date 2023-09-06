package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper extends AbstractMapper<Contact, ContactDto> {
    public ContactMapper() {
        super(Contact.class, ContactDto.class);
    }
}
