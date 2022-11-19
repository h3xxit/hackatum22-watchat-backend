package com.hackatum.watchat.repository.inMemRepository;

import com.hackatum.watchat.entities.Movie;
import org.springframework.stereotype.Repository;

@Repository
public class InMemMovieRepository extends InMemRepository<Movie, String>{
}
