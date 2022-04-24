package com.alura.challenge3.service;

import com.alura.challenge3.model.User;
import com.alura.challenge3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User createUser(String name, String email){
        return repository.save(new User(name, email));
    }
}
