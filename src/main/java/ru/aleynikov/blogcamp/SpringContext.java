package ru.aleynikov.blogcamp;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

@Configuration
public class SpringContext {

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${psql.username}")
    private String psqlUsername;

    @Value("${psql.password}")
    private String psqlPassword;

    @Bean
    public HikariDataSource HikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(jdbcDriver);
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(psqlUsername);
        hikariDataSource.setPassword(psqlPassword);

        return hikariDataSource;
    }

    @Bean
    public JdbcTemplate JdbcTemplate() {
        return new JdbcTemplate(HikariDataSource());
    }

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Pattern regularExpForUsername() {
        int minLength = 6;
        int maxLength = 30;
        return Pattern.compile(String.format("^(\\w|\\d){%d,%d}+$", minLength, maxLength));
    }

    @Bean
    public Pattern regularExpForPassword() {
        int minLength = 8;
        int maxLength = 30;
        return Pattern.compile(String.format("^(\\w|\\d){%d,%d}+$", minLength, maxLength));
    }
}
