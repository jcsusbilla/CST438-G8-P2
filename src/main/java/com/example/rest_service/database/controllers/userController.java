package com.example.rest_service.database.controllers;

import com.example.rest_service.database.repositories.UserRepository;
import com.example.rest_service.database.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

// This means that this class is a Controller
@Controller
@RequestMapping(path = "/user") // For me to refer to the base URL is https://localhost:8080/user
public class userController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    //JPA
    private UserRepository userRepository; // For my reference this is to talk to the database. If I'm not wrong

    @Autowired
    private JdbcTemplate jdbcTemplate; //This adds the jdbcTemplate that allows for raw SQL Queries

    /**
     * This is for Secure User Registration.
     * This method takes in a name, email, and password and saves a new user into the database.
     * Here we hash the password before we save, so that it's not store in plain text.
     */
    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(@RequestParam String user_name,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String first_name,
                                           @RequestParam String last_name) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Error: Email already exists. Please use a different email.";
        }

        Optional<User> existingUserName = userRepository.findByUserName(user_name);

        if (existingUserName.isPresent()) {
            return "Error: Username already exists. Please use a different username.";
        }


        //Here we create a new user object
        User n = new User();
        n.setUserName(user_name);
        n.setEmail(email);
        n.setFirstName(first_name);
        n.setLastName(last_name);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //At this point we now have a hashed password no longer in plain text
        String hashedPassword = encoder.encode(password);

        n.setPassword(hashedPassword); //Here we are now using the new secure password.

        //Here we save the User into the database.
        userRepository.save(n);
        return "User registered successfully!";
    }

    // https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/oauth2/core/user/OAuth2User.html
    // https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/core/annotation/AuthenticationPrincipal.html
    // this path now simply handles logging in a user after authentication.
    @PostMapping("/oauth2/google")
    public @ResponseBody String googleLogin(@AuthenticationPrincipal OAuth2User user, HttpSession session) {
        if (user == null) {
            return "Error: Unable to authenticate with Google.";
        }

        String email = user.getAttribute("email");
        // Check if the user already exists in the database using email. This is kind of redundant since a user will be
        // I want to get the attributes from the actual database because a googleOAuth user may have changed their
        // userName. If i just took the info from user.getAttribute("userName") it may not match anymore.
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User userFromDb = existingUser.get();
            session.setAttribute("userEmail", userFromDb.getEmail());
            session.setAttribute("userName", userFromDb.getUserName());
            session.setAttribute("isLoggedIn", true);
            return "Google Login Successful! Welcome " + userFromDb.getUserName();
        }

        return "Error: Unable to authenticate with Google.";
    }


    //Just for my reference queryForMap() is only for 1 row. queryForList() is for multiple queries
    @PostMapping("/login") //This is the route for user login
    public @ResponseBody String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        String sql = "SELECT id, user_name, password, first_name, last_name FROM user WHERE email = ?";

        try {
            //This executes the SQL query using the "queryForMap()"
            //It is then returned as a map with Map<String, Object) where:
            //Each **COLUMN** name from the database is **KEY** in the Map.
            //Each **COLUMN VALUE** (from the database row) is stored as a **value** in the Map.
            Map<String, Object> userData = jdbcTemplate.queryForMap(sql, email);
            // Example: If the database has this row:
            // | id | user_name | email       | password |
            // | 1  | John     | John@example | #$#@$##$ |
            // Therefore "queryForMap(sql, "John@example.com")" will return:
            // userData = {
            //    "id": 1,
            //    "user_name": "John",
            //    "password": #$#@$##$
            //} This is my understanding of this.

            String userName = (String) userData.get("user_name");
            String hashedPassword = (String) userData.get("password");
            String firstName = (String) userData.get("first_name");
            String lastName = (String) userData.get("last_name");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(password, hashedPassword)) {
                session.setAttribute("userEmail", email);
                session.setAttribute("userName", userName);
                session.setAttribute("isLoggedIn", true);
                return "Login successful! Welcome " + userName;
            } else {
                return "Incorrect password. Please try again!";
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
            return "Error logging in. Please try again later.";
        }
    }


    @GetMapping("/logout")
    public @ResponseBody String logout(HttpSession session) {
        session.invalidate();
        return "Logged out successfully!";
    }

    // Get all users
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}