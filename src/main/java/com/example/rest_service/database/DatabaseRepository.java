package com.example.rest_service.database;
import com.example.rest_service.database.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// This allows us to initialize an instance of our database

public interface DatabaseRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
