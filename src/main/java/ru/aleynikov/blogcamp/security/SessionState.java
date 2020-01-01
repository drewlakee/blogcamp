package ru.aleynikov.blogcamp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.services.UserService;

import java.util.Optional;

@Component
public class SessionState {

    @Autowired
    private UserService userService;

    public void updateUserPrincipals() {
        Optional<User> userForUpdate = userService.findUserByUsername(SecurityUtils.getPrincipal().getUsername());

        if (userForUpdate.isPresent())
            SecurityUtils.getPrincipal().updateUserPrinciples(userForUpdate.get());
    }
}
