package com.hackatum.watchat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SimilarityMessage {
    Long movieId;
    List<MovieTag> preferences;
}
