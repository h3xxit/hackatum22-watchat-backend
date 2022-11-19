package com.hackatum.watchat.repository.jpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.JpaMovieDto;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import com.hackatum.watchat.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

interface JpaMovieRepositoryInterface extends JpaRepository<JpaMovie, Long> {
    @Query(value = "SELECT m.tmdb_id FROM Movie m ORDER BY Random() limit 1", nativeQuery = true)
    Long getRandom();

    @Query(value = "SELECT * FROM Tag t WHERE t.tmdb_id = ?1 ORDER BY t.name asc", nativeQuery = true)
    List<JpaMovieTag> getTagsFromMovie(Long id);
}

@Repository
public class JpaMovieRepository implements MovieRepository {

    private final static HashMap<String, Double> WEIGHTS = new HashMap<>(){{
        put("superhero", 0.8);
        put("sport", 0.8);
        put("criminal", 0.7);
        put("happy", 1.4);
        put("sad", 1.5);
        put("horror", 1.0);
        put("love", 1.3);
        put("funny", 0.9);
        put("space", 0.7);
        put("fantasy", 0.9);
        put("popularity", 0.01);
    }};
    @Autowired
    private JpaMovieRepositoryInterface jpaRepository;
    private final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    @Transactional
    @Override
    public Movie findById(Long id) {
        return jpaRepository.findById(id).map(jpaMovie -> jpaMovie.toMovie(objectMapper)).orElse(null);
    }

    @Transactional
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

    @Transactional
    public JpaMovie findJpaById(Long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    public void saveAllJpa(List<JpaMovieDto> entities) {
        for (JpaMovieDto jpaMovieDto : entities) {
            jpaRepository.save(new JpaMovie(jpaMovieDto.getId(), jpaMovieDto.getName(), jpaMovieDto.getDescription(), jpaMovieDto.getImage(), jpaMovieDto.getRedirect(), jpaMovieDto.getTags(), List.of()));
        }
        for (JpaMovieDto jpaMovieDto : entities) {
            var neigbours = jpaMovieDto.getNeighbours().stream().map(this::findJpaById).filter(movie -> {
                System.out.println("Neighbour not found");
                return movie != null;
            }).toList();
            JpaMovie jpaMovie = findJpaById(jpaMovieDto.getId());
            jpaMovie.setNeighbours(neigbours);
            jpaRepository.save(jpaMovie);
        }
    }

    @Override
    public List<Movie> saveAll(Iterable<Movie> entities) {
        List<Movie> result = new LinkedList<>();
        for (Movie entity : entities) {
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

    @Transactional
    @Override
    public List<Movie> getBestMatch(List<MovieTag> tags) {
        return getBestMatch(tags, jpaRepository.getRandom());
    }

    @Transactional
    @Override
    public List<Movie> getBestMatch(List<MovieTag> tags, Long startId) {
        SortedSet<MovieWrapper> candidates = new TreeSet<>();
        Long firstId = startId;
        MovieWrapper best;
        Movie currentMovie;
        do {
            JpaMovie currentJpaMovie = jpaRepository.findById(firstId).orElse(null);
            if (currentJpaMovie == null) {
                throw new RuntimeException("jpaRepository return invalid random id");
            }
            currentMovie = findById(firstId);
            List<JpaMovie> neighbours = currentJpaMovie.getNeighbours();
            List<MovieTag> currentTags = currentMovie.getTags();
            double distance = distance(tags, currentTags);
            best = new MovieWrapper(currentMovie, distance);
            candidates.add(best);
            for (JpaMovie neighbour : neighbours) {
                Movie n = neighbour.toMovie(objectMapper);
                double d = distance(tags, n.getTags());
                MovieWrapper nWrapper = new MovieWrapper(n, d);
                candidates.add(nWrapper);
                if (nWrapper.compareTo(best) < 0) {
                    best = nWrapper;
                }
            }
            firstId = best.getMovie().getId();
        } while (best.getMovie().getId() != currentMovie.getId());
        List<Movie> result = new ArrayList<>(6);
        int i=0;
        while (i<4 && !candidates.isEmpty()) {
            MovieWrapper first = candidates.first();
            candidates.remove(first);
            result.add(first.getMovie());
            ++i;
        }
        return result;
    }

    private static double distance(List<MovieTag> tags1, List<MovieTag> tags2) {
        double res = 0;
        HashMap<String, Double> htag1 = new HashMap<String, Double>();
        HashMap<String, Double> htag2 = new HashMap<String, Double>();

        for (MovieTag tag : tags1) {
            htag1.put(tag.getName(), tag.getMatch());
        }

        for (MovieTag tag : tags2) {
            htag2.put(tag.getName(), tag.getMatch());
        }

        for (Map.Entry<String, Double> tag : htag1.entrySet()) {
            if(htag2.containsKey(tag.getKey())) {
                double diff = Math.abs(tag.getValue() - htag2.get(tag.getKey()));
                if (diff > 0.1) {
                    diff *= diff;
                    diff *= JpaMovieRepository.WEIGHTS.get(tag.getKey());
                    res += diff;
                }
            }else {
                System.out.println("Tag: " + tag.getKey() + " is missing!");
            }
        }
        return res;
    }

    private class MovieWrapper implements Comparable<MovieWrapper>{
        private final Movie movie;
        private final double distance;
        MovieWrapper(Movie movie, double distance) {
            this.movie = movie;
            this.distance = distance;
        }

        public Movie getMovie() {
            return movie;
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(MovieWrapper o) {
            if (Objects.equals(this.movie.getId(), o.getMovie().getId()))
                return 0;
            if (o.distance > this.distance)
                return -1;
            return 1;
        }
    }
}
