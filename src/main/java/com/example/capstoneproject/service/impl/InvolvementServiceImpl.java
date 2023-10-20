package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.InvolvementViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.mapper.SectionMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvolvementServiceImpl extends AbstractBaseService<Involvement, InvolvementDto, Integer> implements InvolvementService {
    @Autowired
    InvolvementRepository involvementRepository;

    @Autowired
    InvolvementMapper involvementMapper;

    @Autowired
    CvService cvService;

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionLogRepository sectionLogRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionMapper sectionMapper;

    @Autowired
    SectionLogService sectionLogService;

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    EvaluateRepository evaluateRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersService usersService;

    @Autowired
    CvRepository cvRepository;

    public InvolvementServiceImpl(InvolvementRepository involvementRepository, InvolvementMapper involvementMapper) {
        super(involvementRepository, involvementMapper, involvementRepository::findById);
        this.involvementRepository = involvementRepository;
        this.involvementMapper = involvementMapper;
    }

    @Override
    public InvolvementDto createInvolvement(Integer id, InvolvementDto dto) {
        Involvement involvement = involvementMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
        involvement.setUser(Users);
        involvement.setStatus(BasicStatus.ACTIVE);
        Involvement saved = involvementRepository.save(involvement);
        return involvementMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateInvolvement(int UsersId, int involvementId, InvolvementDto dto) {
        Optional<Involvement> existingInvolvementOptional = involvementRepository.findById(involvementId);
        if (existingInvolvementOptional.isPresent()) {
            Involvement existingInvolvement = existingInvolvementOptional.get();
            if (existingInvolvement.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Involvement does not belong to Users with id " + UsersId);
            }
            if (dto.getOrganizationRole() != null && !existingInvolvement.getOrganizationRole().equals(dto.getOrganizationRole())) {
                existingInvolvement.setOrganizationRole(dto.getOrganizationRole());
            } else {
                existingInvolvement.setOrganizationRole(existingInvolvement.getOrganizationRole());
            }
            if (dto.getOrganizationName() != null && !existingInvolvement.getOrganizationName().equals(dto.getOrganizationName())) {
                existingInvolvement.setOrganizationName(dto.getOrganizationName());
            } else {
                existingInvolvement.setOrganizationName(existingInvolvement.getOrganizationName());
            }
            if (dto.getStartDate() != null && !existingInvolvement.getStartDate().equals(dto.getStartDate())) {
                existingInvolvement.setStartDate(dto.getStartDate());
            } else {
                existingInvolvement.setStartDate(existingInvolvement.getStartDate());
            }
            if (dto.getEndDate() != null && !existingInvolvement.getEndDate().equals(dto.getEndDate())) {
                existingInvolvement.setEndDate(dto.getEndDate());
            } else {
                existingInvolvement.setEndDate(existingInvolvement.getEndDate());
            }
            if (dto.getCollege() != null && !existingInvolvement.getCollege().equals(dto.getCollege())) {
                existingInvolvement.setCollege(dto.getCollege());
            } else {
                existingInvolvement.setCollege(existingInvolvement.getCollege());
            }
            if (dto.getDescription() != null && !existingInvolvement.getDescription().equals(dto.getDescription())) {
                existingInvolvement.setDescription(dto.getDescription());
            } else {
                existingInvolvement.setDescription(existingInvolvement.getDescription());
            }

            existingInvolvement.setStatus(BasicStatus.ACTIVE);
            Involvement updated = involvementRepository.save(existingInvolvement);
            return true;
        } else {
            throw new IllegalArgumentException("Involvement ID not found");
        }
    }

    @Override
    public List<InvolvementDto> getAllInvolvement(int UsersId) {
        List<Involvement> involvements = involvementRepository.findInvolvementsByStatus(UsersId, BasicStatus.ACTIVE);
        return involvements.stream()
                .filter(involvement -> involvement.getStatus() == BasicStatus.ACTIVE)
                .map(involvement -> {
                    InvolvementDto involvementDto = new InvolvementDto();
                    involvementDto.setId(involvement.getId());
                    involvementDto.setOrganizationRole(involvement.getOrganizationRole());
                    involvementDto.setOrganizationName(involvement.getOrganizationName());
                    involvementDto.setStartDate(involvement.getStartDate());
                    involvementDto.setEndDate(involvement.getEndDate());
                    involvementDto.setCollege(involvement.getCollege());
                    involvementDto.setDescription(involvement.getDescription());
                    return involvementDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvolvementById(Integer UsersId, Integer involvementId) {
        boolean isInvolvementBelongsToCv = involvementRepository.existsByIdAndUser_Id(involvementId, UsersId);

        if (isInvolvementBelongsToCv) {
            Optional<Involvement> Optional = involvementRepository.findById(involvementId);
            if (Optional.isPresent()) {
                Involvement involvement = Optional.get();
                involvement.setStatus(BasicStatus.DELETED);
                involvementRepository.save(involvement);
            }
        } else {
            throw new IllegalArgumentException("Project with ID " + involvementId + " does not belong to Users with ID " + UsersId);
        }
    }


    @Override
    public InvolvementViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Involvement involvement = involvementRepository.getById(id);
        if (Objects.nonNull(involvement)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<InvolvementDto> dto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
            List<BulletPointDto> bulletPointDtos = sectionRepository.findBulletPointDtoByTypeIdAndTypeName(id, SectionEvaluate.involvement);
            if (dto.isPresent()) {
                InvolvementDto involvementDto = dto.get();
                InvolvementViewDto involvementViewDto = new InvolvementViewDto();
                involvementViewDto.setId(involvement.getId());
                involvementViewDto.setIsDisplay(involvementDto.getIsDisplay());
                involvementViewDto.setOrganizationRole(involvement.getOrganizationRole());
                involvementViewDto.setOrganizationName(involvement.getOrganizationName());
                involvementViewDto.setStartDate(involvement.getStartDate());
                involvementViewDto.setEndDate(involvement.getEndDate());
                involvementViewDto.setCollege(involvement.getCollege());
                involvementViewDto.setDescription(involvement.getDescription());
                involvementViewDto.setBulletPointDtos(bulletPointDtos);
                return involvementViewDto;
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public InvolvementDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<InvolvementDto> dto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<InvolvementViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<InvolvementViewDto> set = new ArrayList<>();
        cvBodyDto.getInvolvements().stream().forEach(
                e -> {
                    try {
                        set.add(getAndIsDisplay(cvId, e.getId()));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        return set;
    }

    @Override
    public InvolvementViewDto updateInCvBody(int cvId, int id, InvolvementDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<InvolvementDto> relationDto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Involvement involvement = involvementRepository.getById(id);
            modelMapper.map(dto, involvement);
            Involvement saved = involvementRepository.save(involvement);
            InvolvementDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);

            //Delete section_log in db
            Section section = sectionRepository.findByTypeNameAndTypeId(SectionEvaluate.involvement, involvement.getId());
            sectionLogRepository.deleteBySection_Id(section.getId());
            //Get process evaluate
            List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
            InvolvementViewDto involvementViewDto = new InvolvementViewDto();
            involvementViewDto.setId(saved.getId());
            involvementViewDto.setIsDisplay(dto.getIsDisplay());
            involvementViewDto.setOrganizationRole(saved.getOrganizationRole());
            involvementViewDto.setOrganizationName(saved.getOrganizationName());
            involvementViewDto.setStartDate(saved.getStartDate());
            involvementViewDto.setEndDate(saved.getEndDate());
            involvementViewDto.setCollege(saved.getCollege());
            involvementViewDto.setDescription(saved.getDescription());
            involvementViewDto.setBulletPointDtos(evaluateResult);

            //Save evaluateLog into db
            List<Evaluate> evaluates = evaluateRepository.findAll();

            int evaluateId = 1;
            for (int i = 0; i < evaluates.size(); i++) {
                Evaluate evaluate = evaluates.get(i);
                BulletPointDto bulletPointDto = evaluateResult.get(i);
                SectionLogDto sectionLogDto1 = new SectionLogDto();
                sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(sectionMapper.mapEntityToDto(section)));
                sectionLogDto1.setEvaluate(evaluate);
                sectionLogDto1.setBullet(bulletPointDto.getResult());
                sectionLogDto1.setStatus(bulletPointDto.getStatus());
                sectionLogService.create(sectionLogDto1);
                evaluateId++;
                if (evaluateId == 7) {
                    break;
                }
            }
            return involvementViewDto;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


@Override
public InvolvementViewDto createOfUserInCvBody(int cvId, InvolvementDto dto) throws JsonProcessingException {
        Involvement education = involvementMapper.mapDtoToEntity(dto);
        Users user = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(user);
        education.setStatus(BasicStatus.ACTIVE);
        Involvement saved = involvementRepository.save(education);
        InvolvementDto involvementDto = new InvolvementDto();
        involvementDto.setId(saved.getId());
        List<Cv> list = cvRepository.findAllByUsersIdAndStatus(user.getId(), BasicStatus.ACTIVE);
        list.stream().forEach(x -> {
        if (x.getId().equals(cvId)) {
        involvementDto.setIsDisplay(true);
        } else {
        involvementDto.setIsDisplay(false);
        }
        try {
        CvBodyDto cvBodyDto = x.deserialize();
        cvBodyDto.getInvolvements().add(involvementDto);
        cvService.updateCvBody(x.getId(), cvBodyDto);
        } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
        }
        });
        //Save evaluate db
        SectionDto sectionDto = new SectionDto();
        List<Involvement> projects = involvementRepository.findExperiencesByStatusOrderedByStartDateDesc(user.getId(), BasicStatus.ACTIVE);
        if (!projects.isEmpty()) {
        sectionDto.setTypeId(projects.get(0).getId());
        }
        sectionDto.setTitle(saved.getOrganizationRole());
        sectionDto.setTypeName(SectionEvaluate.involvement);
        SectionDto section = sectionService.create(sectionDto);

        //Get process evaluate
        List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
        InvolvementViewDto involvementViewDto = new InvolvementViewDto();
        involvementViewDto.setId(saved.getId());
        involvementViewDto.setIsDisplay(true);
        involvementViewDto.setOrganizationRole(saved.getOrganizationRole());
        involvementViewDto.setOrganizationName(saved.getOrganizationName());
        involvementViewDto.setStartDate(saved.getStartDate());
        involvementViewDto.setEndDate(saved.getEndDate());
        involvementViewDto.setCollege(saved.getCollege());
        involvementViewDto.setDescription(saved.getDescription());
        involvementViewDto.setBulletPointDtos(evaluateResult);

        //Save evaluateLog into db
        List<Evaluate> evaluates = evaluateRepository.findAll();
        int evaluateId = 1;
        for (int i = 0; i < evaluates.size(); i++) {
        Evaluate evaluate = evaluates.get(i);
        BulletPointDto bulletPointDto = evaluateResult.get(i);
        SectionLogDto sectionLogDto1 = new SectionLogDto();
        sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(section));
        sectionLogDto1.setEvaluate(evaluate);
        sectionLogDto1.setBullet(bulletPointDto.getResult());
        sectionLogDto1.setStatus(bulletPointDto.getStatus());
        sectionLogService.create(sectionLogDto1);
        evaluateId++;
        if (evaluateId == 7) {
        break;
        }
        }
        return involvementViewDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {

        Optional<Involvement> Optional = involvementRepository.findById(id);
        if (Optional.isPresent()) {
            Involvement education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            involvementRepository.delete(education);
            List<Cv> list = cvRepository.findAllByUsersIdAndStatus(education.getUser().getId(), BasicStatus.ACTIVE);
            list.stream().forEach(x -> {
                try {
                    CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                    cvBodyDto.getInvolvements().removeIf(e -> e.getId() == id);
                    cvService.updateCvBody(x.getId(), cvBodyDto);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


}
