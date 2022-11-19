package com.hackatum.watchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInputResponseDto {
    private List<Movie> movies;
    private List<MovieTag> tagsFromText;
    private String question;
}
