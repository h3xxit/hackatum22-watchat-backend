package com.hackatum.watchat.controller;

import com.hackatum.watchat.entities.*;
import com.hackatum.watchat.repository.MovieRepository;
import com.hackatum.watchat.service.ClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RestController
public class MessageController {
    @Autowired
    ClassifierService classifierService;
    @Autowired
    MovieRepository movieRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/message")
    Mono<UserInputResponseDto> receiveMessage(@RequestBody Message msg){
        if(msg.getText() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Text field should not be empty");
        }
        return classifierService.classify(msg.getText()).map(movieTags ->
        {
            calculateWeights(movieTags, msg.getPreferences());
            return new UserInputResponseDto(movieRepository.getBestMatch(movieTags), movieTags, "Test question");
        });
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/similar")
    UserInputResponseDto findSimilar(@RequestBody SimilarityMessage msg){
        Movie movie = movieRepository.findById(msg.getMovieId());
        List<MovieTag> movieTags = List.copyOf(movie.getTags());
        calculateWeights(movieTags, msg.getPreferences());
        return new UserInputResponseDto(movieRepository.getBestMatch(movieTags, movie.getId()), movieTags, "");
    }

    void calculateWeights(List<MovieTag> newTags, List<MovieTag> preferences){
        for (MovieTag movieTag: newTags) {
            if(preferences == null) continue;
            MovieTag pair = preferences.stream().filter(pref -> Objects.equals(pref.getName(), movieTag.getName())).findFirst().orElse(null);
            if(pair == null) continue;
            movieTag.setMatch(movieTag.getMatch() * 0.7 + pair.getMatch() * 0.3);
        }
    }
}
