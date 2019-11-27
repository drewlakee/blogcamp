package ru.aleynikov.blogcamp;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringContext {

    @Bean
    public HikariDataSource HikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(Config.JDBC_DRIVER);
        hikariDataSource.setJdbcUrl(Config.JDBC_URL);
        hikariDataSource.setUsername(Config.USERNAME_PSQL);
        hikariDataSource.setPassword(Config.PASSWORD_PSQL);

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
}
