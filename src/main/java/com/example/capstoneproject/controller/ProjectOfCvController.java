package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.service.InvolvementOfCvService;
import com.example.capstoneproject.service.ProjectOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class ProjectOfCvController {
    @Autowired
    ProjectOfCvService projectOfCvService;

    public ProjectOfCvController(ProjectOfCvService projectOfCvService) {
        this.projectOfCvService = projectOfCvService;
    }

    @GetMapping("/{cvId}/projects/project-of-cv")
    public List<ProjectViewDto> getAllProject(@PathVariable("cvId") int cvId) {
        return projectOfCvService.getActiveProjectsByCvId(cvId);
    }
    @GetMapping("/{cvId}/projects")
    public List<ProjectViewDto> getAllProjectNotYet(@PathVariable("cvId") int cvId) {
        return projectOfCvService.getAllProject(cvId);
    }
    @PostMapping("/{cvId}/projects/{projectId}")
    public String postProjectOfCv(@PathVariable("cvId") int cvId, @PathVariable("projectId") int projectId) {
        boolean check = projectOfCvService.createProjectOfCv(cvId,projectId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/projects/{projectId}")
    public String deleteProjectOfCv(@PathVariable("cvId") int cvId, @PathVariable("projectId") int projectId) {
        boolean check = projectOfCvService.deleteProjectOfCv(cvId,projectId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
