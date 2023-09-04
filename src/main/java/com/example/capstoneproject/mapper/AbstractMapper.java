package com.example.capstoneproject.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractMapper<E, D> {
    @Autowired
    ModelMapper modelMapper;
    Class<E> entityType;
    Class<D> dtoType;

    @Autowired
    public AbstractMapper(Class<E> entityType, Class<D> dtoType) {
        this.entityType = entityType;
        this.dtoType = dtoType;
    }

    public E mapDtoToEntity(D dto) {
        return modelMapper.map(dto, entityType);
    }

    public void mapDtoToEntity(D dto, E entity) {
        modelMapper.map(dto, entity);
    }

    public D mapEntityToDto(E entity) {
        return modelMapper.map(entity, dtoType);
    }

    public Optional<D> mapOptEntityToOptDto(Optional<E> optionalEntity) {
        if (optionalEntity.isPresent())
            return Optional.empty();

        return Optional.of(modelMapper.map(optionalEntity.get(), dtoType));
    }

    public void mapEntityToDto(E entity, D dto) {
        modelMapper.map(entity, dto);
    }

    public List<D> mapEntitiesToDtoes(List<E> entities) {
        if (entities == null)
            return Collections.emptyList();

        return entities.stream().map(entity -> modelMapper.map(entity, dtoType)).collect(Collectors.toList());
    }

    public List<E> mapDtoesToEntities(List<D> dtoes) {
        if (dtoes == null)
            return Collections.emptyList();

        return dtoes.stream().map(entity -> modelMapper.map(entity, entityType)).collect(Collectors.toList());
    }

    public void mapEntityToEntity(E src, E des) {
        modelMapper.map(src, des);
    }

    public List<D> mapEntitiesToDtos(List<E> entities) {
        if (entities == null)
            return Collections.emptyList();

        return entities.stream().map(entity -> mapEntityToDto(entity)).collect(Collectors.toList());
    }



}