package ru.artem.NauJava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "cinemas")
public class Cinema {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String director;

    @Column
    private String address;

}
