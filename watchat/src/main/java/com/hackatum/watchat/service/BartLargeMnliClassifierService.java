package com.hackatum.watchat.service;

import com.hackatum.watchat.entities.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BartLargeMnliClassifierService implements ClassifierService{
    @Override
    public List<Tag> classify(String text) {
        //TODO
        return null;
    }
}
