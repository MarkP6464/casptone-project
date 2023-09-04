package com.example.capstoneproject;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFullTypeMatchingRequired(true);
        return modelMapper;
    }

}
