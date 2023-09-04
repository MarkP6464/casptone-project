package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.TemplateDto;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/templates")
public class TemplateController {
    @Autowired
    TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public List<TemplateDto> getAllTemplate() {
        return templateService.getAll();
    }

    @PostMapping
    public TemplateDto postTemplate(@RequestBody TemplateDto Dto) {
        return templateService.create(Dto);
    }

    @PutMapping("/{templateId}")
    public TemplateDto updateTemplate(@PathVariable("templateId") int templateId, @RequestBody TemplateDto Dto) {
        return templateService.update(templateId, Dto);
    }

    @DeleteMapping("/{templateId}")
    public String deleteTemplate(@PathVariable("templateId") int templateId) {
        templateService.deleteById(templateId);
        return "Delete successful";
    }

    @PutMapping("/{templateId}/views")
    public TemplateDto updateTemplate(@PathVariable("templateId") int templateId) {
        return templateService.updateView(templateId);
    }

}
