package com.example.DAO;

import com.example.Entity.TestingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


/**
 * Created by Oisín on 3/26/2017.
 */
@Repository
public interface TestingResultRepository extends JpaRepository<TestingResult,Integer> {

    @Transactional
    @Modifying
    void getRidOfFoldResult(String dtype);

}
