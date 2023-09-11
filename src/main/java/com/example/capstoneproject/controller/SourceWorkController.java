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
@RequestMapping("/api/v1/customer")
public class SourceWorkController {
    @Autowired
    SourceWorkService sourceWorkService;

    public SourceWorkController(SourceWorkService sourceWorkService) {
        this.sourceWorkService = sourceWorkService;
    }

    @GetMapping("/{customerId}/source-works")
    public List<SourceWorkViewDto> getAllSourceWork(@PathVariable("customerId") int customerId) {
        return sourceWorkService.getAllSourceWork(customerId);
    }

    @PostMapping("/{customerId}/source-works")
    public SourceWorkDto postSourceWork(@PathVariable("customerId") int customerId,@RequestBody SourceWorkDto Dto) {
        return sourceWorkService.createSourceWork(customerId,Dto);
    }

    @PutMapping("/{customerId}/source-works/{sourceId}")
    public String updateSourceWork(@PathVariable("customerId") int customerId,@PathVariable("sourceId") int sourceId, @RequestBody SourceWorkDto Dto) {
        boolean check = sourceWorkService.updateSourceWork(customerId,sourceId, Dto);
        if(check){
            return "Changes saved";
        }else{
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/source-works/{sourceId}")
    public String deleteSourceWork(@PathVariable("customerId") int customerId,@PathVariable("sourceId") int sourceId) {
        sourceWorkService.deleteSourceWorkById(customerId,sourceId);
        return "Delete successful";
    }
}
