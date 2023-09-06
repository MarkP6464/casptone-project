package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.mapper.ContactMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.repository.ContactRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl extends AbstractBaseService<Contact, ContactDto, Integer> implements ContactService {
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        super(contactRepository, contactMapper, contactRepository::findById);
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    public ContactDto update(Integer id, ContactDto dto) {
        Optional<Contact> existingContactOptional = contactRepository.findById(id);
        if (existingContactOptional.isPresent()) {
            Contact existingContact = existingContactOptional.get();
            if (dto.getFullName() != null && !existingContact.getFullName().equals(dto.getFullName())) {
                existingContact.setFullName(dto.getFullName());
            } else {
                throw new IllegalArgumentException("New Full Name is the same as the existing contact");
            }
            if (dto.getPhone() != null && !existingContact.getPhone().equals(dto.getPhone())) {
                existingContact.setPhone(dto.getPhone());
            } else {
                throw new IllegalArgumentException("New Phone is the same as the existing contact");
            }
            if (dto.getWebsite() != null && !existingContact.getWebsite().equals(dto.getWebsite())) {
                existingContact.setWebsite(dto.getWebsite());
            } else {
                throw new IllegalArgumentException("New Website is the same as the existing contact");
            }
            if (dto.getState() != null && !existingContact.getState().equals(dto.getState())) {
                existingContact.setState(dto.getState());
            } else {
                throw new IllegalArgumentException("New State is the same as the existing contact");
            }
            if (dto.getEmail() != null && !existingContact.getEmail().equals(dto.getEmail())) {
                existingContact.setEmail(dto.getEmail());
            } else {
                throw new IllegalArgumentException("New Email is the same as the existing contact");
            }
            if (dto.getLinkin() != null && !existingContact.getLinkin().equals(dto.getLinkin())) {
                existingContact.setLinkin(dto.getLinkin());
            } else {
                throw new IllegalArgumentException("New Linkin is the same as the existing contact");
            }
            if (dto.getCountry() != null && !existingContact.getCountry().equals(dto.getCountry())) {
                existingContact.setCountry(dto.getCountry());
            } else {
                throw new IllegalArgumentException("New Country is the same as the existing contact");
            }
            existingContact.setStatus(CvStatus.ACTIVE);
            Contact updated = contactRepository.save(existingContact);
            return contactMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Contact ID not found");
        }
    }

    @Override
    public ContactDto GetContactById(int id) {
        Contact contact = contactRepository.findContactByIdAndStatus(id, CvStatus.ACTIVE);
        return contactMapper.mapEntityToDto(contact);
    }

}
