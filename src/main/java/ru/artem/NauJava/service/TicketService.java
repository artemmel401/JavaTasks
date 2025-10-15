package ru.artem.NauJava.service;

import ru.artem.NauJava.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket bookTicket(Ticket ticket);
    Optional<Ticket> findTicketById(Long id);
    List<Ticket> findAllTickets();
    void updateTicket(Ticket ticket);
    void cancelTicket(Long ticketId);

    // Бизнес-логика
    List<Ticket> findTicketsByCustomer(String email);
    List<Ticket> findTicketsByMovie(String movieTitle);
    boolean isSeatAvailable(Long sessionId, int hallNumber, int row, int seat);
    double calculateTotalRevenue();
    int getBookedTicketsCount();
}
