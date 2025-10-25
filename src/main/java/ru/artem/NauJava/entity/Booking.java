package ru.artem.NauJava.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session_id;

    @Column
    private LocalDateTime booking_time;

    @Column
    private Integer total_amount;

    @Column
    private String payment_status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return user_id;
    }

    public void setUserId(User user_id) {
        this.user_id = user_id;
    }

    public Session getSessionId() {
        return session_id;
    }

    public void setSessionId(Session session_id) {
        this.session_id = session_id;
    }

    public LocalDateTime getBookingTime() {
        return booking_time;
    }

    public void setBookingTime(LocalDateTime booking_time) {
        this.booking_time = booking_time;
    }

    public Integer getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public String getPaymentStatus() {
        return payment_status;
    }

    public void setPaymentStatus(String payment_status) {
        this.payment_status = payment_status;
    }
}
