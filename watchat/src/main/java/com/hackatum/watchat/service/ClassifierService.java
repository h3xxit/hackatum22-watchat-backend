package com.hackatum.watchat.service;

import com.hackatum.watchat.entities.Tag;

import java.util.List;

public interface ClassifierService {
    List<Tag> classify(String text);
}
