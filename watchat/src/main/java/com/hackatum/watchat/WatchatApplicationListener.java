package com.hackatum.watchat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Component
public class WatchatApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private TagRepository tagRepository;

    List<String> tags = List.of(
            "superhero",
            "sport",
            "criminal",
            "happy",
            "sad",
            "horror",
            "love",
            "funny",
            "space",
            "fantasy"
        );

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Loaded " + loadTags() + " tags on startup");
    }

    private int loadTags() {
        tagRepository.saveAll(tags.stream().map(Tag::new).toList());
        return tags.size();

        /*ObjectMapper mapper = new Jackson2ObjectMapperBuilder().build();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        //var resource = new ClassPathResource("Tags.json");
        //System.out.println(this.getClass().getClassLoader().getResource("Tags.json").getPath());
        //if (!resource.exists()) return 0;
        try {
            //System.out.println(resource.getFile().toPath());
            //String json = new String(Files.readAllBytes(resource.getFile().toPath()));
            System.out.println(this.getClass().getClassLoader().getResource("Tags.json"));
            FileInputStream stream = new FileInputStream(this.getClass().getClassLoader().getResource("Tags.json").getPath());
            Scanner s = new Scanner(stream).useDelimiter("\\A");
            String json = s.hasNext() ? s.next() : "";
            var reader = mapper.readerForListOf(String.class);
            List<String> tags = reader.readValue(json);
            tagRepository.saveAll(tags.stream().map(Tag::new).toList());
            return tags.size();
        } catch (Exception ex){
            return 0;
        }*/
    }
}
