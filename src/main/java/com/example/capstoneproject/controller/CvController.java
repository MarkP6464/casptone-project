package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.service.ContactService;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/")
public class CvController {
    @Autowired
    CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/{customerId}/cvs")
    public List<CvDto> getCvsById(@PathVariable("customerId") int customerId) {
        return cvService.GetCvsById(customerId);
    }
}
