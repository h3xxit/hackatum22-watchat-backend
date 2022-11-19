package com.hackatum.watchat.repository.jpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

interface JpaBusinessObjectRepositoryInterface extends JpaRepository<JpaMovie, Long> {
}

@Repository
public class JpaMovieRepository implements MovieRepository {
    @Autowired
    private JpaBusinessObjectRepositoryInterface jpaRepository;
    private ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    @Override
    public Movie findById(Long id) {
        return objectMapper.convertValue(jpaRepository.findById(id), Movie.class);
    }

    @Override
    public List<Movie> findAll() {
        return jpaRepository.findAll().stream().map(jpaMovie -> objectMapper.convertValue(jpaMovie, Movie.class)).toList();
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    @Override
    public Movie save(Movie entity) {
        return objectMapper.convertValue(jpaRepository.save(objectMapper.convertValue(entity, JpaMovie.class)), Movie.class);
    }

    @Override
    public List<Movie> saveAll(Iterable<Movie> entities) {
        List<Movie> result = new LinkedList<>();
        for (Movie entity: entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
