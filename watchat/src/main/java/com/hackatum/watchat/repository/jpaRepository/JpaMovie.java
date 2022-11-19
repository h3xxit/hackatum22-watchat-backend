package com.hackatum.watchat.repository.jpaRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackatum.watchat.entities.Movie;
import com.hackatum.watchat.entities.MovieTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Movie")
@Table(name = "movie")
public class JpaMovie {
    @Id
    @Column(name = "tmdb_id")
    private Long id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;
    private String redirect;
    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "tmdb_id")
    private List<JpaMovieTag> tags;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<JpaMovie> neighbours;

    public Movie toMovie(ObjectMapper objectMapper){
        return new Movie(getId(), getName(), getDescription(), getImage(), getRedirect(),
                getTags().stream().map(jpaMovieTag -> objectMapper.convertValue(jpaMovieTag, MovieTag.class)).toList());
    }
}
