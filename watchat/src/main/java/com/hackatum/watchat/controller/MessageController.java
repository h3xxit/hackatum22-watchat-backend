package com.hackatum.watchat.controller;

import com.hackatum.watchat.entities.Message;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.UserInputResponseDto;
import com.hackatum.watchat.repository.MovieRepository;
import com.hackatum.watchat.service.ClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
        return classifierService.classify(msg.getText()).map(movieTags ->
        {
            for (MovieTag movieTag: movieTags) {
                if(msg.getPreferences() == null) continue;
                MovieTag pair = msg.getPreferences().stream().filter(pref -> Objects.equals(pref.getName(), movieTag.getName())).findFirst().orElse(null);
                if(pair == null) continue;
                movieTag.setMatch((movieTag.getMatch() + pair.getMatch()) / 2);
            }
            return new UserInputResponseDto(movieRepository.getBestMatch(movieTags), movieTags, "");
        });
    }
}
