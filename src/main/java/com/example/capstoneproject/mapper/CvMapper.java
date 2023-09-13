package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.entity.Cv;
import org.springframework.stereotype.Component;

@Component
public class CvMapper extends AbstractMapper<Cv, CvDto> {
    public CvMapper() {
        super(Cv.class, CvDto.class);
    }
}
