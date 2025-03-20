package com.example.rest_service.database.controllers;

import com.example.rest_service.database.controllers.TierListRequest;
import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.TierRanking;
import com.example.rest_service.database.repositories.TierListRepository;
import com.example.rest_service.database.repositories.TierRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tier-rankings")
public class TierRankingController {

    private final TierRankingRepository tierRankingRepository;
    private final TierListRepository tierListRepository;

    public TierRankingController(TierRankingRepository tierRankingRepository, TierListRepository tierListRepository) {
        this.tierRankingRepository = tierRankingRepository;
        this.tierListRepository = tierListRepository;
    }

    // ✅ ADD a new ranking to an existing TierList
    @PostMapping("/add")
    public ResponseEntity<?> addRanking(@RequestBody Map<String, String> request) {
        Integer tierListId = Integer.parseInt(request.get("tierListId"));
        String tier = request.get("tier"); // e.g., "S", "A", "B", "C", etc.
        String item = request.get("item"); // The item being ranked

        if (tierListId == null || tier == null || item == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        Optional<TierList> tierListOptional = tierListRepository.findById(tierListId);
        if (tierListOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Tier list not found"));
        }

        TierList tierList = tierListOptional.get();
        TierRanking newRanking = new TierRanking(tier, item, tierList);
        tierRankingRepository.save(newRanking);

        return ResponseEntity.ok(Map.of("message", "Ranking added successfully", "rankingId", newRanking.getId()));
    }

    // ✅ GET all rankings for a given TierList
    @GetMapping("/{tierListId}")
    public ResponseEntity<?> getRankings(@PathVariable Integer tierListId) {
        List<TierRanking> rankings = tierRankingRepository.findByTierListId(tierListId);
        if (rankings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No rankings found for this tier list"));
        }
        return ResponseEntity.ok(rankings);
    }

    // ✅ UPDATE an existing ranking
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRanking(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        Optional<TierRanking> rankingOptional = tierRankingRepository.findById(id);
        if (rankingOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ranking not found"));
        }

        TierRanking ranking = rankingOptional.get();
        ranking.setTier(request.get("tier")); // e.g., move from "A" to "S"
        ranking.setItem(request.get("item")); // Change the ranked item name
        tierRankingRepository.save(ranking);

        return ResponseEntity.ok(Map.of("message", "Ranking updated successfully"));
    }

    // ✅ DELETE a ranking by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRanking(@PathVariable Integer id) {
        if (!tierRankingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ranking not found"));
        }

        tierRankingRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Ranking deleted successfully"));
    }
}