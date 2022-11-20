package com.hackatum.watchat.repository.inMemRepository;

import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public class InMemMovieRepository extends InMemRepository<Movie, Long> implements MovieRepository {
    @Override
    public List<Movie> getBestMatch(List<MovieTag> tags) {
        return null;
    }

    @Override
    public List<Movie> getBestMatch(List<MovieTag> tags, Long startId) {
        return null;
    }
}
