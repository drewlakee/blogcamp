package ru.aleynikov.blogcamp;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.aleynikov.blogcamp.staticResources.ConfigResources;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    }

    @Bean
    public HikariDataSource HikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(ConfigResources.JDBC_DRIVER);
        hikariDataSource.setJdbcUrl(ConfigResources.JDBC_URL);
        hikariDataSource.setUsername(ConfigResources.USERNAME_PSQL);
        hikariDataSource.setPassword(ConfigResources.PASSWORD_PSQL);

        hikariDataSource.setMaximumPoolSize(ConfigResources.MAX_HIKARI_POOL_SIZE);

        return hikariDataSource;
    }

    @Bean
    public JdbcTemplate JdbcTemplate() {
        return new JdbcTemplate(HikariDataSource());
    }

}
