package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.responses.SourceWorkViewDto;
import com.example.capstoneproject.service.SourceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class SourceWorkController {
    @Autowired
    SourceWorkService sourceWorkService;

    public SourceWorkController(SourceWorkService sourceWorkService) {
        this.sourceWorkService = sourceWorkService;
    }

    @GetMapping("/{UsersId}/source-works")
    public List<SourceWorkViewDto> getAllSourceWork(@PathVariable("UsersId") int UsersId) {
        return sourceWorkService.getAllSourceWork(UsersId);
    }

    @PostMapping("/{UsersId}/source-works")
    public SourceWorkDto postSourceWork(@PathVariable("UsersId") int UsersId,@RequestBody SourceWorkDto Dto) {
        return sourceWorkService.createSourceWork(UsersId,Dto);
    }

    @PutMapping("/{UsersId}/source-works/{sourceId}")
    public String updateSourceWork(@PathVariable("UsersId") int UsersId,@PathVariable("sourceId") int sourceId, @RequestBody SourceWorkDto Dto) {
        boolean check = sourceWorkService.updateSourceWork(UsersId,sourceId, Dto);
        if(check){
            return "Changes saved";
        }else{
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/source-works/{sourceId}")
    public String deleteSourceWork(@PathVariable("UsersId") int UsersId,@PathVariable("sourceId") int sourceId) {
        sourceWorkService.deleteSourceWorkById(UsersId,sourceId);
        return "Delete successful";
    }
}
