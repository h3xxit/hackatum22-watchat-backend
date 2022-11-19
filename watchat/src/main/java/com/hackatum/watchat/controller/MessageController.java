package com.hackatum.watchat.controller;

import com.hackatum.watchat.entities.Message;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.service.ClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    ClassifierService classifierService;

    @PostMapping("/message")
    Mono<List<Tag>> receiveMessage(@RequestBody Message msg){
        return classifierService.classify(msg.getText());
    }
}
