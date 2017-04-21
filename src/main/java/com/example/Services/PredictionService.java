package com.example.Services;

import com.example.DAO.LearningCurveResultRepository;
import com.example.DAO.PredictionRepository;
import com.example.Entity.LearningCurveResult;
import com.example.Entity.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import com.example.EntityWrappers.PredictionWinner;

/**
 * Created by Oisín on 1/30/2017.
 */
@Service
public class PredictionService {
    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private LearningCurveResultRepository learningCurveRepository;

    public PredictionService(PredictionRepository predictionRepository){
        this.predictionRepository=predictionRepository;
    }

    //returns ids of predicted winners for future matchups for a particular event
    public List<Integer> getEventPredictions(int eventId) {

        List<Integer> result =  predictionRepository.getEventPredictions(eventId);
        return result;
    }

//all predictions which were made based on test sets from cross validation testing
//    public List<Object[]> getAllPredictions() {
//        return predictionRepository.getAllPredictions();
//    }

    public List<Prediction> getAllPredictions() {
        return predictionRepository.getAllPredictions();
    }

    public List<LearningCurveResult> findLearningCurve() {
       return learningCurveRepository.findLearningCurveValues();
    }
}
