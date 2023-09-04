package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.service.SkillService;
import com.example.capstoneproject.service.SourceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/source-works")
public class SourceWorkController {
    @Autowired
    SourceWorkService sourceWorkService;

    public SourceWorkController(SourceWorkService sourceWorkService) {
        this.sourceWorkService = sourceWorkService;
    }

    @GetMapping
    public List<SourceWorkDto> getAllSourceWork() {
        return sourceWorkService.getAll();
    }

    @PostMapping
    public SourceWorkDto postSourceWork(@RequestBody SourceWorkDto Dto) {
        return sourceWorkService.create(Dto);
    }

    @PutMapping("/{sourceId}")
    public SourceWorkDto updateSourceWork(@PathVariable("sourceId") int sourceId, @RequestBody SourceWorkDto Dto) {
        return sourceWorkService.update(sourceId, Dto);
    }

    @DeleteMapping("/{sourceId}")
    public String deleteSourceWork(@PathVariable("sourceId") int sourceId) {
        sourceWorkService.deleteById(sourceId);
        return "Delete successful";
    }
}
