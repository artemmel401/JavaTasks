package ru.artem.NauJava.dao;

import java.time.LocalDateTime;
import java.util.List;

import ru.artem.NauJava.entity.User;
public interface UserRepositoryCustom
{

    List<User> findByEmail(String email);

    List<User> findByRegistrationDate(LocalDateTime localDateTime);
}
