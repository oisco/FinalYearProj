package com.example.Entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Oisín on 10/22/2016.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedNativeQueries(
        @NamedNativeQuery(name = "Fighter.findAllToDisplay", query = "select id,first_name,last_name,thumbnail from Fighter order by first_name ASC"))
@JsonAutoDetect

public class Fighter {

    @Id
    private int id;


    private String first_name,last_name,rank,pound_for_pound_rank;
    private String weight_class,nickname,left_full_body_image,thumbnail;
    private int wins,losses,draws,koWins,koLosses,submissionWins,submissionLosses,DecisionWins, DecisionLosses;

    private int reach,height;

    @ManyToMany(fetch = FetchType.EAGER,targetEntity=Matchup.class,cascade = CascadeType.DETACH)
    public List<Matchup> matchups;

    @JsonIgnore
    @ManyToOne(targetEntity = Prediction.class,fetch = FetchType.LAZY)
    private List<Prediction> predictions;

    public Fighter(int id, String first_name, String last_name, String rank, String pound_for_pound_rank, String weight_class, String nickname, String left_full_body_image, String thumbnail, int wins, int losses, int draws, int koWins, int koLosses, int submissionWins, int submissionLosses, int decisionWins, int decisionLosses, List<Matchup> matchups, List<Prediction> predictions) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.rank = rank;
        this.pound_for_pound_rank = pound_for_pound_rank;
        this.weight_class = weight_class;
        this.nickname = nickname;
        this.left_full_body_image = left_full_body_image;
        this.thumbnail = thumbnail;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.koWins = koWins;
        this.koLosses = koLosses;
        this.submissionWins = submissionWins;
        this.submissionLosses = submissionLosses;
        DecisionWins = decisionWins;
        DecisionLosses = decisionLosses;
        this.matchups = matchups;
        this.predictions = predictions;
    }

    public Fighter(){

    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getKoWins() {
        return koWins;
    }

    public void setKoWins(int koWins) {
        this.koWins = koWins;
    }

    public int getKoLosses() {
        return koLosses;
    }

    public void setKoLosses(int koLosses) {
        this.koLosses = koLosses;
    }

    public int getSubmissionWins() {
        return submissionWins;
    }

    public void setSubmissionWins(int submissionWins) {
        this.submissionWins = submissionWins;
    }

    public int getSubmissionLosses() {
        return submissionLosses;
    }

    public void setSubmissionLosses(int submissionLosses) {
        this.submissionLosses = submissionLosses;
    }

    public int getDecisionWins() {
        return DecisionWins;
    }

    public void setDecisionWins(int decisionWins) {
        DecisionWins = decisionWins;
    }

    public int getDecisionLosses() {
        return DecisionLosses;
    }

    public void setDecisionLosses(int decisionLosses) {
        DecisionLosses = decisionLosses;
    }

    public String getLeft_full_body_image() {
        return left_full_body_image;
    }

    public void setLeft_full_body_image(String left_full_body_image) {
        this.left_full_body_image = left_full_body_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPound_for_pound_rank() {
        return pound_for_pound_rank;
    }

    public void setPound_for_pound_rank(String pound_for_pound_rank) {
        this.pound_for_pound_rank = pound_for_pound_rank;
    }

    public String getWeight_class() {
        return weight_class;
    }

    public void setWeight_class(String weight_class) {
        this.weight_class = weight_class;
    }



    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public List<Matchup> getMatchups() {
        return matchups;
    }

    public void setMatchups(List<Matchup> matchups) {
        this.matchups = matchups;
    }

    public void addMatchup(Matchup matchup) {
        this.matchups.add(matchup);
    }
}
