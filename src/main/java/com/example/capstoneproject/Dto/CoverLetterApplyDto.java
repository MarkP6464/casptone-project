package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.CoverLetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@NoArgsConstructor
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonType.class)
public class CoverLetterApplyDto {
    @Type(type = "json")
    private String coverLetterApply;

    public String toCoverLetterBody(CoverLetterDto dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String map = objectMapper.writeValueAsString(dto);
        this.setCoverLetterApply(map);
        return map;
    }
    public CoverLetterDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.coverLetterApply, CoverLetterDto.class);
    }

    public void setCoverLetter(CoverLetter coverLetter) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = coverLetter.toJson();
        this.coverLetterApply = json;
    }
}
