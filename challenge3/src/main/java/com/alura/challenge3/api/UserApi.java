package com.alura.challenge3.api;

import com.alura.challenge3.model.User;
import com.alura.challenge3.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller("/user")
public class UserApi {

    private final UserService userService;

    @PostMapping("/new")
    public User createUser(
        @RequestParam(name = "name", value = "Nome do usuário") String name,
        @RequestParam(name = "email", value = "Email do usuário") String email){
        return userService.createUser(name, email);
    }
}
