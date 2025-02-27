package com.example.rest_service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    private final JdbcTemplate jdbcTemplate;


    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAllUsers(){
        return jdbcTemplate.queryForList("SELECT * FROM users");
    }

    public List<Map<String, Object>> getPassword(String username){
        return jdbcTemplate.queryForList("SELECT password_hash FROM users WHERE username = ?", username);
    }



}
