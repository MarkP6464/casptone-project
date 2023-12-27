package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{UsersId}/projects")
    public List<ProjectViewDto> getAllProject(@PathVariable("UsersId") int UsersId) {
        return projectService.getAllProject(UsersId);
    }

    @PostMapping("/cv/{cv-id}/projects")
    public ProjectDto postProject(@PathVariable("cv-id") int cvId,@RequestBody ProjectDto Dto) {
        return projectService.createProject(cvId,Dto);
    }

    @PutMapping("/{UsersId}/projects/{projectId}")
    public String updateProjectDto(@PathVariable("UsersId") int UsersId,@PathVariable("projectId") int projectId, @RequestBody ProjectDto Dto) {
        boolean check = projectService.updateProject(UsersId, projectId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/projects/{projectId}")
    public String deleteProject(@PathVariable("UsersId") int UsersId,@PathVariable("projectId") int projectId) {
        projectService.deleteProjectById(UsersId,projectId);
        return "Delete successful";
    }
}
