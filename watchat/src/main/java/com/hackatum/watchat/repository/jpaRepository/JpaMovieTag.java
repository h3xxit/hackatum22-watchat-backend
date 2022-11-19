package com.hackatum.watchat.repository.jpaRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Tag")
@Table(name = "tag")
public class JpaMovieTag {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="match", nullable=false)
    private Double match;
}
