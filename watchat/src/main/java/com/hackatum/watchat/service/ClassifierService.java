package com.hackatum.watchat.service;

import com.hackatum.watchat.entities.MovieTag;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ClassifierService {
    Mono<List<MovieTag>> classify(String text);
}
