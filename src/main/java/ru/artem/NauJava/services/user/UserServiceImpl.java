package ru.artem.NauJava.services.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artem.NauJava.repository.BookingRepository;
import ru.artem.NauJava.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        bookingRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
