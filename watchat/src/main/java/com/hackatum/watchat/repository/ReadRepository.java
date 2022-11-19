package com.hackatum.watchat.repository;

import com.hackatum.watchat.entities.HasId;

import java.util.List;

public interface ReadRepository<T extends HasId<ID>, ID> {
    T findById(ID id);
    List<T> findAll();
    Long count();
}
