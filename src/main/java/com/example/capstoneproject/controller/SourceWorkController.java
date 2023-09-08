package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import com.example.capstoneproject.service.SkillService;
import com.example.capstoneproject.service.SourceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class SourceWorkController {
    @Autowired
    SourceWorkService sourceWorkService;

    public SourceWorkController(SourceWorkService sourceWorkService) {
        this.sourceWorkService = sourceWorkService;
    }

    @GetMapping("/{cvId}/source-works")
    public List<SourceWorkViewDto> getAllSourceWork(@PathVariable("cvId") int cvId) {
        return sourceWorkService.getAllSourceWork(cvId);
    }

    @PostMapping("/{cvId}/source-works")
    public SourceWorkDto postSourceWork(@PathVariable("cvId") int cvId,@RequestBody SourceWorkDto Dto) {
        return sourceWorkService.createSourceWork(cvId,Dto);
    }

    @PutMapping("/{cvId}/source-works/{sourceId}")
    public String updateSourceWork(@PathVariable("cvId") int cvId,@PathVariable("sourceId") int sourceId, @RequestBody SourceWorkDto Dto) {
        boolean check = sourceWorkService.updateSourceWork(cvId,sourceId, Dto);
        if(check){
            return "Changes saved";
        }else{
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/source-works/{sourceId}")
    public String deleteSourceWork(@PathVariable("cvId") int cvId,@PathVariable("sourceId") int sourceId) {
        sourceWorkService.deleteSourceWorkById(cvId,sourceId);
        return "Delete successful";
    }
}
