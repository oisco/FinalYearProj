package com.example.DAO;

import com.example.Entity.FoldResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Oisín on 3/26/2017.
 */
@Repository
public interface FoldResultRepository extends JpaRepository<FoldResult,Integer> {
}
