package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SourceWorkService extends BaseService<SourceWorkDto, Integer> {
    SourceWorkDto update(Integer id, SourceWorkDto dto);

    boolean updateSourceWork(Integer id, SourceWorkViewDto dto);

    List<SourceWorkViewDto> getAllSourceWork(int cvId);
}
