package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.TemplateDto;
import com.example.capstoneproject.entity.Template;
import org.springframework.stereotype.Component;

@Component
public class TemplateMapper extends AbstractMapper<Template, TemplateDto> {
    public TemplateMapper() {
        super(Template.class, TemplateDto.class);
    }
}
