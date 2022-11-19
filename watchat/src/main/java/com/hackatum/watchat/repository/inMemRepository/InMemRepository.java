package com.hackatum.watchat.repository.inMemRepository;

import com.hackatum.watchat.entities.HasId;
import com.hackatum.watchat.repository.ReadWriteRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InMemRepository<T extends HasId<ID>, ID> implements ReadWriteRepository<T, ID> {
    private List<T> list = new ArrayList<>();


    @Override
    public T findById(ID id) {
        return list.stream().filter((entity) -> Objects.equals(entity.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public List<T> findAll() {
        return list;
    }

    @Override
    public Long count() {
        return (long)list.size();
    }

    @Override
    public T save(T entity) {
        if(list.contains(entity)) return entity;
        deleteById(entity.getId());
        list.add(entity);
        return entity;
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        List<T> newList = new LinkedList<>();
        entities.forEach(item -> {
            save(item);
            newList.add(item);
        });
        return newList;
    }

    @Override
    public void deleteById(ID id) {
        list.removeIf((item) -> Objects.equals(item.getId(), id));
    }

    @Override
    public void deleteAll() {
        list.clear();
    }
}
