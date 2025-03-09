package com.example.rest_service.database.repositories;

import com.example.rest_service.database.entities.UserTierList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserTierListRepository extends CrudRepository<UserTierList, Integer> {

    List<UserTierList> findByUserId(Integer userId);
}
