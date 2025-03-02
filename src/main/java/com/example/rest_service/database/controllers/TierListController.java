package com.example.rest_service.database.controllers;

import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.repositories.TierListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody String addTierList(@RequestParam String title,
                                            @RequestParam String subject,
                                            @RequestParam(required = false) String weekStartDate) {
        // if weekStartDate isn't null or an empty string convert it to a LocalDate Object
        // else make startDate the current date
        LocalDate startDate = (weekStartDate != null && !weekStartDate.isEmpty())
                ? LocalDate.parse(weekStartDate)
                : LocalDate.now();

        // create the new Tier List for insertion
        TierList newTierList = new TierList(title, subject, startDate);
        tierListRepository.save(newTierList);
        return "Tier List saved";
    }


    @DeleteMapping(path = "/delete") // Delete by ID
    public @ResponseBody String deleteTierListByID(@RequestParam Integer id) {
        if (!tierListRepository.existsById(id)) {
            return "Tier List does not exist";
        }

        tierListRepository.deleteById(id);
        return "Tier List deleted successfully";
    }

    // Another way to do it with a path parameter
    // Just experimenting with different ways to use mapping
    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody String deleteTierListByPathId(@PathVariable Integer id) {
        if (!tierListRepository.existsById(id)) {
            return "Tier List does not exist";
        }

        tierListRepository.deleteById(id);
        return "Tier List deleted successfully";
    }

    // GET all TierLists
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<TierList> getAllTierLists() {
        return tierListRepository.findAll();
    }

    // Get by ID using path variable
    @GetMapping("/{id}")
    public TierList getTierListById(@PathVariable Integer id) {
        return tierListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tier List not found with id: " + id));
    }





}







