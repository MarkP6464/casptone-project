package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ContactDto;
import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.service.ContactService;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
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
    @GetMapping("/cv/{cvId}")
    public CvDto getCvsByCvId(@PathVariable("cvId") int cvId) {
        return cvService.GetCvsByCvId(cvId);
    }
    @PostMapping
    public CvAddNewDto postCv(@RequestBody CvAddNewDto Dto) {
        return cvService.createCv(Dto);
    }
    @PutMapping("/cv/{cvId}")
    public String updateContact(@PathVariable("cvId") int cvId, @RequestBody CvAddNewDto Dto) {
        boolean check = cvService.updateCv(cvId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/cv/{cvId}")
    public String deleteCv(@PathVariable("cvId") int cvId) {
        cvService.deleteById(cvId);
        return "Delete successful";
    }
}
