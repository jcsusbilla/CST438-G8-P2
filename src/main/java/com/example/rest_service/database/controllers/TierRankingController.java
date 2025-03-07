package com.example.rest_service.database.controllers;


import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.TierRanking;
import com.example.rest_service.database.repositories.TierRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tier-rankings")
public class TierRankingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/add")
    public ResponseEntity<String> addRanking(@RequestParam int tierListId,
                                             @RequestParam String item,
                                             @RequestParam String tier){

        //This is to check isf the tier list exists
        String checkTierListSQL = "SELECT COUNT(*) FROM tier_list WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(checkTierListSQL, Integer.class, tierListId);

        if(count == null || count == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tier List not found.");
        }

        //This inserts the values into the tier list
        String insertSQL = "INSERT INTO tier_ranking (tier_list_id, item, tier) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSQL, tierListId, item, tier);

        return ResponseEntity.ok("Ranking added successfully.");
    }


}
