package com.example.rest_service.database.repositories;
import com.example.rest_service.database.entities.TierRanking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TierRankingRepository extends CrudRepository<TierRanking, Integer> {

    List<TierRanking> findByTierListId(Integer id);
}
