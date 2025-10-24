package ru.artem.NauJava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema_id;

    @Column
    private String name;

    @Column
    private Integer total_rows;

    @Column
    private Integer seats_per_row;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Cinema getCinemaId () {
        return cinema_id;
    }
    public void setCinemaId (Cinema cinema_id) {
        this.cinema_id = cinema_id;
    }

    public Integer getTotalRows () {
        return total_rows;
    }
    public void setTotalRows(Integer total_rows) {
        this.total_rows = total_rows;
    }

    public Integer getSeatsPerRow () {
        return seats_per_row;
    }
    public void setSeatsPerRow (Integer seats_per_row) {
        this.seats_per_row = seats_per_row;
    }
}
