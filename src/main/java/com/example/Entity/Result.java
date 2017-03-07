package com.example.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.sym.Name;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

/**
 * Created by Oisín on 1/20/2017.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String Method;

    private String EndingRound;

    private String EndingTime;

    String Submission;


    public int fighter2StrikesAttempted;
    public int fighter2StrikesLanded;
    public int fighter1StrikesAttempted;
    public int fighter1StrikesLanded;

    //knockdowns
    int fighter1KnockdownsLanded;
    int fighter2KnockdownsLanded;

    ////////grappling/////
    int fighter1takedownsLanded;
    int fighter2takedownsLanded;
    int fighter1takedownsAttempted;
    int fighter2takedownsAttempted;
    int fighter1submissionsAttempted;
    int fighter2submissionsAttempted;
    int fighter1standupsLanded;
    int fighter2standupsLanded;

    /////control time////////
    String fighter1standingTime;
    String fighter2standingTime;
    String fighter1groundTime;
    String fighter2groundTime;
    String fighter1groundControlTime;
    String fighter2groundControlTime;

    boolean isValid;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "result")
    private Matchup matchup;

    public Result(){}

    public Result(String method, String endingRound, String endingTime, String submission, int fighter2StrikesAttempted, int fighter2StrikesLanded, int fighter1StrikesAttempted, int fighter1StrikesLanded, int fighter1KnockdownsLanded, int fighter2KnockdownsLanded, int fighter1takedownsLanded, int fighter2takedownsLanded, int fighter1takedownsAttempted, int fighter2takedownsAttempted, int fighter1submissionsAttempted, int fighter2submissionsAttempted, int fighter1standupsLanded, int fighter2standupsLanded, String fighter1standingTime, String fighter2standingTime, String fighter1groundTime, String fighter2groundTime, String fighter1groundControlTime, String fighter2groundControlTime, boolean isValid, Matchup matchup) {
        Method = method;
        EndingRound = endingRound;
        EndingTime = endingTime;
        Submission = submission;
        this.fighter2StrikesAttempted = fighter2StrikesAttempted;
        this.fighter2StrikesLanded = fighter2StrikesLanded;
        this.fighter1StrikesAttempted = fighter1StrikesAttempted;
        this.fighter1StrikesLanded = fighter1StrikesLanded;
        this.fighter1KnockdownsLanded = fighter1KnockdownsLanded;
        this.fighter2KnockdownsLanded = fighter2KnockdownsLanded;
        this.fighter1takedownsLanded = fighter1takedownsLanded;
        this.fighter2takedownsLanded = fighter2takedownsLanded;
        this.fighter1takedownsAttempted = fighter1takedownsAttempted;
        this.fighter2takedownsAttempted = fighter2takedownsAttempted;
        this.fighter1submissionsAttempted = fighter1submissionsAttempted;
        this.fighter2submissionsAttempted = fighter2submissionsAttempted;
        this.fighter1standupsLanded = fighter1standupsLanded;
        this.fighter2standupsLanded = fighter2standupsLanded;
        this.fighter1standingTime = fighter1standingTime;
        this.fighter2standingTime = fighter2standingTime;
        this.fighter1groundTime = fighter1groundTime;
        this.fighter2groundTime = fighter2groundTime;
        this.fighter1groundControlTime = fighter1groundControlTime;
        this.fighter2groundControlTime = fighter2groundControlTime;
        this.isValid = isValid;
        this.matchup = matchup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getEndingRound() {
        return EndingRound;
    }

    public void setEndingRound(String endingRound) {
        EndingRound = endingRound;
    }

    public String getEndingTime() {
        return EndingTime;
    }

    public void setEndingTime(String endingTime) {
        EndingTime = endingTime;
    }

    public String getSubmission() {
        return Submission;
    }

    public void setSubmission(String submission) {
        this.Submission = submission;
    }

    public Matchup getMatchup() {
        return matchup;
    }

    public void setMatchup(Matchup matchup) {
        this.matchup = matchup;
    }

    public int getFighter2StrikesAttempted() {
        return fighter2StrikesAttempted;
    }

    public void setFighter2StrikesAttempted(int fighter2StrikesAttempted) {
        this.fighter2StrikesAttempted = fighter2StrikesAttempted;
    }

    public int getFighter2StrikesLanded() {
        return fighter2StrikesLanded;
    }

    public void setFighter2StrikesLanded(int fighter2StrikesLanded) {
        this.fighter2StrikesLanded = fighter2StrikesLanded;
    }

    public int getFighter1StrikesAttempted() {
        return fighter1StrikesAttempted;
    }

    public void setFighter1StrikesAttempted(int fighter1StrikesAttempted) {
        this.fighter1StrikesAttempted = fighter1StrikesAttempted;
    }

    public int getFighter1StrikesLanded() {
        return fighter1StrikesLanded;
    }

    public void setFighter1StrikesLanded(int fighter1StrikesLanded) {
        this.fighter1StrikesLanded = fighter1StrikesLanded;
    }

    public int getFighter1KnockdownsLanded() {
        return fighter1KnockdownsLanded;
    }

    public void setFighter1KnockdownsLanded(int fighter1KnockdownsLanded) {
        this.fighter1KnockdownsLanded = fighter1KnockdownsLanded;
    }

    public int getFighter2KnockdownsLanded() {
        return fighter2KnockdownsLanded;
    }

    public void setFighter2KnockdownsLanded(int fighter2KnockdownsLanded) {
        this.fighter2KnockdownsLanded = fighter2KnockdownsLanded;
    }

    public int getFighter1takedownsLanded() {
        return fighter1takedownsLanded;
    }

    public void setFighter1takedownsLanded(int fighter1takedownsLanded) {
        this.fighter1takedownsLanded = fighter1takedownsLanded;
    }

    public int getFighter2takedownsLanded() {
        return fighter2takedownsLanded;
    }

    public void setFighter2takedownsLanded(int fighter2takedownsLanded) {
        this.fighter2takedownsLanded = fighter2takedownsLanded;
    }

    public int getFighter1takedownsAttempted() {
        return fighter1takedownsAttempted;
    }

    public void setFighter1takedownsAttempted(int fighter1takedownsAttempted) {
        this.fighter1takedownsAttempted = fighter1takedownsAttempted;
    }

    public int getFighter2takedownsAttempted() {
        return fighter2takedownsAttempted;
    }

    public void setFighter2takedownsAttempted(int fighter2takedownsAttempted) {
        this.fighter2takedownsAttempted = fighter2takedownsAttempted;
    }

    public int getFighter1submissionsAttempted() {
        return fighter1submissionsAttempted;
    }

    public void setFighter1submissionsAttempted(int fighter1submissionsAttempted) {
        this.fighter1submissionsAttempted = fighter1submissionsAttempted;
    }

    public int getFighter2submissionsAttempted() {
        return fighter2submissionsAttempted;
    }

    public void setFighter2submissionsAttempted(int fighter2submissionsAttempted) {
        this.fighter2submissionsAttempted = fighter2submissionsAttempted;
    }

    public int getFighter1standupsLanded() {
        return fighter1standupsLanded;
    }

    public void setFighter1standupsLanded(int fighter1standupsLanded) {
        this.fighter1standupsLanded = fighter1standupsLanded;
    }

    public int getFighter2standupsLanded() {
        return fighter2standupsLanded;
    }

    public void setFighter2standupsLanded(int fighter2standupsLanded) {
        this.fighter2standupsLanded = fighter2standupsLanded;
    }

    public String getFighter1standingTime() {
        return fighter1standingTime;
    }

    public void setFighter1standingTime(String fighter1standingTime) {
        this.fighter1standingTime = fighter1standingTime;
    }

    public String getFighter2standingTime() {
        return fighter2standingTime;
    }

    public void setFighter2standingTime(String fighter2standingTime) {
        this.fighter2standingTime = fighter2standingTime;
    }

    public String getFighter1groundTime() {
        return fighter1groundTime;
    }

    public void setFighter1groundTime(String fighter1groundTime) {
        this.fighter1groundTime = fighter1groundTime;
    }

    public String getFighter2groundTime() {
        return fighter2groundTime;
    }

    public void setFighter2groundTime(String fighter2groundTime) {
        this.fighter2groundTime = fighter2groundTime;
    }

    public String getFighter1groundControlTime() {
        return fighter1groundControlTime;
    }

    public void setFighter1groundControlTime(String fighter1groundControlTime) {
        this.fighter1groundControlTime = fighter1groundControlTime;
    }

    public String getFighter2groundControlTime() {
        return fighter2groundControlTime;
    }

    public void setFighter2groundControlTime(String fighter2groundControlTime) {
        this.fighter2groundControlTime = fighter2groundControlTime;
    }


    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
}