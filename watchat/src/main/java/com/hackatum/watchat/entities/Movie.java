package com.hackatum.watchat.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Movie implements HasId<String>{
    String name;
    List<Tag> tags;

    @Override
    public String getId(){
        return name;
    }
}
