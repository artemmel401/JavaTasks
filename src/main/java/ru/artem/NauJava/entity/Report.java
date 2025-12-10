package ru.artem.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.artem.NauJava.model.Status;

@Setter
@Getter
@Entity
@Table(name = "Reports")
public class Report {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(length = 10000)
    private String content;

}
