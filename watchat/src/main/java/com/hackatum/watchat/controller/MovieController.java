package com.hackatum.watchat.controller;

import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/saveMovie")
    Movie saveMovie(@RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    @PostMapping("/saveMovies")
    List<Movie> saveMovies(@RequestBody List<Movie> movie){
        return movieRepository.saveAll(movie);
    }

    @PostMapping("/movie")
    Movie getMovie(@RequestParam("id") Long movieId){
        return movieRepository.findById(movieId);
    }
}
