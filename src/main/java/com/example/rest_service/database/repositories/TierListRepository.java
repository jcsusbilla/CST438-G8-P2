package com.example.rest_service.database.repositories;
import com.example.rest_service.database.entities.TierList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TierListRepository extends CrudRepository<TierList, Integer> {

    Optional<TierList> findByTitle(String title);

    Optional<TierList> findBySubject(String subject);

    TierList getReferenceById(Integer tierId);
}
