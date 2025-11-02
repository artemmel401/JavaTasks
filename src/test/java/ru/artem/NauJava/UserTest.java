package ru.artem.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.repository.UserRepository;

import java.util.List;

@SpringBootTest
class UserTest {
    private final UserRepository userRepository;

    @Autowired
    UserTest (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testFindIsActiveUsers () {
        List<User> allUsers = (List<User>) userRepository.findAll();
        long activeCount = allUsers.stream()
                .filter((User::getIsActive))
                .count();
        long disActiveCount = allUsers.size() - activeCount;
        User user1 = new User();
        user1.setIsActive(true);
        User user2 = new User();
        user2.setIsActive(false);
        userRepository.save(user1);
        userRepository.save(user2);
        List<User> activeUsers = userRepository.findByIsActive(true);
        List<User> disActiveUsers = userRepository.findByIsActive(false);
        Assertions.assertEquals(activeCount + 1, activeUsers.size());
        Assertions.assertEquals(disActiveCount + 1, disActiveUsers.size());
    }
}
