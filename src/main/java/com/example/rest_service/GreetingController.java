package com.example.rest_service;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final DatabaseService databaseService;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private boolean loggedIn = false;

    public GreetingController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/getAllUsers")
    public void getAllUsers(){
        System.out.println(databaseService.getAllUsers());
    }

    @GetMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password){
        List<Map<String, Object>> databasePass = databaseService.getPassword(username);
        String getPass = databasePass.getFirst().get("password_hash").toString();

        if(encoder.matches(password, getPass)){
            loggedIn = true;
        }
        System.out.println(getPass);
        System.out.println(databasePass);
    }

    @PostMapping("/add-user")
    public void addUser(@RequestParam String username, @RequestParam String password){
        String hashedPassword = encoder.encode(password);
        System.out.println("Username: " + username + " Password: " + hashedPassword);
    }

    @GetMapping("/")
    public String rootRoute(){
        return "Hello! You have reached the root route.";
    }
}