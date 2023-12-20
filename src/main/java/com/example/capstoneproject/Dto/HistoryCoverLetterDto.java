package com.example.capstoneproject.Dto;

import com.example.capstoneproject.Dto.responses.UsersCvViewDto;
import com.example.capstoneproject.entity.CoverLetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryCoverLetterDto {
    private Integer id;
    private String title;
    private String dear;
    private LocalDate date;
    private String company;
    private String description;
    private UsersCvViewDto contact;
}
