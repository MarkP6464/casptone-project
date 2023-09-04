package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.TemplateDto;
import org.springframework.stereotype.Service;

@Service
public interface TemplateService extends BaseService<TemplateDto, Integer> {
    TemplateDto update(Integer id, TemplateDto dto);
    TemplateDto updateView(Integer id);

}
