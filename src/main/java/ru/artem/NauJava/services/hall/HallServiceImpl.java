package ru.artem.NauJava.services.hall;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.SeatRepository;
import ru.artem.NauJava.services.session.SessionServiceImpl;

import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final SessionServiceImpl sessionService;

    @Autowired
    public HallServiceImpl (HallRepository hallRepository, SeatRepository seatRepository,
                            SessionServiceImpl sessionService)
    {
        this.hallRepository = hallRepository;
        this.seatRepository= seatRepository;
        this.sessionService = sessionService;
    }

    @Override
    @Transactional
    public void deleteHall(Long id) {
        seatRepository.deleteByHallId(id);
        sessionService.deleteSessionByHallId(id);
        hallRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteHallByCinemaId(Long id) {
        List<Hall> hallsToDelete = hallRepository.findByCinemaId(id);
        for (Hall item : hallsToDelete) {
            this.deleteHall(item.getId());
        }
    }


}
