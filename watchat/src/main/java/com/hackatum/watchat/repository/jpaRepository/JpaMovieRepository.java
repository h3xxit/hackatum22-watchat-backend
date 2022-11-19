package com.hackatum.watchat.repository.jpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.entities.Tag;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

interface JpaMovieRepositoryInterface extends JpaRepository<JpaMovie, Long> {
    @Query(value = "SELECT * FROM Movie m ORDER BY m.name asc limit 4", nativeQuery = true)
    List<JpaMovie> get4Alphabetic();

    @Query(value = "SELECT * FROM Tag t WHERE t.tmdb_id = ?1 ORDER BY t.name asc", nativeQuery = true)
    List<JpaMovieTag> getTagsFromMovie(Long id);
}

@Repository
public class JpaMovieRepository implements MovieRepository {
    @Autowired
    private JpaMovieRepositoryInterface jpaRepository;
    private final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

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

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public List<Movie> getBestMatch(List<MovieTag> tags) {
        return jpaRepository.get4Alphabetic().stream().map(jpaMovie -> objectMapper.convertValue(jpaMovie, Movie.class)).toList();
    }
}
