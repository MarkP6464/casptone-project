package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.TemplateDto;
import com.example.capstoneproject.Dto.TemplateViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TemplateService extends BaseService<TemplateDto, Integer> {
    boolean updateTemplate(Integer id, TemplateDto dto);
    TemplateDto updateView(Integer id);
    List<TemplateViewDto> getAllTemplate();
}
