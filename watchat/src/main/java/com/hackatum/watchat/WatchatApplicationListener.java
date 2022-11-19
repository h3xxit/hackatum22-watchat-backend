package com.hackatum.watchat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class WatchatApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Loaded " + loadTags() + " tags on startup");
    }

    private int loadTags() {
        ObjectMapper mapper = new Jackson2ObjectMapperBuilder().build();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        var resource = new ClassPathResource("Tags.json");
        if (!resource.exists()) return 0;
        try {
            String json = new String(Files.readAllBytes(resource.getFile().toPath()));
            var reader = mapper.readerForListOf(String.class);
            List<String> tags = reader.readValue(json);
            tagRepository.saveAll(tags.stream().map(Tag::new).toList());
            return tags.size();
        } catch (Exception ex){
            return 0;
        }
    }
}
