package com.example.MachineLearning;

import com.example.DAO.MatchupRepository;
import com.example.Entity.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oisín on 2/15/2017.
 */

@Service
public class PrepareInputs {
    @Autowired
    MatchupRepository matchupRepository;

    PrepareInputs(){}

    //
    public List<Inputs> getClass0Inputs(){
        //look at first half
//        int toEvaluate=matchupRepository.findByStatus("valid").size()/2;
//        List<Object[]> losers=matchupRepository.findMLClass0Inputs(toEvaluate);
     List<Object[]> losers=matchupRepository.findMLClass0Inputs();
        return listGetClassStats(losers,0);
    }

    ///class 1 (winners)--> status reach,record and height in comparison to their opponents
    public List<Inputs> getClass1Inputs(){
        //look at second half
//        int toEvaluate=matchupRepository.findByStatus("valid").size()/2;
//        List<Object[]> winners=matchupRepository.findMLClass1Inputs(toEvaluate,toEvaluate*2);
        List<Object[]> winners=matchupRepository.findMLClass1Inputs();
        return listGetClassStats(winners,1);
    }



    private double getFighterWinPct(String s) {
        int[] winsAndTotalFights=getTotalFightsAndWins(s);
        int wins=winsAndTotalFights[1];
        double totalFights=winsAndTotalFights[0];
        double winpct=wins/(totalFights/100);
        return winpct;
    }

    public int[] getTotalFightsAndWins(String s){
        //get wins ,losses and draws from record
        //get wins ,losses and draws from record
        s=s.replace(',','-');
        String[] wld = s.split("-");
        int wins = Integer.parseInt(wld[0]);
        int losses = Integer.parseInt(wld[1]);
        int draws=0;
        //fighter might not have any draws
        if (wld.length>2){
            draws=Integer.parseInt(wld[2]);
        }

        //find wins as a % of total fights
        int[] totalfights=new int[]{(wins+losses+draws),wins};
        return totalfights;
    }

public ArrayList<Inputs> listGetClassStats(List<Object[]> matchups, int clas){
    //find reach advantage/disadvantage
    ArrayList<Inputs> inputs=new ArrayList<>();
    for (int i=0;i<matchups.size();i++){

        int matchupId=Integer.parseInt(matchups.get(i)[0].toString());
        int fighter1height=Integer.parseInt(matchups.get(i)[1].toString());
        int fighter1reach=Integer.parseInt(matchups.get(i)[2].toString());
        String fighter1record=matchups.get(i)[3].toString();
        String weightClass=matchups.get(i)[4].toString();
        double fighter1_strikingaccuracy=Double.parseDouble(matchups.get(i)[5].toString());
        double fighter1_sapm=Double.parseDouble(matchups.get(i)[6].toString());
        double fighter1_slpm=Double.parseDouble(matchups.get(i)[7].toString());
        double fighter1_strikingdefense=Double.parseDouble(matchups.get(i)[8].toString());
        double fighter1_takedownaverage =Double.parseDouble(matchups.get(i)[9].toString());
        double fighter1_takedownaccuracy=Double.parseDouble(matchups.get(i)[10].toString());
        double fighter1_takedowndefense=Double.parseDouble(matchups.get(i)[11].toString());
        double fighter1_submissionsaverage=Double.parseDouble(matchups.get(i)[12].toString());
        int fighter1_numberOfUfcFights=Integer.parseInt(matchups.get(i)[13].toString());
        double fighterwinPct=getFighterWinPct(fighter1record);
        int totalFights=getTotalFightsAndWins(fighter1record)[0];

        int fighter2height=Integer.parseInt(matchups.get(i)[14].toString());
        int fighter2reach=Integer.parseInt(matchups.get(i)[15].toString());
        String fighter2record=matchups.get(i)[16].toString();
        double fighter2_strikingaccuracy=Double.parseDouble(matchups.get(i)[18].toString());
        double fighter2_sapm=Double.parseDouble(matchups.get(i)[19].toString());
        double fighter2_slpm=Double.parseDouble(matchups.get(i)[20].toString());
        double fighter2_strikingdefense=Double.parseDouble(matchups.get(i)[21].toString());
        double fighter2_takedownaverage =Double.parseDouble(matchups.get(i)[22].toString());
        double fighter2_takedownaccuracy=Double.parseDouble(matchups.get(i)[23].toString());
        double fighter2_takedowndefense=Double.parseDouble(matchups.get(i)[24].toString());
        double fighter2_submissionsaverage=Double.parseDouble(matchups.get(i)[25].toString());
        int fighter2_numberOfUfcFights=Integer.parseInt(matchups.get(i)[26].toString());
        double fighter2winPct=getFighterWinPct(fighter2record);
        int totalFights2=getTotalFightsAndWins(fighter2record)[0];

        totalFights =totalFights-totalFights2;
        fighterwinPct=fighterwinPct-fighter2winPct;
         fighter1height=fighter1height-fighter2height;
          fighter1reach=fighter1reach-fighter2reach;
         fighter1_strikingaccuracy=fighter1_strikingaccuracy-fighter2_strikingaccuracy;
         fighter1_sapm=fighter1_sapm-fighter2_sapm;
         fighter1_slpm=fighter1_slpm-fighter2_slpm;
         fighter1_strikingdefense=fighter1_strikingdefense-fighter2_strikingdefense;
         fighter1_takedownaverage=fighter1_takedownaverage-fighter2_takedownaverage;
         fighter1_takedownaccuracy=fighter1_takedownaccuracy-fighter2_takedownaccuracy;
         fighter1_takedowndefense=fighter1_takedowndefense-fighter2_takedowndefense;
         fighter1_submissionsaverage=fighter1_submissionsaverage-fighter2_submissionsaverage;
        fighter1_numberOfUfcFights=fighter1_numberOfUfcFights-fighter2_numberOfUfcFights;

        Inputs in=new Inputs(matchupId,totalFights,fighterwinPct, fighter1height,  fighter1reach, weightClass, fighter1_strikingaccuracy, fighter1_sapm, fighter1_slpm, fighter1_strikingdefense, fighter1_takedownaverage, fighter1_takedownaccuracy, fighter1_takedowndefense, fighter1_submissionsaverage,fighter1_numberOfUfcFights,clas);
        inputs.add(in);
    }
    return inputs;

}


}
