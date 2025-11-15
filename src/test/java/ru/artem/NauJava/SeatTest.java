package ru.artem.NauJava;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.entity.Seat;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.SeatRepository;

@SpringBootTest
class SeatTest {
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    @Autowired
    SeatTest(SeatRepository seatRepository, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

    @Test
    @Transactional
    void testDeleteByHallId () {
        Hall hall = new Hall();
        Hall savedHall = hallRepository.save(hall);
        Seat seat = new Seat();
        seat.setHallId(savedHall);
        long oldRows = seatRepository.count();
        seatRepository.save(seat);
        seatRepository.deleteByHallId(savedHall.getId());

        Assertions.assertEquals(oldRows, seatRepository.count());
    }

}
