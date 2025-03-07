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

    //What I used to test this GET http://localhost:8080/tier-rankings/154 (postman)
    @GetMapping("/{tierListId}")
    public ResponseEntity<List<TierRanking>> getRanking(@PathVariable int tierListId){
        String sql = """
                    SELECT tr.id, tr.item, tr.tier, tl.id AS tier_list_id, tl.title AS tier_list_title, tl.subject AS subject, tl.week_start_date
                    FROM tier_ranking tr
                    JOIN tier_list tl ON tr.tier_list_id = tl.id
                    WHERE tr.tier_list_id = ?
                    """;

        List<TierRanking> rankings = jdbcTemplate.query(sql, (rs, rowNum) -> {
            TierRanking ranking = new TierRanking();
            ranking.setId(rs.getInt("id"));
            ranking.setItem(rs.getString("item"));
            ranking.setTier(TierRanking.Tier.valueOf(rs.getString("tier")));

            TierList tierList = new TierList();
            tierList.setId(rs.getInt("tier_list_id"));
            tierList.setTitle(rs.getString("tier_list_title"));
            tierList.setSubject(rs.getString("subject"));
            tierList.setWeekStartDate(rs.getDate("week_start_date").toLocalDate());

            ranking.setTierList(tierList);

            return ranking;
        }, tierListId);

        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/ranking/{id}")
    public ResponseEntity<TierRanking> getRankingById(@PathVariable int id) {
        String sql = """
                    SELECT tr.id, tr.item, tr.tier, tl.id AS tier_list_id, tl.title AS tier_list_title, tl.subject, tl.week_start_date 
                    FROM tier_ranking tr
                    JOIN tier_list tl ON tr.tier_list_id = tl.id
                    WHERE tr.id = ?
                    """;

        try {

            TierRanking ranking = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                TierRanking r = new TierRanking();
                r.setId(rs.getInt("id"));
                r.setItem(rs.getString("item"));
                r.setTier(TierRanking.Tier.valueOf(rs.getString("tier")));

                TierList tierList = new TierList();
                tierList.setId(rs.getInt("tier_list_id"));
                tierList.setTitle(rs.getString("tier_list_title"));
                tierList.setSubject(rs.getString("subject"));
                tierList.setWeekStartDate(rs.getDate("week_start_date").toLocalDate());

                r.setTierList(tierList);
                return r;
            }, id);

            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
