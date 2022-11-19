package com.hackatum.watchat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BartLargeMnliClassifierService implements ClassifierService{
    final private String HUGGINGFACE_API_URI = "https://api-inference.huggingface.co";
    final private String HUGGINGFACE_API_URL = "/models/facebook/bart-large-mnli";
    final private String HUGGINGFACE_API_TOKEN = "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";

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Mono<List<MovieTag>> classify(String text) {
        WebClient client = WebClient.create(HUGGINGFACE_API_URI);
        String body = "";
        try
        {
            ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
            List<String> tags = tagRepository.findAll().stream().map(Tag::getName).toList();
            body = objectMapper.writeValueAsString(new HuggingfaceApiRequestBody(Map.of("candidate_labels", tags), text));
        } catch (JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not parse tag list for external api request");
        }
        try
        {
            //responseBody =
            return client
                .post()
                .uri(HUGGINGFACE_API_URL)
                .bodyValue(body)
                .header("Authorization", "Bearer " + HUGGINGFACE_API_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(HuggingfaceApiResponseBody.class)
                    .map(responseBody -> {
                        List<MovieTag> result = new ArrayList<>();
                        for(int i = 0; i<responseBody.scores.size(); i++){
                            result.add(new MovieTag(responseBody.labels.get(i), responseBody.scores.get(i)));
                        }
                        return result;
                    });
                //.toProcessor().block();
                //.share().block();
                //.toEntity(HuggingfaceApiResponseBody.class)
                //.block();
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "External api call failed");
        }
    }
}

@AllArgsConstructor
class HuggingfaceApiRequestBody {
    public Map<String, List<String>> parameters;
    public String inputs;
}

@NoArgsConstructor
@AllArgsConstructor
class HuggingfaceApiResponseBody {
    public String sequence;
    public List<String> labels;
    public List<Double> scores;
}