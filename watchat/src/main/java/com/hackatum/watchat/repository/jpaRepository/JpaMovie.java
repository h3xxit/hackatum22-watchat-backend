package com.hackatum.watchat.repository.jpaRepository;

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
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "tmdb_id")
    private Long id;
    @Column(name="name", nullable=false)
    private String name;
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
    @ManyToMany
    private List<JpaMovie> neighbours;
}
