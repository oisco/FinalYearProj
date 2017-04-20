package com.example.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/28/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Matchup.findFightersToUpdate",query = "select fighter1_id from matchup where event_id=?1 union select fighter2_id from matchup where event_id=?1"),

        //below 2 queries can more than likely be a select *
        ///the below query will return the record,height and reach of all status and loser in each matchup,
        @NamedNativeQuery(name = "Matchup.findMLClass1Inputs",
                query = "select m.id as matchupId,m.fighter1height,m.fighter1reach,m.fighter1record,m.fighter1_weight_class,\n" +
                        "m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm" +
                        " ,m.fighter1_strikingdefense,m.fighter1_takedownaverage, \n" +
                        "m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct, \n" +
                        "m.fighter2height,m.fighter2reach,m.fighter2record,\n" +
                        "m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm" +
                        " ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                        ",m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct,m.fighter1_id as currentFighter \n" +
                        "from matchup m\n" +
                        "where m.fighter1_is_winner\n" +
                        "and m.fighter1reach>0  and m.fighter1height>0 and m.fighter1record!=\"\" "+
                        "and m.fighter2reach>0  and m.fighter2height>0 and m.fighter2record!=\"\" "+
                        "and m.status='valid'" +
//                        "and m.date<now()-INTERVAL  ?1 month" +
                        " union \n" +
                        "select m.id,m.fighter2height,m.fighter2reach,m.fighter2record,m.fighter2_weight_class,\n" +
                        " m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                        ",m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct ,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct\n" +
                        ",m.fighter1height,m.fighter1reach,m.fighter1record,\n" +
                        "m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm ,m.fighter1_strikingdefense,m.fighter1_takedownaverage\n" +
                        ",m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct ,m.fighter2_id as currentFighter\n" +
                        "from matchup m\n" +
                        "where m.fighter2_is_winner\n" +
                        "and m.status='valid'" +
                        "and m.fighter2reach>0  and m.fighter2height>0   and m.fighter2record!=\"\" "+
                        "and m.fighter1reach>0  and m.fighter1height>0   and m.fighter1record!=\"\" " +
//                        "and m.date<now()-INTERVAL ?1 month" +
                        "  order by matchupId  ;")

        //the same as above but this time the losers of all matchups will appear first
        ,@NamedNativeQuery(name = "Matchup.findMLClass0Inputs",
                query = "select m.id as matchupId,m.fighter1height,m.fighter1reach,m.fighter1record,m.fighter1_weight_class,\n" +
                        "m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm ,m.fighter1_strikingdefense,m.fighter1_takedownaverage\n" +
                        ",m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct \n" +
                         ",m.fighter2height,m.fighter2reach,m.fighter2record,\n" +
                        "m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                        ",m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct, m.fighter1_id as currentFighter \n" +
                        "from matchup m\n" +
                        "where m.fighter2_is_winner\n" +
                        " and m.status='valid'" +
                        " and m.fighter1reach>0  and m.fighter1height>0   and m.fighter1record!=\"\""+
                        "and m.fighter2reach>0  and m.fighter2height>0   and m.fighter2record!=\"\""+
                        "union \n" +
                        "select m.id,m.fighter2height,m.fighter2reach,m.fighter2record,m.fighter2_weight_class,\n" +
                        "m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                        ",m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct\n" +
                        ",m.fighter1height,m.fighter1reach,m.fighter1record \n" +
                        ",m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm ,m.fighter1_strikingdefense,m.fighter1_takedownaverage\n" +
                        ",m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct, m.fighter2_id as currentFighter \n" +
                        "from matchup m\n" +
                        "where m.fighter1_is_winner\n" +
                        "and m.status='valid'" +
                        " and m.fighter2reach>0  and m.fighter2height>0   and m.fighter2record!=\"\" "+
                        "and m.fighter1reach>0  and m.fighter1height>0   and m.fighter1record!=\"\" order by matchupId ;")
        ,
        @NamedNativeQuery(name = "Matchup.findFutureMatchupsToPredict",query = "select m.id as matchupId,m.fighter1height,m.fighter1reach,m.fighter1record,m.fighter1_weight_class,\n" +
                "                m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm\n" +
                "                 ,m.fighter1_strikingdefense,m.fighter1_takedownaverage, \n" +
                "                m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct, \n" +
                "                m.fighter2height,m.fighter2reach,m.fighter2record,\n" +
                "                m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm\n" +
                "                 ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                "                ,m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct,m.fighter1_id as currentFighter \n" +
                "                from matchup m\n" +
                "                where \n" +
                "                  m.date>now()\n" +
                "                and m.status='valid'\n" +
                "                 union \n" +
                "                select m.id,m.fighter2height,m.fighter2reach,m.fighter2record,m.fighter2_weight_class,\n" +
                "                 m.fighter2_strikingaccuracy,m.fighter2_sapm ,m.fighter2_slpm ,m.fighter2_strikingdefense,m.fighter2_takedownaverage\n" +
                "                ,m.fighter2_takedownaccuracy,m.fighter2_takedowndefense,m.fighter2_submissionsaverage,m.fighter2ufcfinish_pct ,m.fighter2ufcwin_pct,m.fighter2ufcloss_pct\n" +
                "                ,m.fighter1height,m.fighter1reach,m.fighter1record,\n" +
                "                m.fighter1_strikingaccuracy,m.fighter1_sapm ,m.fighter1_slpm ,m.fighter1_strikingdefense,m.fighter1_takedownaverage\n" +
                "                ,m.fighter1_takedownaccuracy,m.fighter1_takedowndefense,m.fighter1_submissionsaverage,m.fighter1ufcfinish_pct,m.fighter1ufcwin_pct,m.fighter1ufcloss_pct ,m.fighter2_id as currentFighter\n" +
                "                from matchup m\n" +
                "                where m.status='valid'\n" +
                "                and m.date>now()\n" +
                "                  order by matchupId  ;"),
        @NamedNativeQuery(name = "Matchup.findPastFightersMatchupStats",
        query="select r.ending_time\n" +
                "        ,r.fighter1knockdowns_landed,r.fighter1strikes_attempted,r.fighter1strikes_landed,r.fighter1ground_control_time,r.fighter1ground_time\n" +
                ",r.fighter1standing_time,r.fighter1standups_landed,r.fighter1submissions_attempted,r.fighter1takedowns_attempted,r.fighter1takedowns_landed,r.fighter2knockdowns_landed\n" +
                ",r.fighter2strikes_attempted,r.fighter2strikes_landed,r.fighter2ground_control_time,r.fighter2ground_time,r.fighter2standing_time\n" +
                ",r.fighter2standups_landed,r.fighter2submissions_attempted,r.fighter2takedowns_attempted,r.fighter2takedowns_landed,r.ending_round,m.date as date\n" +
                "from matchup m,result r\n" +
                "where m.fighter1_id=?1\n" +
                "and m.date< ?2 \n" +
                "and m.result_id=r.id\n" +
                "and r.is_valid \n" +
                "union\n" +
                "select r.ending_time,r.fighter2knockdowns_landed\n" +
                ",r.fighter2strikes_attempted,r.fighter2strikes_landed,r.fighter2ground_control_time,r.fighter2ground_time,r.fighter2standing_time,r.fighter2standups_landed,\n" +
                "r.fighter2submissions_attempted,r.fighter2takedowns_attempted,r.fighter2takedowns_landed,r.fighter1knockdowns_landed,r.fighter1strikes_attempted,r.fighter1strikes_landed,\n" +
                "r.fighter1ground_control_time,r.fighter1ground_time,r.fighter1standing_time,r.fighter1standups_landed," +
                "r.fighter1submissions_attempted,r.fighter1takedowns_attempted,r.fighter1takedowns_landed,r.ending_round ,m.date as date\n" +
                "from matchup m,result r\n" +
                "where m.fighter2_id=?1\n" +
                "and m.date< ?2\n" +
                "and m.result_id=r.id\n" +
                "and r.is_valid " +
                "order by date limit 3;"),
        @NamedNativeQuery(name = "Matchup.findNoOfPastUfcFinishes",
                query = "select count(*) from matchup m,result r where \n" +
                        "m.result_id=r.id\n" +
                        "and \n" +
                        "(r.method LIKE '%KO%' OR r.method LIKE '%Submission%')\n" +
                        "and \n" +
                        "((m.fighter1_id=?1 and m.fighter1_is_winner and m.date <?2) \n" +
                        "or\n" +
                        "(m.fighter2_id=?1 and m.fighter2_is_winner and m.date <?2));;")

        , @NamedNativeQuery(name = "Matchup.fightersNoOfWins",
        query="select count(*) from matchup where \n" +
                "(fighter1_id=?1 and fighter1_is_winner and date <?2)\n" +
                "or\n" +
                "(fighter2_id=?1 and fighter2_is_winner and date <?2);"),

        @NamedNativeQuery(name = "Matchup.fightersNoOfLosses",
                query="select count(*) from matchup where \n" +
                    "(fighter2_id=?1 and fighter1_is_winner and date <?2)\n" +
                        "or\n" +
                        "(fighter1_id=?1 and fighter2_is_winner and date <?2);"),

        @NamedNativeQuery(name = "Matchup.findUpcomingToDelete",query = "select * from matchup where date>now();")
})

public class Matchup {
    @Id
    private int id;

    private boolean fighter1_is_winner,fighter2_is_winner;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.DETACH)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToMany(mappedBy = "matchups", fetch = FetchType.LAZY)
    private List<Fighter> fighters;

    @JsonProperty("result")
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private Result result;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,cascade =CascadeType.REMOVE )
    private Prediction prediction;

    private int fighter1_id;
    private int fighter2_id;

    private double fighter1UFCFinishPct,fighter2UFCFinishPct;
    private double fighter1UFCWinPct,fighter2UFCWinPct;
    private double fighter1UFCLossPct,fighter2UFCLossPct;

    public String fighter1_first_name, fighter2_first_name,fighter2_last_name, fighter1_last_name;
    public String fighter1_profile_image,fighter2_profile_image;

    String fighter2_weight_class,fighter1_weight_class;
    //below is used for machine learning inputs
    int fighter1reach,fighter2reach;
    int fighter1height,fighter2height;

    double fighter2_strikingaccuracy;
    double fighter1_strikingaccuracy;
    double fighter1_sapm ,fighter2_sapm ;
    double fighter1_slpm ,fighter2_slpm ;

    double fighter1_strikingdefense,fighter2_strikingdefense;
    double fighter2_takedownaverage,fighter1_takedownaverage;
    double fighter1_takedownaccuracy,fighter2_takedownaccuracy;
    double fighter1_takedowndefense,fighter2_takedowndefense;

    double fighter1_submissionsaverage,fighter2_submissionsaverage;

    boolean fighter1IsActive,fighter2IsActive;
    public String weightClass;
    public Date date;
    public String status;

    String fm_stats_feed_url;

    //reference to each fighters record at the time of going into the matchup (used for predicting past events)
    public String fighter1record, fighter2record;


    public Matchup(int id, boolean fighter1_is_winner, boolean fighter2_is_winner, Event event, List<Fighter> fighters, Result result, Prediction prediction, int fighter1_id, int fighter2_id, double fighter1UFCFinishPct, double fighter2UFCFinishPct, double fighter1UFCWinPct, double fighter2UFCWinPct, double fighter1UFCLossPct, double fighter2UFCLossPct, String fighter1_first_name, String fighter2_first_name, String fighter2_last_name, String fighter1_last_name, String fighter1_profile_image, String fighter2_profile_image, String fighter2_weight_class, String fighter1_weight_class, int fighter1reach, int fighter2reach, int fighter1height, int fighter2height, double fighter2_strikingaccuracy, double fighter1_strikingaccuracy, double fighter1_sapm, double fighter2_sapm, double fighter1_slpm, double fighter2_slpm, double fighter1_strikingdefense, double fighter2_strikingdefense, double fighter2_takedownaverage, double fighter1_takedownaverage, double fighter1_takedownaccuracy, double fighter2_takedownaccuracy, double fighter1_takedowndefense, double fighter2_takedowndefense, double fighter1_submissionsaverage, double fighter2_submissionsaverage, boolean fighter1IsActive, boolean fighter2IsActive, String weightClass, Date date, String status, String fm_stats_feed_url, String fighter1record, String fighter2record) {
        this.id = id;
        this.fighter1_is_winner = fighter1_is_winner;
        this.fighter2_is_winner = fighter2_is_winner;
        this.event = event;
        this.fighters = fighters;
        this.result = result;
        this.prediction = prediction;
        this.fighter1_id = fighter1_id;
        this.fighter2_id = fighter2_id;
        this.fighter1UFCFinishPct = fighter1UFCFinishPct;
        this.fighter2UFCFinishPct = fighter2UFCFinishPct;
        this.fighter1UFCWinPct = fighter1UFCWinPct;
        this.fighter2UFCWinPct = fighter2UFCWinPct;
        this.fighter1UFCLossPct = fighter1UFCLossPct;
        this.fighter2UFCLossPct = fighter2UFCLossPct;
        this.fighter1_first_name = fighter1_first_name;
        this.fighter2_first_name = fighter2_first_name;
        this.fighter2_last_name = fighter2_last_name;
        this.fighter1_last_name = fighter1_last_name;
        this.fighter1_profile_image = fighter1_profile_image;
        this.fighter2_profile_image = fighter2_profile_image;
        this.fighter2_weight_class = fighter2_weight_class;
        this.fighter1_weight_class = fighter1_weight_class;
        this.fighter1reach = fighter1reach;
        this.fighter2reach = fighter2reach;
        this.fighter1height = fighter1height;
        this.fighter2height = fighter2height;
        this.fighter2_strikingaccuracy = fighter2_strikingaccuracy;
        this.fighter1_strikingaccuracy = fighter1_strikingaccuracy;
        this.fighter1_sapm = fighter1_sapm;
        this.fighter2_sapm = fighter2_sapm;
        this.fighter1_slpm = fighter1_slpm;
        this.fighter2_slpm = fighter2_slpm;
        this.fighter1_strikingdefense = fighter1_strikingdefense;
        this.fighter2_strikingdefense = fighter2_strikingdefense;
        this.fighter2_takedownaverage = fighter2_takedownaverage;
        this.fighter1_takedownaverage = fighter1_takedownaverage;
        this.fighter1_takedownaccuracy = fighter1_takedownaccuracy;
        this.fighter2_takedownaccuracy = fighter2_takedownaccuracy;
        this.fighter1_takedowndefense = fighter1_takedowndefense;
        this.fighter2_takedowndefense = fighter2_takedowndefense;
        this.fighter1_submissionsaverage = fighter1_submissionsaverage;
        this.fighter2_submissionsaverage = fighter2_submissionsaverage;
        this.fighter1IsActive = fighter1IsActive;
        this.fighter2IsActive = fighter2IsActive;
        this.weightClass = weightClass;
        this.date = date;
        this.status = status;
        this.fm_stats_feed_url = fm_stats_feed_url;
        this.fighter1record = fighter1record;
        this.fighter2record = fighter2record;
    }

    public double getfighter1UFCFinishPct() {
        return fighter1UFCFinishPct;
    }

    public void setfighter1UFCFinishPct(double fighter1UFCFinishPct) {
        this.fighter1UFCFinishPct = fighter1UFCFinishPct;
    }

    public double getfighter1UFCWinPct() {
        return fighter1UFCWinPct;
    }

    public void setfighter1UFCWinPct(double fighter1UFCWinPct) {
        this.fighter1UFCWinPct = fighter1UFCWinPct;
    }

    public double getFighter2UfcWinPct() {
        return fighter1UFCWinPct;
    }

    public void setfighter2UFCWinPct(double fighter2UFCWinPct) {
        this.fighter2UFCWinPct = fighter2UFCWinPct;
    }

    public double getfighter1UFCLossPct() {
        return fighter1UFCLossPct;
    }

    public void setfighter1UFCLossPct(double fighter1UFCLossPct) {
        this.fighter1UFCLossPct = fighter1UFCLossPct;
    }

    public double getfighter2UFCLossPct() {
        return fighter2UFCLossPct;
    }

    public void setfighter2UFCLossPct(double fighter2UFCLossPct) {
        this.fighter2UFCLossPct = fighter2UFCLossPct;
    }

    public double getfighter2UFCFinishPct() {
        return fighter2UFCFinishPct;
    }

    public void setfighter2UFCFinishPct(double fighter2UFCFinishPct) {
        this.fighter2UFCFinishPct = fighter2UFCFinishPct;
    }

    public Matchup() {

    }

    public String getFighter2_weight_class() {
        return fighter2_weight_class;
    }

    public void setFighter2_weight_class(String fighter2_weight_class) {
        this.fighter2_weight_class = fighter2_weight_class;
    }

    public String getFighter1_weight_class() {
        return fighter1_weight_class;
    }

    public void setFighter1_weight_class(String fighter1_weight_class) {
        this.fighter1_weight_class = fighter1_weight_class;
    }

    public double getFighter1_slpm() {
        return fighter1_slpm;
    }

    public void setFighter1_slpm(double fighter1_slpm) {
        this.fighter1_slpm = fighter1_slpm;
    }

    public double getFighter2_slpm() {
        return fighter2_slpm;
    }

    public void setFighter2_slpm(double fighter2_slpm) {
        this.fighter2_slpm = fighter2_slpm;
    }

    public String getFighter1record() {
        return fighter1record;
    }

    public void setFighter1record(String fighter1record) {
        this.fighter1record = fighter1record;
    }

    public String getFighter2record() {
        return fighter2record;
    }

    public void setFighter2record(String fighter2record) {
        this.fighter2record = fighter2record;
    }

    public int getFighter1reach() {
        return fighter1reach;
    }

    public void setFighter1reach(int fighter1reach) {
        this.fighter1reach = fighter1reach;
    }

    public int getFighter2reach() {
        return fighter2reach;
    }

    public void setFighter2reach(int fighter2reach) {
        this.fighter2reach = fighter2reach;
    }

    public int getFighter1height() {
        return fighter1height;
    }

    public void setFighter1height(int fighter1height) {
        this.fighter1height = fighter1height;
    }

    public int getFighter2height() {
        return fighter2height;
    }

    public void setFighter2height(int fighter2height) {
        this.fighter2height = fighter2height;
    }

    public String getFm_stats_feed_url() {
        return fm_stats_feed_url;
    }

    public void setFm_stats_feed_url(String fm_stats_feed_url) {
        this.fm_stats_feed_url = fm_stats_feed_url;
    }

    public String getFighter1_profile_image() {
        return fighter1_profile_image;
    }

    public void setFighter1_profile_image(String fighter1_profile_image) {
        this.fighter1_profile_image = fighter1_profile_image;
    }

    public String getFighter2_profile_image() {
        return fighter2_profile_image;
    }

    public void setFighter2_profile_image(String fighter2_profile_image) {
        this.fighter2_profile_image = fighter2_profile_image;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFighter1_is_winner() {
        return fighter1_is_winner;
    }

    public void setFighter1_is_winner(boolean fighter1_is_winner) {
        this.fighter1_is_winner = fighter1_is_winner;
    }

    public boolean isFighter2_is_winner() {
        return fighter2_is_winner;
    }

    public void setFighter2_is_winner(boolean fighter2_is_winner) {
        this.fighter2_is_winner = fighter2_is_winner;
    }

    public int getFighter2_id() {
        return fighter2_id;
    }

    public void setFighter2_id(int fighter2_id) {
        this.fighter2_id = fighter2_id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setFighters(List<Fighter> fighters) {
        this.fighters = fighters;
    }

    public int getFighter1_id() {
        return fighter1_id;
    }

    public void setFighter1_id(int fighter1_id) {
        this.fighter1_id = fighter1_id;
    }


    public String getFighter1_first_name() {
        return fighter1_first_name;
    }

    public void setFighter1_first_name(String fighter1_first_name) {
        this.fighter1_first_name = fighter1_first_name;
    }

    public String getFighter2_first_name() {
        return fighter2_first_name;
    }

    public void setFighter2_first_name(String fighter2_first_name) {
        this.fighter2_first_name = fighter2_first_name;
    }

    public String getFighter2_last_name() {
        return fighter2_last_name;
    }

    public void setFighter2_last_name(String fighter2_last_name) {
        this.fighter2_last_name = fighter2_last_name;
    }

    public String getFighter1_last_name() {
        return fighter1_last_name;
    }

    public void setFighter1_last_name(String fighter1_last_name) {
        this.fighter1_last_name = fighter1_last_name;
    }

    public String getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(String weightClass) {
        this.weightClass = weightClass;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public double getFighter2_strikingaccuracy() {
        return fighter2_strikingaccuracy;
    }

    public void setFighter2_strikingaccuracy(double fighter2_strikingaccuracy) {
        this.fighter2_strikingaccuracy = fighter2_strikingaccuracy;
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

    public double getFighter2_sapm() {
        return fighter2_sapm;
    }

    public void setFighter2_sapm(double fighter2_sapm) {
        this.fighter2_sapm = fighter2_sapm;
    }

    public double getFighter1_strikingdefense() {
        return fighter1_strikingdefense;
    }

    public void setFighter1_strikingdefense(double fighter1_strikingdefense) {
        this.fighter1_strikingdefense = fighter1_strikingdefense;
    }

    public double getFighter2_strikingdefense() {
        return fighter2_strikingdefense;
    }

    public void setFighter2_strikingdefense(double fighter2_strikingdefense) {
        this.fighter2_strikingdefense = fighter2_strikingdefense;
    }

    public double getFighter2_takedownaverage() {
        return fighter2_takedownaverage;
    }

    public void setFighter2_takedownaverage(double fighter2_takedownaverage) {
        this.fighter2_takedownaverage = fighter2_takedownaverage;
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

    public double getFighter2_takedownaccuracy() {
        return fighter2_takedownaccuracy;
    }

    public void setFighter2_takedownaccuracy(double fighter2_takedownaccuracy) {
        this.fighter2_takedownaccuracy = fighter2_takedownaccuracy;
    }

    public double getFighter1_takedowndefense() {
        return fighter1_takedowndefense;
    }

    public void setFighter1_takedowndefense(double fighter1_takedowndefense) {
        this.fighter1_takedowndefense = fighter1_takedowndefense;
    }

    public double getFighter2_takedowndefense() {
        return fighter2_takedowndefense;
    }

    public void setFighter2_takedowndefense(double fighter2_takedowndefense) {
        this.fighter2_takedowndefense = fighter2_takedowndefense;
    }

    public double getFighter1_submissionsaverage() {
        return fighter1_submissionsaverage;
    }

    public void setFighter1_submissionsaverage(double fighter1_submissionsaverage) {
        this.fighter1_submissionsaverage = fighter1_submissionsaverage;
    }

    public double getFighter2_submissionsaverage() {
        return fighter2_submissionsaverage;
    }

    public void setFighter2_submissionsaverage(double fighter2_submissionsaverage) {
        this.fighter2_submissionsaverage = fighter2_submissionsaverage;
    }


    public boolean isFighter1IsActive() {
        return fighter1IsActive;
    }

    public void setFighter1IsActive(boolean fighter1IsActive) {
        this.fighter1IsActive = fighter1IsActive;
    }

    public boolean isFighter2IsActive() {
        return fighter2IsActive;
    }

    public void setFighter2IsActive(boolean fighter2IsActive) {
        this.fighter2IsActive = fighter2IsActive;
    }
}