package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.InvolvementOfCvDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.Dto.ProjectOfCvDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectOfCvService extends BaseService<ProjectOfCvDto, Integer> {
    boolean createProjectOfCv(Integer cvId, Integer projectId);
    boolean deleteProjectOfCv(Integer cvId, Integer projectId);
    List<ProjectViewDto> getActiveProjectsByCvId(Integer cvId);
    List<ProjectViewDto> getAllProject(int cvId);
}
