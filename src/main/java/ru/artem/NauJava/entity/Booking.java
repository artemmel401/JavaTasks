package ru.artem.NauJava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.artem.NauJava.model.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Setter
    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Setter
    @Getter
    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Setter
    @Getter
    @Column(name = "total_amount")
    private Integer totalAmount;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

}
