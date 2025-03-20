package com.example.rest_service.database.controllers;
import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.User;
import com.example.rest_service.database.repositories.TierListRepository;
import com.example.rest_service.database.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.rest_service.database.controllers.TierListRequest;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/tierlists")
//@CrossOrigin(origins = "*") // ✅ Allows requests from frontend
public class TierListController {

    private final TierListRepository tierListRepository;
    private final UserRepository userRepository;

    public TierListController(TierListRepository tierListRepository, UserRepository userRepository) {
        this.tierListRepository = tierListRepository;
        this.userRepository = userRepository;
    }

    // ✅ CREATE a new TierList
    @PostMapping("/add")
    public ResponseEntity<?> createTierList(@RequestBody TierListRequest request) {
        if (request.getTitle() == null || request.getSubject() == null || request.getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        User user = userOptional.get();
        TierList newTierList = new TierList(request.getTitle(), request.getSubject(), user);
        tierListRepository.save(newTierList);

        return ResponseEntity.ok(Map.of("message", "Tier list created successfully", "tierListId", newTierList.getId()));
    }

    
}

//package com.example.rest_service.database.controllers;
//import com.example.rest_service.database.entities.TierList;
//import com.example.rest_service.database.repositories.TierListRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;// maybe need this if we decide to pivot from ResponseBody
//import org.springframework.web.bind.annotation.*;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/tierlists")
//public class TierListController {
//
//    @Autowired
//    private TierListRepository tierListRepository;
//
//
//    // CREATE
//    // Note that tier list doesn't know anything about the rankings its basically a template with all the metadata for the tierRankings
//
//    /**
//     * This allows a TierList to be added to the table
//     * Required Parameters ar String: title and String: subject
//     * <p>
//     * Optional Parameter: String: weekStartDate
//     * If there is a given date convert it into localDate object
//     * Else use the current date as a default
//     **/
//    @PostMapping(path = "/add") // Map ONLY POST Requests
//    public @ResponseBody String addTierList(@RequestParam String title,
//                                            @RequestParam String subject,
//                                            @RequestParam(required = false) String weekStartDate) {
//        // if weekStartDate isn't null or an empty string convert it to a LocalDate Object
//        // else make startDate the current date
//        LocalDate startDate = (weekStartDate != null && !weekStartDate.isEmpty())
//                ? LocalDate.parse(weekStartDate)
//                : LocalDate.now();
//
//        // create the new Tier List for insertion
//        TierList newTierList = new TierList(title, subject, startDate);
//        tierListRepository.save(newTierList);
//        return "Tier List saved";
//    }
//
//
//    // DELETE by id  with request Param
//    @DeleteMapping(path = "/delete")
//    public @ResponseBody String deleteTierListByID(@RequestParam Integer id) {
//        if (!tierListRepository.existsById(id)) {
//            return "Tier List does not exist";
//        }
//
//        tierListRepository.deleteById(id);
//        return "Tier List deleted successfully";
//    }
//
//    // DELETE by id with PathVariable (i think this is the better way)
//    @DeleteMapping(path = "/delete/{id}")
//    public @ResponseBody String deleteTierListByPathId(@PathVariable Integer id) {
//        if (!tierListRepository.existsById(id)) {
//            return "Tier List does not exist";
//        }
//
//        tierListRepository.deleteById(id);
//        return "Tier List deleted successfully";
//    }
//
//
//    //UPDATE
//    /**
//     * This mapping finds the Tierlist you want to update by id
//     * There are three optional parameters to modify here
//     *  String: title
//     *  String: subject
//     *  String: date
//     *
//     *  Any or none of these fields may be modified
//     * */
//    @PutMapping(path = "update/{id}")
//    public @ResponseBody String updateTierList(@PathVariable Integer id,
//                                               @RequestParam(required = false) String title,
//                                               @RequestParam(required = false) String subject,
//                                               @RequestParam(required = false) String date) {
//
//        // This checks to see if the tierList id exists. if not optional is empty
//        Optional<TierList> existingTierList = tierListRepository.findById(id);
//
//        if (existingTierList.isPresent()) {
//            TierList tierList = existingTierList.get();
//
//            // keeps track of whether or not an update has been made
//            boolean update = false;
//
//            // Update only if parameter is given in request
//            if (title != null && !title.isEmpty()) {
//                tierList.setTitle(title);
//                update = true;
//            }
//            if (subject != null && !subject.isEmpty()) {
//                tierList.setSubject(subject);
//                update = true;
//            }
//            if (date != null && !date.isEmpty()) {
//                tierList.setWeekStartDate(LocalDate.parse(date));
//                update = true;
//            }
//
//            if (!update) {
//                return "Update Failed: No parameters selected for change!";
//            }
//
//
//
//            // Save after the update
//            tierListRepository.save(tierList);
//
//            return "Tier List updated successfully.";
//        } else {
//            throw new RuntimeException("Tier List not found with ID: " + id);
//        }
//    }
//
//
//    ///////////////////// GET REQUESTS ///////////////////////////////////////////////
//
//    // GET all TierLists
//    @GetMapping("/all")
//    public ResponseEntity<List<TierList>> getAllTierLists() {
//        List<TierList> tierLists = (List<TierList>) tierListRepository.findAll();
//        return ResponseEntity.ok(tierLists);
//    }
//
//    // Get by ID using path variable
//    @GetMapping("/{id}")
//    public TierList getTierListById(@PathVariable Integer id) {
//        return tierListRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Tier List not found with id: " + id));
//    }
//
//    // Get TierList by Title (NEEDS TESTING)
//    @GetMapping(path = "title/{title}")
//    public @ResponseBody TierList getTierListByTitle(@PathVariable String title) {
//
//        // optional allows for null checks
//        // if tierList exists then optional contains it
//        // if it doesnt exist optional is empty
//        Optional<TierList> tierList = tierListRepository.findByTitle(title);
//
//        if (tierList.isPresent()) {
//
//            return tierList.get();
//        } else {
//            throw new RuntimeException("Tier List not found with title: " + title);
//        }
//    }
//
//    // Get TierList by Subject (NEEDS TESTING)
//    @GetMapping(path = "subject/{subject}")
//    public @ResponseBody TierList getTierListBySubject(@PathVariable String subject) {
//        Optional<TierList> tierList = tierListRepository.findBySubject(subject);
//
//        if (tierList.isPresent()) {
//            return tierList.get();
//        } else {
//            throw new RuntimeException("Tier List not found with subject: " + subject);
//        }
//    }
//
//    // Should probably add one to get by date as well
//
//
//    ///////////////////// GET REQUESTS END //////////////////////////////////////////
//
//
//}
//
//
//
//
//
//
//
