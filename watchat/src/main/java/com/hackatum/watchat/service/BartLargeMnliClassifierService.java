package com.hackatum.watchat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    //final private String huggingface_headers = {"Authorization": f"Bearer {HUGGINGFACE_API_TOKEN}"}
    List<String> tags = List.of("funny", "sad");

    @Override
    public Mono<List<Tag>> classify(String text) {
        WebClient client = WebClient.create(HUGGINGFACE_API_URI);
        String body = "";
        try
        {
            ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
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
                        List<Tag> result = new ArrayList<>();
                        for(int i = 0; i<responseBody.scores.size(); i++){
                            result.add(new Tag(null, responseBody.labels.get(i), responseBody.scores.get(i)));
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