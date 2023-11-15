package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper extends AbstractMapper<Cv, CvDto> {
    public TransactionMapper() {
        super(Cv.class, CvDto.class);
    }

    public TransactionDto toDto(Transaction entity) {
        return modelMapper.map(entity, TransactionDto.class);
    }
}
