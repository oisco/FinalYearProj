package com.example.Entity;

//import com.example.EntityWrappers.PredictionWinner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.sym.Name;

import javax.persistence.*;

/**
 * Created by Oisín on 1/24/2017.
 */
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Prediction.getAllPredictions",
                query = "SELECT m.fighter1_profile_image,m.fighter1_first_name,m.fighter1_last_name,m.fighter2_profile_image,m.fighter2_first_name,m.fighter2_last_name,p.is_correct,m.date,f.last_name\n" +
                "FROM \n" +
                "matchup m, prediction p,fighter f\n" +
                "WHERE\n" +
                "m.id=p.matchup_id" +
                        " and p.winner_id=f.id" +
                        " and m.date<now();"),
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


    boolean isCorrect;


    public Prediction(){

    }

    public Prediction(Matchup matchup, Fighter winner, boolean isCorrect) {
        this.matchup = matchup;
        this.winner = winner;
        this.isCorrect = isCorrect;
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

