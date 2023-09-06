package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{contactId}")
    public ContactDto getContactById(@PathVariable("contactId") int contactId) {
        return contactService.GetContactById(contactId);
    }

    @PostMapping
    public ContactDto postContact(@RequestBody ContactDto Dto) {
        return contactService.create(Dto);
    }

    @PutMapping("/{contactId}")
    public ContactDto updateContact(@PathVariable("contactId") int contactId, @RequestBody ContactDto Dto) {
        return contactService.update(contactId, Dto);
    }
}
