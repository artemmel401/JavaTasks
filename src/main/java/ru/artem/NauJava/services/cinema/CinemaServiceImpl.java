package ru.artem.NauJava.services.cinema;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.repository.CinemaRepository;
import ru.artem.NauJava.services.hall.HallService;
import ru.artem.NauJava.services.movie.MovieService;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final HallService hallService;
    private final MovieService movieService;
    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository,
                             HallService hallService,
                             MovieService movieService) {
        this.cinemaRepository = cinemaRepository;
        this.hallService = hallService;
        this.movieService = movieService;

    }

    @Override
    @Transactional
    public void deleteCinema(Long id) {
        hallService.deleteHallByCinemaId(id);
        movieService.deleteMovieByCinemaId(id);
        cinemaRepository.deleteById(id);
    }
}

