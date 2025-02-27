package com.example.rest_service.database;
import com.example.rest_service.database.entities.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// This allows us to initialize an instance of our database
// Should I create a repository for each entity or store them all in one?
// And I was reading up on how to do CRUD operations in spring and need to look into Services
public interface DatabaseRepository extends CrudRepository<User, Integer> {

}
