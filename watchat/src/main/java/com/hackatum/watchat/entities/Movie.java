package com.hackatum.watchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
