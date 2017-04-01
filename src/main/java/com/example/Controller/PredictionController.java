package com.example.Controller;

//import com.example.DAO.PredictionRepository;
//import com.example.DAO.PredictionRepository;
import com.example.DAO.FoldResultRepository;
import com.example.Entity.FoldResult;
import com.example.Entity.LearningCurveResult;
import com.example.Entity.Matchup;
import com.example.Entity.Prediction;
//import com.example.EntityWrappers.PredictionWinner;
import com.example.Services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Oisín on 1/24/2017.
 */
@RestController
@RequestMapping("/predictions")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private FoldResultRepository foldResultRepository;

    public PredictionController(PredictionService predictionService){
        this.predictionService=predictionService;
    }

    //get all predictions for a specific event
    @RequestMapping(value = "/event/{eventId}",method = RequestMethod.GET)
    public List<Integer> view(@PathVariable int eventId) {
        return predictionService.getEventPredictions(eventId);
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Object[]> findAll() {
        return predictionService.getAllPredictions();
    }

    //gets the cross validaiton results on the full data set
    @RequestMapping(value = "/folds",method = RequestMethod.GET)
    public List<FoldResult> getFoldTestingResults() {
        return foldResultRepository.findAll();
    }

    @RequestMapping(value = "/learningCurve",method = RequestMethod.GET)
    public List<LearningCurveResult> getLearningCurve() {
        return predictionService.findLearningCurve();
    }



}
