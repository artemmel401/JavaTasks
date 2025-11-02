package ru.artem.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artem.NauJava.dao.UserRepositoryImpl;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class UserCustomTest {
    private final UserRepositoryImpl userRepositoryImpl;
    private final UserRepository userRepository;

    @Autowired
    UserCustomTest (UserRepositoryImpl userRepositoryImpl, UserRepository userRepository) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.userRepository = userRepository;
    }

    @Test
    void testFindByEmail() {
        String userEmail = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail(userEmail);
        userRepository.save(user);
        Optional<User> foundUser =  userRepositoryImpl.findByEmail(userEmail);
        Assertions.assertNotNull(foundUser);
    }

    @Test
    void testFindByRegistrationDate() {
        LocalDateTime userRegistrationDate = LocalDateTime.now();
        User user = new User();
        user.setRegistrationDate(userRegistrationDate);
        userRepository.save(user);
        List<User> foundUsers =  userRepositoryImpl.findByRegistrationDate(userRegistrationDate);
        Assertions.assertEquals(1, foundUsers.size());
    }
}
