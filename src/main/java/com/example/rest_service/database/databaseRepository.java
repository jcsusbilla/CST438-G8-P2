package com.example.rest_service.database;
import com.example.rest_service.database.entities.User;
import com.example.rest_service.database.entities.TierList;
import com.example.rest_service.database.entities.TierList;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// This allows us to initialize an instance of our database
public interface databaseRepository extends CrudRepository<User, Integer> {

}
