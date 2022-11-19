package com.hackatum.watchat.repository.inMemRepository;

import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.TagRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemTagRepository extends InMemRepository<Tag, String> implements TagRepository {
}
