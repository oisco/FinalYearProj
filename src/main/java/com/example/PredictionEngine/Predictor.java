package com.example.PredictionEngine;

import com.example.DAO.PredictionRepository;
import com.example.DAO.ResultRepository;
import com.example.Entity.Fighter;
import com.example.Entity.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Oisín on 1/24/2017.
 */
@Service
public class Predictor {
    @Autowired
    PredictionRepository predictionRepository;

    public Predictor(){

    }

    public Prediction predict(Fighter f1,Fighter f2){

        return compareRecords(f1,f2);
        //query and check for fighter finishes
    }

    //find each fighters win %
    public Prediction compareRecords(Fighter f1,Fighter f2){
        Fighter winner=null;

        //check for undefeated records(if both fighters are undefeated favour the one with more UFC fights)
        if (f1.getLosses()==0) {
            if (f2.getLosses() == 0) {
                return mostExperienced(f1,f2);
            }
            else {
                winner= f1;
            }
        }
        else if (f2.getLosses()==0)
        {
            if (f1.getLosses() == 0) {
                return mostExperienced(f1,f2);
            }else {
                winner= f2;
            }
        }
        else {
            double f1TotalFights= f1.getWins()+f1.getLosses();
            double f2TotalFights=f2.getWins()+f2.getLosses();

            double fighter1WinPct=f1.getWins()/(f1TotalFights/100);
            double fighter2WinPct=f2.getWins()/(f2TotalFights/100);
            if(fighter1WinPct>fighter2WinPct){
                winner= f1;
            }else {
                winner= f2;
            }
        }

        //create Prediction object persist and return
        Prediction p=new Prediction();
        p.setWinner(winner);
        return p;

    }

    private Prediction mostExperienced(Fighter f1, Fighter f2) {
        Prediction p=new Prediction();
        if(f1.getMatchups().size()>f2.getMatchups().size()){
            p.setWinner(f1);
        }
        else if(f2.getMatchups().size()>f1.getMatchups().size()){
            p.setWinner(f2);
        }
        return p;
    }

}
