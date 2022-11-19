package com.hackatum.watchat.controller;

import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/saveMovie")
    void saveMovie(@RequestBody Movie movie){
        movieRepository.save(movie);
    }

    @PostMapping("/saveMovies")
    void saveMovies(@RequestBody List<Movie> movie){
        movieRepository.saveAll(movie);
    }
}
