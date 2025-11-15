package ru.artem.NauJava.service;


import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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
public class UserService implements UserDetailsService
{
    private final UserRepositoryImpl userRepositoryCustom;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepositoryImpl userRepositoryCustom, UserRepository userRepository,
                       PasswordEncoder passwordEncoder)
    {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser (User user) {
        user.setIsActive(true);
        user.setRole(Role.ADMIN);
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
                    mapRoles(appUser));
        }
        else
        {
            throw new UsernameNotFoundException("user not found");
        }
    }
    private Collection<GrantedAuthority> mapRoles(User appUser)
    {
        return appUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" +
                        role.name())).collect(Collectors.toList());
    }
}

