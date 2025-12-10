package ru.artem.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private String name;

    @Column(name = "total_rows")
    private Integer totalRows;

    @Column(name = "seats_per_row")
    private Integer seatsPerRow;

}
