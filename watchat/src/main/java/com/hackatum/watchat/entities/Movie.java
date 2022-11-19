package com.hackatum.watchat.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Movie implements HasId<Long>{
    private Long id;
    private String name;
    private String description;
    private String image;
    private String redirect;
    private List<MovieTag> tags;

    @Override
    public Long getId(){
        return id;
    }
}
