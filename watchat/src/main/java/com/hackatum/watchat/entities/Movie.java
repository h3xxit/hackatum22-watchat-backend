package com.hackatum.watchat.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Movie {
    String name;
    List<Tag> tags;
}
