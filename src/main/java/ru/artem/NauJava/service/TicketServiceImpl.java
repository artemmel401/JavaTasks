package ru.artem.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.entity.Ticket;
import ru.artem.NauJava.repository.TicketRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket bookTicket(Ticket ticket) {
        if (!isSeatAvailable(ticket.getId(), ticket.getHallNumber(),
                ticket.getRowNumber(), ticket.getSeatNumber())) {
            throw new IllegalStateException("Место уже занято: " + ticket.getSeatInfo());
        }
        ticketRepository.create(ticket);
        System.out.println("Билет успешно забронирован: " + ticket.getSeatInfo());
        return ticket;
    }

    @Override
    public Optional<Ticket> findTicketById(Long id) {
        Ticket ticket = ticketRepository.read(id);
        System.out.println("Ваш билет: " + ticket.getSeatInfo());
        return Optional.of(ticket);
    }

    @Override
    public List<Ticket> findAllTickets() {
        List<Ticket> tickets = getAllTickets();
        System.out.println("Ваши билеты: " + tickets);
        return tickets;
    }

    @Override
    public void updateTicket(Ticket ticket) {
        // Проверяем существование билета
        if (findTicketById(ticket.getId()).isEmpty()) {
            throw new IllegalArgumentException("Билет с ID " + ticket.getId() + " не найден");
        }

        ticketRepository.update(ticket);
        System.out.println("Билет обновлен: " + ticket.getId());
    }

    @Override
    public void cancelTicket(Long ticketId) {
        // Проверяем существование билета
        if (findTicketById(ticketId).isEmpty()) {
            throw new IllegalArgumentException("Билет с ID " + ticketId + " не найден");
        }

        ticketRepository.delete(ticketId);
        System.out.println("Бронирование отменено для билета ID: " + ticketId);
    }

    @Override
    public List<Ticket> findTicketsByCustomer(String email) {
        List<Ticket> tickets = getAllTickets().stream()
                .filter(ticket -> email.equalsIgnoreCase(ticket.getCustomerEmail()))
                .toList();
        System.out.println("Ваши билеты: " + tickets);
        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByMovie(String movieTitle) {
        List<Ticket> tickets = getAllTickets().stream()
                .filter(ticket -> movieTitle.equalsIgnoreCase(ticket.getMovieTitle()))
                .toList();
        System.out.println("Ваши билеты: " + tickets);
        return tickets;
    }

    @Override
    public boolean isSeatAvailable(Long id, int hallNumber, int row, int seat) {
        boolean available = getAllTickets().stream()
                .noneMatch(ticket ->
                        id.equals(ticket.getId()) &&
                                hallNumber == ticket.getHallNumber() &&
                                row == ticket.getRowNumber() &&
                                seat == ticket.getSeatNumber()
                );
        if (available) {
            System.out.println("Место свободно");
        } else {
            System.out.println("Место занято");
        }
        return available;
    }

    @Override
    public double calculateTotalRevenue() {
        double total = getAllTickets().stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
        System.out.println(total);
        return total;
    }

    @Override
    public int getBookedTicketsCount() {
        int sum = getAllTickets().size();
        System.out.println(sum);
        return sum;
    }

    private List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
