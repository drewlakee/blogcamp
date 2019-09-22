package ru.aleynikov.blogcamp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.service.UserService;

@Component
public class SessionManager {

    @Autowired
    private UserService userService;

    public void updateSessionUser() {
        SecurityUtils.getPrincipal().updateUserByNewUser(userService.findUserByUsername(SecurityUtils.getPrincipal().getUsername()));
    }
}
