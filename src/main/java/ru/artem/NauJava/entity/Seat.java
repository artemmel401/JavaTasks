package ru.artem.NauJava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall_id;

    @Column
    private Integer row_number;

    @Column
    private Integer seat_number;

    @Column
    private boolean is_free;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hall getHallId() {
        return hall_id;
    }

    public void setHallId(Hall hall_id) {
        this.hall_id = hall_id;
    }

    public Integer getRowNumber() {
        return row_number;
    }

    public void setRowNumber(Integer row_number) {
        this.row_number = row_number;
    }

    public Integer getSeatNumber() {
        return seat_number;
    }

    public void setSeatNumber(Integer seat_number) {
        this.seat_number = seat_number;
    }

    public boolean isIsFree() {
        return is_free;
    }

    public void setIsFree(boolean is_free) {
        this.is_free = is_free;
    }
}
