package com.example.capstoneproject.entity;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("Admin")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Admin extends Users{

    @Type(type = "json")
    @Column(columnDefinition = "json")
    AdminConfigurationResponse configuration;

//    public AdminConfigurationResponse deserialize() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(this.configuration, AdminConfigurationResponse.class);
//    }

}
