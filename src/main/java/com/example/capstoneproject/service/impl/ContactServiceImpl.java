package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.Dto.ContactViewDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.entity.Cv;
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
    public ContactDto create(ContactDto dto) {
        Contact contact = contactMapper.mapDtoToEntity(dto);
        contact.setStatus(CvStatus.ACTIVE);
        Contact saved = contactRepository.save(contact);
        return contactMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateContact(Integer id, ContactDto dto) {
        Optional<Contact> existingContactOptional = contactRepository.findById(id);
        if (existingContactOptional.isPresent()) {
            Contact existingContact = existingContactOptional.get();
            if (dto.getFullName() != null && !existingContact.getFullName().equals(dto.getFullName())) {
                existingContact.setFullName(dto.getFullName());
            } else {
                existingContact.setFullName(existingContact.getFullName());
            }
            if (dto.getPhone() != null && !existingContact.getPhone().equals(dto.getPhone())) {
                existingContact.setPhone(dto.getPhone());
            } else {
                existingContact.setPhone(existingContact.getPhone());
            }
            if (dto.getWebsite() != null && !existingContact.getWebsite().equals(dto.getWebsite())) {
                existingContact.setWebsite(dto.getWebsite());
            } else {
                existingContact.setWebsite(existingContact.getWebsite());
            }
            if (dto.getState() != null && !existingContact.getState().equals(dto.getState())) {
                existingContact.setState(dto.getState());
            } else {
                existingContact.setState(existingContact.getState());
            }
            if (dto.getEmail() != null && !existingContact.getEmail().equals(dto.getEmail())) {
                existingContact.setEmail(dto.getEmail());
            } else {
                existingContact.setEmail(existingContact.getEmail());
            }
            if (dto.getLinkin() != null && !existingContact.getLinkin().equals(dto.getLinkin())) {
                existingContact.setLinkin(dto.getLinkin());
            } else {
                existingContact.setLinkin(existingContact.getLinkin());
            }
            if (dto.getCountry() != null && !existingContact.getCountry().equals(dto.getCountry())) {
                existingContact.setCountry(dto.getCountry());
            } else {
                existingContact.setCountry(existingContact.getCountry());
            }
            existingContact.setStatus(CvStatus.ACTIVE);
            Contact updated = contactRepository.save(existingContact);
            return true;
        } else {
            throw new IllegalArgumentException("Contact ID not found");
        }
    }

    @Override
    public ContactViewDto GetContactById(int id) {
        Contact contact = contactRepository.findContactByIdAndStatus(id, CvStatus.ACTIVE);
        if (contact != null) {
            ContactViewDto contactDto = new ContactViewDto();
            contactDto.setId(contact.getId());
            contactDto.setCountry(contact.getCountry());
            contactDto.setEmail(contact.getEmail());
            contactDto.setFullName(contact.getFullName());
            contactDto.setLinkin(contact.getLinkin());
            contactDto.setPhone(contact.getPhone());
            contactDto.setState(contact.getState());
            contactDto.setWebsite(contact.getWebsite());
            return contactDto;
        } else {
            throw new IllegalArgumentException("Contact not found with id: " + id);
        }
    }


}
