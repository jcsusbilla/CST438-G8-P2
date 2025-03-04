package com.example.rest_service.database.controllers;

import com.example.rest_service.database.repositories.UserRepository;
import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.User;
import com.example.rest_service.database.entities.UserTierList;
import com.example.rest_service.database.repositories.TierListRepository;
import com.example.rest_service.database.repositories.UserTierListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
@RequestMapping(path = "/userTierLists")
public class UserTierListController {

    // Most likely need access to all repositories for any advanced queries.
    // Need at least 3 for normal CRUD

    @Autowired
    private UserTierListRepository userTierListRepository;
    @Autowired
    private UserRepository databaseRepository;
    @Autowired
    private TierListRepository tierListRepository;

    // CREATE MAPPING
    // Right now I want to see how we could use the http sessions to autopopulate userId with a signed in userID
    @PostMapping(path = "addTierList")
    public @ResponseBody String createUserTierList(@RequestParam Integer userId,
                                                   @RequestParam Integer tierId) {

        //first we need to get the userId and tier ID
        Optional<User> user = databaseRepository.findById(userId);
        Optional<TierList> tierList = tierListRepository.findById(tierId);


        // All the conditions this could fail (except an identical userId/tierID entry)
        // Probably need a helper function to check the exception missing from this logic
        if (user.isEmpty() && tierList.isEmpty()) {
            return "User and Tier List not found.";
        } else if (user.isEmpty()) {
            return "User not found with ID: " + userId;
        } else if (tierList.isEmpty()) {
            return "Tier List not found with ID: " + tierId;
        }


        // If it doesn't fail the above conditions add it
        UserTierList newUserTierList = new UserTierList();
        newUserTierList.setUser(user.get());
        newUserTierList.setTierList(tierList.get());


        // save the newUserTierList and return
        userTierListRepository.save(newUserTierList);
        return "UserTierList created successfully.";

    }

    // DELETE MAPPING by userID
   @DeleteMapping(path= "deleteTierList/{id}")
    public @ResponseBody String deleteUserById (@PathVariable Integer id) {
        Optional<UserTierList> userTierList = userTierListRepository.findById(id);

        if (userTierList.isPresent()) {
            userTierListRepository.delete(userTierList.get());
            return "UserTierList deleted successfully.";
        }

        else{
            throw new RuntimeException("Tier List not found with ID: " + id);
        }
   }
   


}
