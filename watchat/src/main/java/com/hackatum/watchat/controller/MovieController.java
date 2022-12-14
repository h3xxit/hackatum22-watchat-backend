package com.hackatum.watchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.JpaMovieDto;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.repository.MovieRepository;
import com.hackatum.watchat.repository.jpaRepository.JpaMovie;
import com.hackatum.watchat.repository.jpaRepository.JpaMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/saveMovie")
    private Movie saveMovie(@RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveMovies")
    private List<Movie> saveMovies(@RequestBody List<Movie> movie){
        return movieRepository.saveAll(movie);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/saveJpaMovies")
    private void saveJpaMovies(@RequestBody List<JpaMovieDto> movies){
        JpaMovieRepository mRep = ((JpaMovieRepository)movieRepository);
        mRep.saveAllJpa(movies);
    }

    @PostMapping("/movie")
    Movie getMovie(@RequestParam("id") Long movieId){
        return movieRepository.findById(movieId);
    }

    @DeleteMapping("/deleteAll")
    private void deleteAllMovies(){
        movieRepository.deleteAll();
    }

    @DeleteMapping("/delete")
    private void deleteMovie(@RequestParam Long id){
        movieRepository.deleteById(id);
    }
}
