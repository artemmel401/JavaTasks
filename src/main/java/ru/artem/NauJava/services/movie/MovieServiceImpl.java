package ru.artem.NauJava.services.movie;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.entity.Movie;
import ru.artem.NauJava.repository.MovieRepository;
import ru.artem.NauJava.services.session.SessionServiceImpl;

import java.util.List;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final SessionServiceImpl sessionService;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository,
                            SessionServiceImpl sessionService) {
        this.movieRepository = movieRepository;
        this.sessionService = sessionService;
    }

    @Override
    @Transactional
    public void deleteMovie (Long id) {
        log.info("Начинаем удаление фильма ID: {}", id);
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Фильм с ID " + id + " не найден"
        ));
        sessionService.deleteSessionByMovieId(movie.getId());
        log.info("Удалили сессии ");
        movieRepository.deleteById(id);
        log.info("Удалили фильм");
    }

    @Override
    @Transactional
    public void deleteMovieByCinemaId(Long id) {
        List<Movie> moviesToDelete = movieRepository.findByCinemaId(id);
        for (Movie item : moviesToDelete) {
            this.deleteMovie(item.getId());
        }
    }
}
