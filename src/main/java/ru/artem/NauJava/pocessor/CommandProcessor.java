package ru.artem.NauJava.pocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.artem.NauJava.entity.Ticket;
import ru.artem.NauJava.service.TicketService;

import java.time.LocalDateTime;

@Component
public class CommandProcessor
{
    private final TicketService ticketService;
    @Autowired
    public CommandProcessor(TicketService ticketService)
    {
        this.ticketService = ticketService;
    }
    public void processCommand(String input)
    {
        String[] cmd = input.split(" ");
        switch (cmd[0].toLowerCase()) {
            case "book" -> ticketService.bookTicket(parseCmd(cmd));
            case "list" -> ticketService.findAllTickets();
            case "find" -> ticketService.findTicketById(Long.valueOf(cmd[1]));
            case "cancel" -> ticketService.cancelTicket(Long.valueOf(cmd[1]));
            case "customer" -> ticketService.findTicketsByCustomer(cmd[1]);
            case "movie" -> ticketService.findTicketsByMovie(cmd[1]);
            case "update" -> ticketService.updateTicket(parseCmd(cmd));
            case "revenue" -> ticketService.calculateTotalRevenue();
            case "count" -> ticketService.getBookedTicketsCount();
            default -> System.out.println("Неизвестная команда: " + cmd[0]);
        }
    }

    private Ticket parseCmd (String[] cmd) {
        Long id = Long.valueOf(cmd[1]);
        String movieTitle = cmd[2];

        // формат: "2024-01-20T19:30:00"
        LocalDateTime sessionDateTime = LocalDateTime.parse(cmd[3]);

        int hallNumber = Integer.parseInt(cmd[4]);
        int rowNumber = Integer.parseInt(cmd[5]);
        int seatNumber = Integer.parseInt(cmd[6]);
        String customerName = cmd[7];
        String customerEmail = cmd[8];
        double price = Double.parseDouble(cmd[9]);
        return new Ticket(id, movieTitle, sessionDateTime, hallNumber,
                rowNumber, seatNumber, customerName, customerEmail, price);
    }
}
