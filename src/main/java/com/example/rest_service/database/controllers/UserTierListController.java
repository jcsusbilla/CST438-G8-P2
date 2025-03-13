package com.example.rest_service.database.controllers;

import com.example.rest_service.database.entities.TierRanking;
import com.example.rest_service.database.repositories.TierRankingRepository;
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

    @Autowired
    private UserTierListRepository userTierListRepository;
    @Autowired
    private TierListRepository tierListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TierRankingRepository tierRankingRepository;

    @PostMapping(path = "add")
    public ResponseEntity<String> createUserTierList(@RequestParam Integer userId,
                                                     @RequestParam Integer tierId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + userId);
        }
        if (!tierListRepository.existsById(tierId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tier List not found with ID: " + tierId);
        }

        if (userTierListRepository.findByUserIdAndTierListId(userId, tierId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User is already assigned to this Tier List.");
        }

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

    @GetMapping("all")
    public @ResponseBody Iterable<UserTierList> getAllUserTierLists() {
        return userTierListRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<TierList>> getUserTierListsByUserId(@PathVariable Integer userId) {
        List<UserTierList> userTierLists = userTierListRepository.findByUserId(userId);
        if (userTierLists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
        List<TierList> tierLists = new ArrayList<>();
        for (UserTierList userTierList : userTierLists) {
            tierLists.add(userTierList.getTierList());
        }
        return ResponseEntity.ok(tierLists);
    }

    @GetMapping("/user/{userId}/tier-rankings")
    public ResponseEntity<List<TierRanking>> getTierRankingsByUserId(@PathVariable Integer userId) {
        List<UserTierList> userTierLists = userTierListRepository.findByUserId(userId);
        if (userTierLists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        List<TierRanking> tierRankings = new ArrayList<>();
        for (UserTierList userTierList : userTierLists) {
            List<TierRanking> rankings = tierRankingRepository.findByTierListId(userTierList.getTierList().getId());
            tierRankings.addAll(rankings);
        }
        return ResponseEntity.ok(tierRankings);
    }

    @GetMapping("/user/{userId}/tier-list/{tierListId}/tier-rankings")
    public ResponseEntity<List<TierRanking>> getTierRankingsByUserAndTierList(
            @PathVariable Integer userId,
            @PathVariable Integer tierListId) {
        Optional<UserTierList> userTierList = userTierListRepository.findByUserIdAndTierListId(userId, tierListId);
        if (userTierList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        List<TierRanking> rankings = tierRankingRepository.findByTierListId(tierListId);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/tier-lists/subject/{subject}")
    public ResponseEntity<List<TierList>> getTierListsBySubject(@PathVariable String subject) {
        List<TierList> tierLists = tierListRepository.findBySubjectIgnoreCase(subject);
        if (tierLists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(tierLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTierList> getUserTierListById(@PathVariable Integer id) {
        return userTierListRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
}
