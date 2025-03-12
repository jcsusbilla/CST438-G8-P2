package com.example.rest_service.database.controllers;

import com.example.rest_service.database.repositories.UserRepository;
import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.User;
import com.example.rest_service.database.entities.UserTierList;
import com.example.rest_service.database.repositories.TierListRepository;
import com.example.rest_service.database.repositories.UserTierListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/user-tier-lists")
public class UserTierListController {

    // Most likely need access to all repositories for any advanced queries.
    // Need at least 3 for normal CRUD

    @Autowired
    private UserTierListRepository userTierListRepository;
    @Autowired
    private TierListRepository tierListRepository;
    @Autowired
    private UserRepository userRepository;

    // CREATE MAPPING
    // Right now I want to see how we could use the http sessions to autopopulate userId with a signed in userID
    @PostMapping(path = "add")
    public ResponseEntity<String> createUserTierList(@RequestParam Integer userId,
                                                     @RequestParam Integer tierId) {
        // Check if user and tier list exist
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + userId);
        }
        if (!tierListRepository.existsById(tierId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tier List not found with ID: " + tierId);
        }

        // Check if the UserTierList already exists
        if (userTierListRepository.findByUserIdAndTierListId(userId, tierId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User is already assigned to this Tier List.");
        }

        // Create and save the new UserTierList
        UserTierList newUserTierList = new UserTierList();
        newUserTierList.setUser(userRepository.getReferenceById(userId));
        newUserTierList.setTierList(tierListRepository.getReferenceById(tierId));

        userTierListRepository.save(newUserTierList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("UserTierList created successfully.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTierList(@PathVariable Integer id) {
        if (!userTierListRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("UserTierList not found with ID: " + id);
        }

        userTierListRepository.deleteById(id);
        return ResponseEntity.ok("UserTierList deleted successfully.");
    }


    @GetMapping
    public @ResponseBody Iterable<UserTierList> getAllUserTierLists() {
        return userTierListRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<TierList>> getUserTierListsByUserId(@PathVariable Integer userId) {

        // Get the list of UserTierList objects associated with the user
        List<UserTierList> userTierLists = userTierListRepository.findByUserId(userId);

        // check if the list is empty
        if (userTierLists == null || userTierLists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        // create a new list
        List<TierList> tierLists = new ArrayList<>();
        for (UserTierList userTierList : userTierLists) {
            tierLists.add(userTierList.getTierList());
        }

        // Return a successful response with the  TierLists
        return ResponseEntity.ok(tierLists);
    }


}