package com.example.DAO;

import com.example.Entity.LearningCurveResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Oisín on 3/26/2017.
 */
@Repository
public interface LearningCurveResultRepository extends JpaRepository<LearningCurveResult,Integer> {
    List<LearningCurveResult> findLearningCurveValues();
}
