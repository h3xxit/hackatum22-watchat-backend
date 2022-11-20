package com.hackatum.watchat.entities;

import com.hackatum.watchat.repository.jpaRepository.JpaMovie;
import com.hackatum.watchat.repository.jpaRepository.JpaMovieTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JpaMovieDto {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String redirect;
    private List<JpaMovieTag> tags;
    private List<Long> neighbours;
}
