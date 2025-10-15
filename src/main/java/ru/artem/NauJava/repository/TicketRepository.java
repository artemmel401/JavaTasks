package ru.artem.NauJava.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.artem.NauJava.entity.Ticket;
import java.util.List;
import java.util.Optional;

@Component
public class TicketRepository implements CrudRepository<Ticket, Long> {

    private final List<Ticket> ticketContainer;
    @Autowired
    public TicketRepository(List<Ticket> ticketContainer)
    {
        this.ticketContainer = ticketContainer;
    }
    @Override
    public void create(Ticket ticket)
    {
        ticketContainer.add(ticket);
    }
    @Override
    public Ticket read(Long id)
    {
        Optional<Ticket> foundTicket = ticketContainer.stream().filter(ticket -> id.equals(ticket.getId()))
                .findFirst();
        return foundTicket.orElse(null);

    }
    @Override
    public void update(Ticket ticket)
    {
        int index = -1;
        for (int i = 0; i < ticketContainer.size(); i++) {
            if (ticket.getId().equals(ticketContainer.get(i).getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            ticketContainer.set(index, ticket);
        } else {
            System.out.println("Билет с ID " + ticket.getId() + " не найден для обновления");
        }
    }
    @Override
    public void delete(Long id)
    {
        boolean removed = ticketContainer.removeIf(ticket -> id.equals(ticket.getId()));

        if (removed) {
            System.out.println("Удален билет с ID: " + id);
        } else {
            System.out.println("Билет с ID " + id + " не найден для удаления");
        }
    }
    @Override
    public List<Ticket> findAll() {
        return List.copyOf(ticketContainer);
    }
}
