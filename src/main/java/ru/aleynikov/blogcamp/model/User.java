package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;
import java.sql.Date;

@Setter
@Getter
public class User implements UserDetails {

    private int id;
    private String username;
    private String fullName;
    private String password;
    private String secretQuestion;
    private String secretAnswer;
    private boolean active;
    private Timestamp registeredDate;
    private String status;
    private Date birthday;
    private String country;
    private String city;
    private String role;
    private String avatar;

    public User() {}

    public void updateUserByNewUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.password = user.getPassword();
        this.secretQuestion = user.getSecretQuestion();
        this.secretAnswer = user.getSecretAnswer();
        this.active = user.isAccountNonLocked();
        this.registeredDate = user.getRegisteredDate();
        this.status = user.getStatus();
        this.birthday = user.getBirthday();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.role = user.getRole();
        this.avatar = user.getAvatar();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() { return active;
    }
}
