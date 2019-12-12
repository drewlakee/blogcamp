package ru.aleynikov.blogcamp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.services.UserService;

@Component
public class SessionState {

    @Autowired
    private UserService userService;

    public void updateUserPrincipals() {
        SecurityUtils.getPrincipal().refreshUser(userService.findUserByUsername(SecurityUtils.getPrincipal().getUsername()));
    }
}
