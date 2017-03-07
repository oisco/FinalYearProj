package com.example.MachineLearning;

import com.example.Entity.Matchup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Oisín on 2/16/2017.
 */
//@Entity
public class Inputs {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
    int totalFights;
    double winPct;
    int matchupId;
    int fighter1height;
    int fighter1reach;
    String weightClass;
    double fighter1_strikingaccuracy;
    double fighter1_sapm;
    double fighter1_slpm;
    double fighter1_strikingdefense;
    double fighter1_takedownaverage;
    double fighter1_takedownaccuracy;
    double fighter1_takedowndefense;
    double fighter1_submissionsaverage;
    int numberOfUfcFights;
    int numberOfUfcWins;
    int numberOfUfcLosses;
    

    //define win/loss 1/0
    int clas;

    public Inputs(int matchupId,int totalFights, double winPct, int fighter1height, int fighter1reach, String weightClass, double fighter1_strikingaccuracy, double fighter1_sapm, double fighter1_slpm, double fighter1_strikingdefense, double fighter1_takedownaverage, double fighter1_takedownaccuracy, double fighter1_takedowndefense, double fighter1_submissionsaverage, int numberOfUfcFights, int numberOfUfcWins, int numberOfUfcLosses, int clas) {
        this.totalFights = totalFights;
        this.winPct = winPct;
        this.matchupId = matchupId;
        this.fighter1height = fighter1height;
        this.fighter1reach = fighter1reach;
        this.weightClass = weightClass;
        this.fighter1_strikingaccuracy = fighter1_strikingaccuracy;
        this.fighter1_sapm = fighter1_sapm;
        this.fighter1_slpm = fighter1_slpm;
        this.fighter1_strikingdefense = fighter1_strikingdefense;
        this.fighter1_takedownaverage = fighter1_takedownaverage;
        this.fighter1_takedownaccuracy = fighter1_takedownaccuracy;
        this.fighter1_takedowndefense = fighter1_takedowndefense;
        this.fighter1_submissionsaverage = fighter1_submissionsaverage;
        this.numberOfUfcFights = numberOfUfcFights;
        this.numberOfUfcWins = numberOfUfcWins;
        this.numberOfUfcLosses = numberOfUfcLosses;
        this.clas = clas;
    }

    public int getMatchupId() {
        return matchupId;
    }

    public void setMatchupId(int matchupId) {
        this.matchupId = matchupId;
    }

    public int getNumberOfUfcWins() {
        return numberOfUfcWins;
    }

    public void setNumberOfUfcWins(int numberOfUfcWins) {
        this.numberOfUfcWins = numberOfUfcWins;
    }

    public int getNumberOfUfcLosses() {
        return numberOfUfcLosses;
    }

    public void setNumberOfUfcLosses(int numberOfUfcLosses) {
        this.numberOfUfcLosses = numberOfUfcLosses;
    }

    public int getNumberOfUfcFights() {
        return numberOfUfcFights;
    }

    public void setNumberOfUfcFights(int numberOfUfcFights) {
        this.numberOfUfcFights = numberOfUfcFights;
    }

    public int getClas() {
        return clas;
    }

    public void setClas(int clas) {
        this.clas = clas;
    }

    public int getTotalFights() {
        return totalFights;
    }

    public void setTotalFights(int totalFights) {
        this.totalFights = totalFights;
    }

    public double getWinPct() {
        return winPct;
    }

    public void setWinPct(double winPct) {
        this.winPct = winPct;
    }

    public int getFighter1height() {
        return fighter1height;
    }

    public void setFighter1height(int fighter1height) {
        this.fighter1height = fighter1height;
    }

    public int getFighter1reach() {
        return fighter1reach;
    }

    public void setFighter1reach(int fighter1reach) {
        this.fighter1reach = fighter1reach;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public double getFighter1_strikingaccuracy() {
        return fighter1_strikingaccuracy;
    }

    public void setFighter1_strikingaccuracy(double fighter1_strikingaccuracy) {
        this.fighter1_strikingaccuracy = fighter1_strikingaccuracy;
    }

    public double getFighter1_sapm() {
        return fighter1_sapm;
    }

    public void setFighter1_sapm(double fighter1_sapm) {
        this.fighter1_sapm = fighter1_sapm;
    }

    public double getFighter1_slpm() {
        return fighter1_slpm;
    }

    public void setFighter1_slpm(double fighter1_slpm) {
        this.fighter1_slpm = fighter1_slpm;
    }

    public double getFighter1_strikingdefense() {
        return fighter1_strikingdefense;
    }

    public void setFighter1_strikingdefense(double fighter1_strikingdefense) {
        this.fighter1_strikingdefense = fighter1_strikingdefense;
    }

    public double getFighter1_takedownaverage() {
        return fighter1_takedownaverage;
    }

    public void setFighter1_takedownaverage(double fighter1_takedownaverage) {
        this.fighter1_takedownaverage = fighter1_takedownaverage;
    }

    public double getFighter1_takedownaccuracy() {
        return fighter1_takedownaccuracy;
    }

    public void setFighter1_takedownaccuracy(double fighter1_takedownaccuracy) {
        this.fighter1_takedownaccuracy = fighter1_takedownaccuracy;
    }

    public double getFighter1_takedowndefense() {
        return fighter1_takedowndefense;
    }

    public void setFighter1_takedowndefense(double fighter1_takedowndefense) {
        this.fighter1_takedowndefense = fighter1_takedowndefense;
    }

    public double getFighter1_submissionsaverage() {
        return fighter1_submissionsaverage;
    }

    public void setFighter1_submissionsaverage(double fighter1_submissionsaverage) {
        this.fighter1_submissionsaverage = fighter1_submissionsaverage;
    }
}


