package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.service.InvolvementService;
import com.example.capstoneproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{cvId}")
    public List<ProjectViewDto> getAllProject(@PathVariable("cvId") int cvId) {
        return projectService.getAllProject(cvId);
    }

    @PostMapping
    public ProjectDto postProject(@RequestBody ProjectDto Dto) {
        return projectService.create(Dto);
    }

    @PutMapping("/{projectId}")
    public String updateProjectDto(@PathVariable("projectId") int projectId, @RequestBody ProjectViewDto Dto) {
        boolean check = projectService.updateProject(projectId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable("projectId") int projectId) {
        projectService.deleteById(projectId);
        return "Delete successful";
    }
}
