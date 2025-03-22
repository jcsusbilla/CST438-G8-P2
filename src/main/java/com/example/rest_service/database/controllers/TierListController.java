
package com.example.rest_service.database.controllers;

import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.TierRanking;
import com.example.rest_service.database.repositories.TierListRepository;
import com.example.rest_service.database.repositories.TierRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/tierlists")
public class TierListController {

    @Autowired
    private TierListRepository tierListRepository;


    // CREATE
    // Note that tier list doesn't know anything about the rankings its basically a template with all the metadata for the tierRankings

    /**
     * This allows a TierList to be added to the table
     * Required Parameters ar String: title and String: subject
     * <p>
     * Optional Parameter: String: weekStartDate
     * If there is a given date convert it into localDate object
     * Else use the current date as a default
     **/

    @Autowired
    private TierRankingRepository tierRankingRepository;
    @PostMapping(path = "/add")
    public ResponseEntity<Map<String, String>> addTierList(@RequestBody TierListRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            System.out.println("🎯 Received Rankings: " + request.getRankings());
            LocalDate startDate = (request.getWeekStartDate() != null && !request.getWeekStartDate().isEmpty())
                    ? LocalDate.parse(request.getWeekStartDate())
                    : LocalDate.now();

            // 1. Save the TierList
            TierList newTierList = new TierList(request.getTitle(), request.getSubject(), startDate);
            newTierList.setUserId(request.getUserId());
            tierListRepository.save(newTierList);

//            // 2. Save Rankings if present
            if (request.getRankings() != null && !request.getRankings().isEmpty()) {
                System.out.println("🎯 Received Rankings: " + request.getRankings());

                for (Map<String, String> entry : request.getRankings()) {
                    String tier = entry.get("tier");
                    String item = entry.get("item");

                    if (tier != null && item != null) {
                        TierRanking ranking = new TierRanking(tier, item, newTierList);
                        tierRankingRepository.save(ranking);
                    } else {
                        System.out.println("⚠️ Skipping invalid entry: " + entry);
                    }
                }
            }
//            if (request.getRankings() != null) {
//                for (Map<String, String> entry : request.getRankings()) {
//                    String tier = entry.get("tier");
//                    String item = entry.get("item");
//                    if (tier != null && item != null) {
//                        TierRanking ranking = new TierRanking(tier, item, newTierList);
//                        tierRankingRepository.save(ranking);
//                    }
//                }
//            }

            response.put("message", "Tier List saved successfully");
            response.put("tierListId", String.valueOf(newTierList.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DateTimeParseException e) {
            response.put("message", "Invalid date format. Please use YYYY-MM-DD.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while saving the Tier List.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
//    @PostMapping(path = "/add")
//    public ResponseEntity<Map<String, String>> addTierList(@RequestParam String title,
//                                                           @RequestParam String subject,
//                                                           @RequestParam(required = false) String weekStartDate) {
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            LocalDate startDate = (weekStartDate != null && !weekStartDate.isEmpty())
//                    ? LocalDate.parse(weekStartDate)
//                    : LocalDate.now();
//
//            TierList newTierList = new TierList(title, subject, startDate);
//            tierListRepository.save(newTierList);
//
//            response.put("message", "Tier List saved successfully");
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (DateTimeParseException e) {
//            response.put("message", "Invalid date format. Please use YYYY-MM-DD.");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        } catch (Exception e) {
//            response.put("message", "An error occurred while saving the Tier List.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }


    // DELETE by id with PathVariable (i think this is the better way)
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteTierListByPathId(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();

        if (!tierListRepository.existsById(id)) {
            response.put("message", "Tier List does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            tierListRepository.deleteById(id);
            response.put("message", "Tier List deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while deleting the Tier List.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //UPDATE

    /**
     * This mapping finds the Tierlist you want to update by id
     * There are three optional parameters to modify here
     * String: title
     * String: subject
     * String: date
     * <p>
     * Any or none of these fields may be modified
     */
    @PutMapping(path = "update/{id}")
    public ResponseEntity<Map<String, String>> updateTierList(@PathVariable Integer id,
                                                              @RequestParam(required = false) String title,
                                                              @RequestParam(required = false) String subject,
                                                              @RequestParam(required = false) String date) {
        Map<String, String> response = new HashMap<>();

        // Check if the Tier List exists
        Optional<TierList> existingTierList = tierListRepository.findById(id);

        if (existingTierList.isPresent()) {
            TierList tierList = existingTierList.get();
            boolean update = false;

            // Update only if the parameter is provided
            if (title != null && !title.isEmpty()) {
                tierList.setTitle(title);
                update = true;
            }
            if (subject != null && !subject.isEmpty()) {
                tierList.setSubject(subject);
                update = true;
            }
            if (date != null && !date.isEmpty()) {
                try {
                    tierList.setWeekStartDate(LocalDate.parse(date));
                    update = true;
                } catch (DateTimeParseException e) {
                    response.put("message", "Invalid date format. Please use YYYY-MM-DD.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }

            if (!update) {
                response.put("message", "Update Failed: No parameters selected for change!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Save the updated Tier List
            tierListRepository.save(tierList);
            response.put("message", "Tier List updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {
            response.put("message", "Tier List not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    ///////////////////// GET REQUESTS ///////////////////////////////////////////////

    // GET all TierLists
    @GetMapping("/all")
    public ResponseEntity<List<TierList>> getAllTierLists() {
        List<TierList> tierLists = (List<TierList>) tierListRepository.findAll();
        return ResponseEntity.ok(tierLists);
    }

    // Get by ID using path variable
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTierListById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        Optional<TierList> tierList = tierListRepository.findById(id);

        if (tierList.isPresent()) {
            response.put("tierList", tierList.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Tier List not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @GetMapping(path = "title/{title}")
    public ResponseEntity<Map<String, Object>> getTierListByTitle(@PathVariable String title) {
        Map<String, Object> response = new HashMap<>();

        Optional<TierList> tierList = tierListRepository.findByTitle(title);

        if (tierList.isPresent()) {
            response.put("tierList", tierList.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Tier List not found with title: " + title);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // Get TierList by Subject (NEEDS TESTING)
    @GetMapping(path = "subject/{subject}")
    public ResponseEntity<Map<String, Object>> getTierListBySubject(@PathVariable String subject) {
        Map<String, Object> response = new HashMap<>();

        Optional<TierList> tierList = tierListRepository.findBySubject(subject);

        if (tierList.isPresent()) {
            response.put("tierList", tierList.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Tier List not found with subject: " + subject);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getTierListsByUser(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Let JPA handle filtering directly
            List<TierList> tierLists = tierListRepository.findByUserId(userId);

            response.put("message", "Tier lists fetched successfully");
            response.put("tierLists", tierLists);
            response.put("count", tierLists.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while fetching tier lists");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Should probably add one to get by date as well
    @GetMapping("/user/{userId}/with-rankings")
    public ResponseEntity<Map<String, Object>> getTierListsWithRankings(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // ✅ Fetch all tier lists created by this user
            List<TierList> tierLists = tierListRepository.findByUserId(userId);
            List<Map<String, Object>> tierListData = new ArrayList<>();

            for (TierList list : tierLists) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", list.getId());
                data.put("title", list.getTitle());
                data.put("subject", list.getSubject());

                // ✅ Fetch raw rankings
                List<TierRanking> rankings = tierRankingRepository.findByTierListId(list.getId());

                // ✅ Map each TierRanking into a simple tier-item structure
                List<Map<String, String>> rankingList = rankings.stream().map(r -> Map.of(
                        "tier", r.getTier(),
                        "item", r.getItem()
                )).collect(Collectors.toList());

                // ✅ Add to the response
                data.put("rankings", rankingList);

                tierListData.add(data);
            }

            response.put("tierLists", tierListData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to retrieve tier lists");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    ///////////////////// GET REQUESTS END //////////////////////////////////////////


}







