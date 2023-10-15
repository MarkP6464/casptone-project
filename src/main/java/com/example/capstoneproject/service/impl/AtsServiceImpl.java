package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.entity.Ats;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.repository.AtsRepository;
import com.example.capstoneproject.service.AtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtsServiceImpl extends AbstractBaseService<Ats, AtsDto, Integer> implements AtsService {

    @Autowired
    AtsRepository atsRepository;

    @Autowired
    AtsMapper atsMapper;

    public AtsServiceImpl(AtsRepository atsRepository, AtsMapper atsMapper) {
        super(atsRepository, atsMapper, atsRepository::findById);
        this.atsRepository = atsRepository;
        this.atsMapper = atsMapper;
    }

    @Override
    public Ats createAts(Ats ats1) {
        Ats ats = new Ats();
        ats.setJobDescription(ats1.getJobDescription());
        ats.setAts(ats1.getAts());
        return atsRepository.save(ats);
    }

    @Override
    public boolean deleteAts(Integer atsId) {
        Optional<Ats> atsOptional = atsRepository.findById(atsId);
        if(atsOptional.isPresent()){
            Ats ats = atsOptional.get();
            atsRepository.delete(ats);
            return true;
        }
        return false;
    }
}
