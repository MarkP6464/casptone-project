package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SourceWorkService extends BaseService<SourceWorkDto, Integer> {
    SourceWorkDto update(Integer id, SourceWorkDto dto);

    boolean updateSourceWork(int cvId, int sourceWorkId, SourceWorkDto dto);

    List<SourceWorkViewDto> getAllSourceWork(int cvId);
    SourceWorkDto createSourceWork(Integer id, SourceWorkDto dto);
    void deleteSourceWorkById(Integer cvId,Integer sourceId);
}
