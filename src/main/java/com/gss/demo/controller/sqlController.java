package com.gss.demo.controller;

import com.gss.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
public class sqlController implements CommandLineRunner {

    @Autowired
    private userRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/user/{id}")
    public List<Object[]> findUserFromId(@PathVariable("id") String id) {
        return userRepository.findUserByIdUnsafe(id);
    }

    @Override
    public void run(String... args) throws Exception {
        // Create the `users` table if it doesn't exist
        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL)";
        jdbcTemplate.execute(createTableSql);

        // Insert users if no records exist in the `users` table
        Long userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
        if (userCount == 0) {
            String sql1 = "INSERT INTO users (name, password) VALUES ('Alice', 'password123')";
            String sql2 = "INSERT INTO users (name, password) VALUES ('Bob', 'securePass456')";
            String sql3 = "INSERT INTO users (name, password) VALUES ('Charlie', 'charlie789')";

            jdbcTemplate.update(sql1);
            jdbcTemplate.update(sql2);
            jdbcTemplate.update(sql3);

            System.out.println("Users have been initialized.");
        } else {
            System.out.println("Users already exist. Skipping initialization.");
        }
    }
}
