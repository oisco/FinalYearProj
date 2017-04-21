package com.example.DAO;

import com.example.Entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//import com.example.EntityWrappers.PredictionWinner;

/**
 * Created by Oisín on 1/24/2017.
 */
@Repository
public interface PredictionRepository extends JpaRepository<Prediction,Integer>{


    public List<Integer> getEventPredictions(int eventId);
    public List<Prediction> getAllPredictions();
}
