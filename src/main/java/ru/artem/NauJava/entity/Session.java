package ru.artem.NauJava.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie_id;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall_id;

    @Column
    private LocalDateTime start_time;

    @Column
    private LocalDateTime end_time;

    @Column
    private Integer price;

    public Movie getMovieId() {
        return movie_id;
    }

    public void setMovieId(Movie movie_id) {
        this.movie_id = movie_id;
    }

    public Hall getHallId() {
        return hall_id;
    }

    public void setHallId(Hall hall_id) {
        this.hall_id = hall_id;
    }

    public LocalDateTime getStartTime() {
        return start_time;
    }

    public void setStartTime(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEndTime() {
        return end_time;
    }

    public void setEndTime(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
