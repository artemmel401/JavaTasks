package ru.artem.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artem.NauJava.entity.Hall;
import ru.artem.NauJava.entity.Seat;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.SeatRepository;
import ru.artem.NauJava.service.HallService;

@SpringBootTest
class HallTest {
    private final HallService hallService;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public HallTest (
            HallService hallService,
            HallRepository hallRepository,
            SeatRepository seatRepository)
    {
        this.hallService = hallService;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }

    @Test
    void testDeleteHall_Success () {
        Hall hall = new Hall();
        hall.setName("Test Hall");
        Hall savedHall = hallRepository.save(hall);
        long oldRows = seatRepository.count();
        Seat seat1 = new Seat();
        seat1.setHallId(savedHall);
        seat1.setRowNumber(1);
        seat1.setSeatNumber(1);
        seatRepository.save(seat1);

        Seat seat2 = new Seat();
        seat2.setHallId(savedHall);
        seat2.setRowNumber(1);
        seat2.setSeatNumber(2);
        seatRepository.save(seat2);

        Long hallId = savedHall.getId();

        Assertions.assertTrue(hallRepository.existsById(hallId));
        Assertions.assertEquals(oldRows + 2, seatRepository.count());

        hallService.deleteHall(hallId);

        Assertions.assertFalse(hallRepository.existsById(hallId));
        Assertions.assertEquals(oldRows, seatRepository.count());
    }

    @Test
    void testDeleteHall_Exception() {

        Hall hall = new Hall();
        hall.setName("Test Hall for Rollback");
        Hall savedHall = hallRepository.save(hall);

        Seat seat = new Seat();
        seat.setHallId(savedHall);
        seat.setRowNumber(1);
        seat.setSeatNumber(1);
        seatRepository.save(seat);

        Long hallId = savedHall.getId();
        long initialSeatCount = seatRepository.count();

        HallService failingHallService = id -> {
            seatRepository.deleteByHallId(id);
            throw new RuntimeException("Искусственное исключение для теста отката");
        };

        try {
            failingHallService.deleteHall(hallId);
            Assertions.fail("Должно было выброситься исключение");
        } catch (RuntimeException e) {
            // Expected exception
        }
        Assertions.assertTrue(hallRepository.existsById(hallId));
        Assertions.assertEquals(initialSeatCount, seatRepository.count());
    }
}
