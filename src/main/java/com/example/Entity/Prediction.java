package com.example.Entity;

//import com.example.EntityWrappers.PredictionWinner;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Oisín on 1/24/2017.
 */
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Prediction.getAllPredictions",
                query = "SELECT p.* \n" +
                "FROM \n" +
                "matchup m, prediction p "+
                "WHERE\n" +
                "m.id=p.matchup_id" +
                        " and m.date<now();", resultClass = Prediction.class),
@NamedNativeQuery(name = "Prediction.getEventPredictions",
        query = "select p.winner_id from prediction p,matchup m,fighter f where p.matchup_id=m.id and m.event_id=?1 and p.winner_id=f.id ORDER BY p.winner_id ASC;")
})
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonIgnore
    @OneToOne(targetEntity = Matchup.class,fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    private Matchup matchup;

    @JsonIgnore
    @ManyToOne(targetEntity = Fighter.class,fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    private Fighter winner;

    String label;
    String predictedWinnerName;

    boolean isCorrect;


    public Prediction(){

    }

    //extend to add labels for upcoming
    public Prediction(Matchup matchup, Fighter winner, boolean isCorrect) {
        this.matchup = matchup;
        this.winner = winner;
        this.isCorrect = isCorrect;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPredictedWinnerName() {
        return predictedWinnerName;
    }

    public void setPredictedWinnerName(String predictedWinnerName) {
        this.predictedWinnerName = predictedWinnerName;
    }

    public Matchup getMatchup() {
        return matchup;
    }

    public void setMatchup(Matchup matchup) {
        this.matchup = matchup;
    }

    public Fighter getWinner() {
        return winner;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setWinner(Fighter winner) {
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

