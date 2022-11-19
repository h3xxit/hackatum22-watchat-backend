package com.hackatum.watchat.repository.inMemRepository;

import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.stereotype.Repository;

//@Repository
public class InMemMovieRepository extends InMemRepository<Movie, Long> implements MovieRepository {
}
