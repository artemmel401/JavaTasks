package ru.artem.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.artem.NauJava.repository.SeatRepository;

@RestController
@RequestMapping("/seats")
public class SeatController {
    @Autowired
    private SeatRepository seatRepository;
    @GetMapping("/deleteByHallId")
    public void deleteByHallId(@RequestParam Long hallId)
    {
        seatRepository.deleteByHallId(hallId);
    }
}
