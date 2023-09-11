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
@RequestMapping("/api/v1/customer")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{customerId}/projects")
    public List<ProjectViewDto> getAllProject(@PathVariable("customerId") int customerId) {
        return projectService.getAllProject(customerId);
    }

    @PostMapping("/{customerId}/projects")
    public ProjectDto postProject(@PathVariable("customerId") int customerId,@RequestBody ProjectDto Dto) {
        return projectService.createProject(customerId,Dto);
    }

    @PutMapping("/{customerId}/projects/{projectId}")
    public String updateProjectDto(@PathVariable("customerId") int customerId,@PathVariable("projectId") int projectId, @RequestBody ProjectDto Dto) {
        boolean check = projectService.updateProject(customerId, projectId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/projects/{projectId}")
    public String deleteProject(@PathVariable("customerId") int customerId,@PathVariable("projectId") int projectId) {
        projectService.deleteProjectById(customerId,projectId);
        return "Delete successful";
    }
}
