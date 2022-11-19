package com.hackatum.watchat.repository;

import com.hackatum.watchat.entities.HasId;

import java.util.List;


public interface ReadWriteRepository<T extends HasId<ID>, ID> extends ReadRepository<T, ID> {
    T save(T entity);
    List<T> saveAll(Iterable<T> entities);
    void deleteById(ID id);
}
