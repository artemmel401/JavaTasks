package ru.artem.NauJava.services.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artem.NauJava.entity.Session;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.SessionRepository;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              BookingRepository bookingRepository) {
        this.sessionRepository = sessionRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public void deleteSession(Long id) {
        bookingRepository.deleteBySessionId(id);
        sessionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteSessionByMovieId(Long movieId) {
        List<Session> sessionsToDelete = sessionRepository.findByMovieId(movieId);

        for (Session session : sessionsToDelete) {
            deleteSession(session.getId());
        }
    }
    @Override
    @Transactional
    public void deleteSessionByHallId(Long hallId) {
        List<Session> sessionsToDelete = sessionRepository.findByHallId(hallId);
        for (Session item : sessionsToDelete) {
            this.deleteSession(item.getId());
        }
    }
}
