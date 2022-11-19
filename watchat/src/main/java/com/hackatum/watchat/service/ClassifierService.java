package com.hackatum.watchat.service;

import com.hackatum.watchat.entities.Tag;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClassifierService {
    Mono<List<Tag>> classify(String text);
}
