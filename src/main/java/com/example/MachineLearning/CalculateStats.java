package com.example.MachineLearning;

import com.example.DAO.FighterRepository;
import com.example.DAO.MatchupRepository;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Oisín on 2/23/2017.
 */
//used to prepare inputs for machine learning mlp
@Service
public class CalculateStats {
    @Autowired
    MatchupRepository matchupRepository;

    @Autowired
    FighterRepository fighterRepository;
    public CalculateStats(){}

    //iterates through every matchup and updates each fighters stats to be that of the time of going into the matchup
    public void getFighterStatsAtTimeOfMatchup() {
        //go through every matchup
        List<Matchup> matchups=matchupRepository.findAll();
        for (int i=0;i<matchups.size();i++) {
            //UPDATE THE STATS FOR EACH FIGHTER IN THE MATCHUP
            int currentFighter=1;
            for (currentFighter=1;currentFighter<3;currentFighter++){
                ///stats to calculate for each fighter
                int careerFightTime=0;
                int careerKnockdownsLanded=0,careerStrikesAttempted=0,careerStrikesLanded=0,careerGroundControlTime=0,careerGroundTime=0;
                int careerStanding_time=0,careerStandups_landed=0,careerSubmissionsAttempted=0,careerTakedownsAttempted=0,careerTakedownsLanded=0;
                int careerKnockdownsRecieved=0,careerStrikesOpponentsAttempted=0,careerStrikesRecieved=0;
                int careerGroundTimeBeingControlled=0;
                int careerTakedownsOpponentsAttempted=0,careerTakedownsRecieved=0;
                double strikingDefense=0,strikingAccuracy=0,strikesAbsorbedPerMinute=0,strikesLandedPerMinute=0;
                double takedownAccuracy=0,takedownDefense=0,takedownAverage=0, submissionAverage=0;

                int numOfUfcFinishes = 0;
                int numOfUfcWins = 0;
                int numOfUfcLosses = 0;

                //past fights which contain info such as strikes landed
                int validNumberOfUfcFights=0;
                List<Object[]> fightersPastMatchups;
                if(currentFighter==1){//if the current fighter is listed in the matchup as fighter1
                    //query below returns the number of  past wins, wins by finishing their opponent and
//                    number of losses which are in the system (these are based on their UFC fights stored in the database not their career record)
                    numOfUfcFinishes=matchupRepository.findNoOfPastUfcFinishes(matchups.get(i).getFighter1_id(), matchups.get(i).getDate());//finishes
                    numOfUfcLosses = matchupRepository.fightersNoOfLosses(matchups.get(i).getFighter1_id(), matchups.get(i).getDate());
                    numOfUfcWins =matchupRepository.fightersNoOfWins(matchups.get(i).getFighter1_id(), matchups.get(i).getDate());
                    //finish pct
                    if(numOfUfcLosses>0 || numOfUfcWins>0){
                        //below actually sets the number of wins , losses, finished NOT percentage
                        matchups.get(i).setfighter1UFCFinishPct(numOfUfcFinishes);
                        matchups.get(i).setfighter1UFCWinPct(numOfUfcWins);
                        matchups.get(i).setfighter1UFCLossPct(numOfUfcLosses);
                    }

                    //the below query returns a list of the current fighters past fights
                    fightersPastMatchups = matchupRepository.findPastFightersMatchupStats(matchups.get(i).getFighter1_id(), matchups.get(i).getDate());
                }
                else {//if the current fighter we are dealing with is listed as fighter 2 in the matchup, then repeat the above but only using fighter 2 from the matchup
                    numOfUfcWins = matchupRepository.fightersNoOfWins(matchups.get(i).getFighter2_id(), matchups.get(i).getDate());
                    numOfUfcLosses = matchupRepository.fightersNoOfLosses(matchups.get(i).getFighter2_id(), matchups.get(i).getDate());
                    numOfUfcFinishes=matchupRepository.findNoOfPastUfcFinishes(matchups.get(i).getFighter2_id(), matchups.get(i).getDate());

                    if(numOfUfcLosses>0 || numOfUfcWins>0) {
                        matchups.get(i).setfighter2UFCFinishPct(numOfUfcFinishes);
                        matchups.get(i).setfighter2UFCWinPct(numOfUfcWins);
                        matchups.get(i).setfighter2UFCLossPct(numOfUfcLosses);
                    }

                    fightersPastMatchups = matchupRepository.findPastFightersMatchupStats(matchups.get(i).getFighter2_id(), matchups.get(i).getDate());
                }

                //update stats for fighter 1

                //for every past Matchup in the system involving the current fighter,
                for (int p = 0; p < fightersPastMatchups.size(); p++) {
                    validNumberOfUfcFights++;
                    ///the above query returns a list of
                    // current fighter we are dealing with is fighter 1 , his/her  past opponent is fighter2
                    int fighter1knockdowns_landed = Integer.parseInt(fightersPastMatchups.get(p)[1].toString());
                    int fighter1strikes_attempted = Integer.parseInt(fightersPastMatchups.get(p)[2].toString());
                    int fighter1strikes_landed = Integer.parseInt(fightersPastMatchups.get(p)[3].toString());
                    int fighter1submissions_attempted = Integer.parseInt(fightersPastMatchups.get(p)[8].toString());
                    int fighter1takedowns_attempted = Integer.parseInt(fightersPastMatchups.get(p)[9].toString());
                    int fighter1takedowns_landed = Integer.parseInt(fightersPastMatchups.get(p)[10].toString());
                    int fighter2knockdowns_landed = Integer.parseInt(fightersPastMatchups.get(p)[11].toString());
                    int fighter2strikes_attempted = Integer.parseInt(fightersPastMatchups.get(p)[12].toString());
                    int fighter2strikes_landed = Integer.parseInt(fightersPastMatchups.get(p)[13].toString());
                    int fighter2submissions_attempted = Integer.parseInt(fightersPastMatchups.get(p)[18].toString());
                    int fighter2takedowns_attempted = Integer.parseInt(fightersPastMatchups.get(p)[19].toString());
                    int fighter2takedowns_landed = Integer.parseInt(fightersPastMatchups.get(p)[20].toString());
                    int endingRound = Integer.parseInt(fightersPastMatchups.get(p)[21].toString());

                    int ending_time = ((endingRound - 1) * 300) + inSeconds(fightersPastMatchups.get(p)[0].toString()); //ending round+seconds in round
                    careerFightTime += ending_time;
                    careerStrikesAttempted += fighter1strikes_attempted;
                    careerStrikesLanded += fighter1strikes_landed;

                    careerStrikesRecieved += fighter2strikes_landed;
                    careerStrikesOpponentsAttempted += fighter2strikes_attempted;
                    careerKnockdownsLanded += fighter1knockdowns_landed;
                    careerKnockdownsRecieved += fighter2knockdowns_landed;

                    careerTakedownsAttempted += fighter1takedowns_attempted;
                    careerTakedownsLanded += fighter1takedowns_landed;
                    careerTakedownsOpponentsAttempted += fighter2takedowns_attempted;
                    careerTakedownsRecieved += fighter2takedowns_landed;
                    careerSubmissionsAttempted += fighter1submissions_attempted;
                }
                ///calculate overall career stats(going into that matchup)
                if(validNumberOfUfcFights>0) {
                    strikingDefense = asPercentage(careerStrikesOpponentsAttempted, careerStrikesOpponentsAttempted - careerStrikesRecieved);
                    strikingAccuracy = asPercentage(careerStrikesAttempted, careerStrikesLanded);

                    strikesAbsorbedPerMinute = getPerMinute(careerFightTime, careerStrikesRecieved);
                    strikesLandedPerMinute = getPerMinute(careerFightTime, careerStrikesLanded);
                    takedownAccuracy = asPercentage(careerTakedownsAttempted, careerTakedownsLanded);
                    takedownDefense = asPercentage(careerTakedownsOpponentsAttempted, careerTakedownsOpponentsAttempted - careerTakedownsRecieved);


                    //average submissions and takedowns per 15 min fight
                    takedownAverage = getAverage(careerFightTime, careerTakedownsLanded);
                    submissionAverage = getAverage(careerFightTime, careerSubmissionsAttempted);
                    if (currentFighter == 1) {
                        
                        //try and get the current fighters height and reach if available
                        if(matchups.get(i).getFighter1height()==0 || matchups.get(i).getFighter1reach()==0){
                            Fighter fighter=fighterRepository.findOne(matchups.get(i).getFighter1_id());
                            if(fighter!=null){
                                matchups.get(i).setFighter1height(fighter.getHeight());
                                matchups.get(i).setFighter1height(fighter.getReach());
                            }
                        }

                        //update the matchup
                        matchups.get(i).setFighter1_takedownaverage(takedownAverage);
                        matchups.get(i).setFighter1_submissionsaverage(submissionAverage);
                        //striking
                        matchups.get(i).setFighter1_sapm(strikesAbsorbedPerMinute);
                        matchups.get(i).setFighter1_slpm(strikesLandedPerMinute);
                        if(careerStrikesAttempted>0){
                            matchups.get(i).setFighter1_strikingaccuracy(strikingAccuracy);
                        }
                        if(careerStrikesOpponentsAttempted>0){
                            matchups.get(i).setFighter1_strikingdefense(strikingDefense);
                        }
                        //grappling
                        if(careerTakedownsAttempted>0){
                            matchups.get(i).setFighter1_takedownaccuracy(takedownAccuracy);
                        }
                        if(careerTakedownsOpponentsAttempted>0){
                            matchups.get(i).setFighter1_takedowndefense(takedownDefense);
                        }
                        //need to change
                    } else if (currentFighter == 2) {
                        //update the matchup
                        //try and get the current fighters height and reach if available
                        if(matchups.get(i).getFighter2height()==0 || matchups.get(i).getFighter2reach()==0){
                            Fighter fighter=fighterRepository.findOne(matchups.get(i).getFighter2_id());
                            if(fighter!=null){
                                matchups.get(i).setFighter2height(fighter.getHeight());
                                matchups.get(i).setFighter2height(fighter.getReach());
                            }
                        }

                        matchups.get(i).setFighter2_takedownaverage(takedownAverage);
                        matchups.get(i).setFighter2_submissionsaverage(submissionAverage);
                        //striking
                        matchups.get(i).setFighter2_sapm(strikesAbsorbedPerMinute);
                        matchups.get(i).setFighter2_slpm(strikesLandedPerMinute);
                        if(careerStrikesAttempted>0){
                            matchups.get(i).setFighter2_strikingaccuracy(strikingAccuracy);
                        }
                        if(careerStrikesOpponentsAttempted>0){
                            matchups.get(i).setFighter2_strikingdefense(strikingDefense);
                        }
                        //grappling
                        if(careerTakedownsAttempted>0){
                            matchups.get(i).setFighter2_takedownaccuracy(takedownAccuracy);
                        }
                        if(careerTakedownsOpponentsAttempted>0){
                            matchups.get(i).setFighter2_takedowndefense(takedownDefense);
                        }

                        //the below means this matchup is valid for predicting/training;
                        matchups.get(i).setStatus("valid");
                        matchupRepository.save(matchups.get(i));
                    }
                }
            }
        }
        System.out.println("stats calculated");
    }


    public double getPerMinute(int timeInSeconds,int val){
        return val/(timeInSeconds/60.0f);
    }

    public double asPercentage(int overall,int val){
        return ((val * 100.0f) / overall);
    }

    public int inSeconds(String time){
        String[] minSec=time.split(":");
        return ((Integer.parseInt(minSec[0])*60)+(Integer.parseInt(minSec[1])));

    }

    //submission and takedown average per 15mins
    public double getAverage(int timeInSeconds,int val){
            return val/(timeInSeconds/900.0f);
    }
    
    

}
