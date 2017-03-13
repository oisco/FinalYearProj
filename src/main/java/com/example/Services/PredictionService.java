package com.example.Services;

import com.example.DAO.PredictionRepository;
import com.example.Entity.Prediction;
//import com.example.EntityWrappers.PredictionWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Oisín on 1/30/2017.
 */
@Service
public class PredictionService {
    @Autowired
    private PredictionRepository predictionRepository;


    public PredictionService(PredictionRepository predictionRepository){
        this.predictionRepository=predictionRepository;
    }


    public List<Integer> getEventPredictions(int eventId) {
        List<Integer> result =  predictionRepository.getEventPredictions(eventId);
        return result;
    }

    public List<Prediction> findAll() {
    return predictionRepository.findAll();
    }

    public List<Object[]> getAllPredictions() {
        return predictionRepository.getAllPredictions();
    }
}
