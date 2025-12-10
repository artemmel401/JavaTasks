package ru.artem.NauJava.services.session;


public interface SessionService {
    void deleteSession(Long id);
    void deleteSessionByMovieId(Long movieId);
    void deleteSessionByHallId(Long hallId);
}
