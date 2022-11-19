package com.hackatum.watchat.repository;

import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.Tag;

import java.util.List;

public interface MovieRepository extends ReadWriteRepository<Movie, Long>{
    List<Movie> getBestMatch(List<MovieTag> tags);
    List<Movie> getBestMatch(List<MovieTag> tags, Long startId);
}
