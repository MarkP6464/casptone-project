package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.TemplateDto;
import com.example.capstoneproject.Dto.TemplateViewDto;
import com.example.capstoneproject.entity.Template;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.TemplateMapper;
import com.example.capstoneproject.repository.TemplateRepository;
import com.example.capstoneproject.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public TemplateDto create(TemplateDto dto) {
        Template template = templateMapper.mapDtoToEntity(dto);
        template.setStatus(BasicStatus.ACTIVE);
        Template saved = templateRepository.save(template);
        return templateMapper.mapEntityToDto(saved);
    }

    @Override
    public List<TemplateViewDto> getAllTemplate() {
        List<Template> templates = templateRepository.findTemplatesByStatus(BasicStatus.ACTIVE);
        List<TemplateViewDto> templateDtos = new ArrayList<>();

        for (Template template : templates) {
            TemplateViewDto templateDto = new TemplateViewDto();
            templateDto.setId(template.getId());
            templateDto.setAmountView(template.getAmountView());
            templateDto.setContent(template.getContent());
            templateDto.setName(template.getName());

            templateDtos.add(templateDto);
        }

        return templateDtos;
    }

    @Override
    public boolean updateTemplate(Integer id, TemplateDto dto) {
        Optional<Template> existingTemplateOptional = templateRepository.findById(id);
        if (existingTemplateOptional.isPresent()) {
            Template existingTemplate = existingTemplateOptional.get();
            if (dto.getName() != null && !existingTemplate.getName().equals(dto.getName())) {
                existingTemplate.setName(dto.getName());
            } else {
                existingTemplate.setName(existingTemplate.getName());
            }
            if (dto.getContent() != null && !existingTemplate.getContent().equals(dto.getContent())) {
                existingTemplate.setContent(dto.getContent());
            } else {
                existingTemplate.setContent(existingTemplate.getContent());
            }
            if (dto.getAmountView() > 0 && existingTemplate.getAmountView() != dto.getAmountView()) {
                existingTemplate.setAmountView(dto.getAmountView());
            } else {
                existingTemplate.setAmountView(existingTemplate.getAmountView());
            }
            existingTemplate.setStatus(BasicStatus.ACTIVE);
            Template updated = templateRepository.save(existingTemplate);
            templateMapper.mapEntityToDto(updated);
            return true;
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
            template.setStatus(BasicStatus.DELETED);
            templateRepository.save(template);
        }
    }

}
