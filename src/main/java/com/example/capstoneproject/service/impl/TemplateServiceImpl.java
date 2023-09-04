package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.TemplateDto;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.entity.Template;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.SourceWorkMapper;
import com.example.capstoneproject.mapper.TemplateMapper;
import com.example.capstoneproject.repository.SourceWorkRepository;
import com.example.capstoneproject.repository.TemplateRepository;
import com.example.capstoneproject.service.SourceWorkService;
import com.example.capstoneproject.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TemplateServiceImpl extends AbstractBaseService<Template, TemplateDto, Integer> implements TemplateService {
    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    TemplateMapper templateMapper;

    public TemplateServiceImpl(TemplateRepository templateRepository, TemplateMapper templateMapper) {
        super(templateRepository, templateMapper, templateRepository::findById);
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
    }

    @Override
    public List<TemplateDto> getAll() {
        List<Template> templates = templateRepository.findTemplatesByStatus(CvStatus.ACTIVE);
        return templateMapper.mapEntitiesToDtoes(templates);
    }

    @Override
    public TemplateDto update(Integer id, TemplateDto dto) {
        Optional<Template> existingTemplateOptional = templateRepository.findById(id);
        if (existingTemplateOptional.isPresent()) {
            Template existingTemplate = existingTemplateOptional.get();
            if (dto.getName() != null && !existingTemplate.getName().equals(dto.getName())) {
                existingTemplate.setName(dto.getName());
            } else {
                throw new IllegalArgumentException("New Name is the same as the existing template");
            }
            if (dto.getContent() != null && !existingTemplate.getContent().equals(dto.getContent())) {
                existingTemplate.setContent(dto.getContent());
            } else {
                throw new IllegalArgumentException("New Content is the same as the existing template");
            }
            existingTemplate.setStatus(CvStatus.ACTIVE);
            Template updated = templateRepository.save(existingTemplate);
            return templateMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Template ID not found");
        }
    }

    @Override
    public TemplateDto updateView(Integer id) {
        Optional<Template> existingTemplateOptional = templateRepository.findById(id);
        if (existingTemplateOptional.isPresent()) {
            Template existingTemplate = existingTemplateOptional.get();
            existingTemplate.setAmountView(existingTemplate.getAmountView()+1);
            Template updated = templateRepository.save(existingTemplate);
            return templateMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Template ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Template> Optional = templateRepository.findById(id);
        if (Optional.isPresent()) {
            Template template = Optional.get();
            template.setStatus(CvStatus.DELETED);
            templateRepository.save(template);
        }
    }

}
