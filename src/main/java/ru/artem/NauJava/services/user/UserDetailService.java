package ru.artem.NauJava.services.user;


import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.artem.NauJava.dao.UserRepositoryImpl;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.Role;
import ru.artem.NauJava.repository.UserRepository;

@Component
public class UserDetailService implements UserDetailsService
{
    private final UserRepositoryImpl userRepositoryCustom;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailService(UserRepositoryImpl userRepositoryCustom, UserRepository userRepository,
                             PasswordEncoder passwordEncoder)
    {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser (User user) {
        long usersSize = userRepository.count();
        user.setActive(true);
        if (usersSize == 0){
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        user.setRegistrationDate(LocalDateTime.now());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User appUser = userRepositoryCustom.findByEmail(username)
                .orElse(null);
        if (appUser != null)
        {
            return new
                    org.springframework.security.core.userdetails.User(
                    appUser.getEmail(), appUser.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())));
        }
        else
        {
            throw new UsernameNotFoundException("user not found");
        }
    }
}

