package com.example.capstoneproject.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<D, ID> {

    D create(D dto);

    Optional<D> getById(ID id);

    List<D> getAll();

    D update(ID id, D dto);

    void deleteById(ID id);

}
