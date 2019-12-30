package ru.aleynikov.blogcamp.domain.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.aleynikov.blogcamp.domain.extensions.RoleProver;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
public class User implements UserDetails, RoleProver {

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
    private Country country;
    private Optional<City> city;
    private Role role;
    private String avatar;
    private boolean isBanned;

    public void refreshUser(User user) {
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
        this.isBanned = user.isBanned();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));

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

    @Override
    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    @Override
    public boolean isAnonymous() {
        return role.equals(Role.UNKNOWN);
    }
}
