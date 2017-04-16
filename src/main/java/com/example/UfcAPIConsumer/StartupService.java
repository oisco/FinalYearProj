package com.example.UfcAPIConsumer;

import com.example.DAO.*;
import com.example.Entity.Event;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
import com.example.Entity.Prediction;
import com.example.FileWriter.CsvFileWriter;
import com.example.MachineLearning.CalculateStats;
import com.example.MachineLearning.MultiLayerPerceptron;
//import com.example.PredictionEngine.Predictor;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Oisín on 1/16/2017.
 */
@Service
public class StartupService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    FighterRepository fighterRepository;
    @Autowired
    MatchupRepository matchupRepository;
    @Autowired
    ResultRepository resultRepository;
    @Autowired
    PredictionRepository predictionRepository;
    @Autowired
    CalculateStats calculateStats;
    @Autowired
    CsvFileWriter csvFileWriter;
    @Autowired
    MultiLayerPerceptron multiLayerPerceptron;
    @Autowired
    FoldResultRepository foldResultRepository;
    @Autowired
    TestingResultRepository testingResultRepository;
    //service to populate db with data from UFC API on startup if database is currently empty
//   @PostConstruct
    public void onStartup() {
        if(fighterRepository.findAll().size()>0) {
//            events
//           ResponseEntity<Event[]> responseEntity = restTemplate.getForEntity("http://ufc-data-api.ufc.com/api/v3/us/events", Event[].class);
//           Event[] events = responseEntity.getBody();
//           eventRepository.save(Arrays.asList(events));
//            //fighters
//           ResponseEntity<Fighter[]> responseEntity2 = restTemplate.getForEntity("http://ufc-data-api.ufc.com/api/v3/us/fighters", Fighter[].class);
//           Fighter[] fighters = responseEntity2.getBody();
//           fighterRepository.save(Arrays.asList(fighters));

                //get all matchups for the event ids
//            ArrayList<Event> events1 = new ArrayList<>();
//            events1.add(eventRepository.findOne(611356));
//                List<Event> events1 = eventRepository.findByDateGreaterThan();


         calculateStats.getFighterStatsAtTimeOfMatchup();
//            getEventInfo(eventRepository.findOne(611205));

            testingResultRepository.deleteAll();
            //REMOVE RECORDS TO DETERMINE LEARNING CURVE
            int setToDelete[]={0,400,800,1200,1600};
//            int setToDelete[]={0,200,400,600,800,1000,1200,1400,1600};
            double results[]=new double[setToDelete.length];
            //is deleting the first of the full data set and not the last!
            createArffMLInputs();
            for(int i=setToDelete.length-1;i >= 0;i--){
                //save the results if we are cross validating using the full data set
                if(i==0){
                    predictionRepository.deleteAll();
                    testingResultRepository.getRidOfFoldResult("FoldResult");
                }

                System.out.println(setToDelete[i]+" fights removed");

                //pass crossvalidate an int of how many fights to delete before beginnning
                results[i]=multiLayerPerceptron.crossValidate(setToDelete[i]);
            }

            multiLayerPerceptron.PredictUpcoming();
            for (int i=0;i<results.length;i++){
                System.out.println(i+" months ago "+results[i]+"%");
            }
            }
   }

   //goes to an event url and gets its fights , gets matchup results if it past and if upcming will save the matchup -->and calculate its stats for each fighter
   public void getEventInfo(Event event){
       RestTemplate restTemplate=new RestTemplate();
           ResponseEntity<String> responseEntity3 = restTemplate.getForEntity("http://ufc-data-api.ufc.com/api/v3/us/events/" + event.getId() + "/fights", String.class);
           String matchupString = responseEntity3.getBody();
           Gson gson = new GsonBuilder().create();
           Matchup[] matchups = gson.fromJson(matchupString, Matchup[].class);
           saveMatchups(matchups, event);
           System.out.println("event data got");
   }
    private void createArffMLInputs() {
        csvFileWriter.setFileWriter("src/main/resources/PerceptronInputs/PastMatchups.arff");
        csvFileWriter.writeCsvFile(true);
        csvFileWriter.setFileWriter("src/main/resources/PerceptronInputs/FutureMatchups.arff");
        csvFileWriter.writeCsvFile(false);

    }

    private void addMatchupToFighter(int id, Matchup m) {
        Fighter fighter = fighterRepository.findOne(id);
        if (fighter != null) {
            //avoid duplicate links to matchups when loading data from api
                fighter.getMatchups().add(m);
                //update the fighter with latest reach and height
                if(fighter.getId()==m.getFighter1_id()){
                    if(m.getFighter1reach()>0 && m.getFighter1height()>0)
                    {
                        fighter.setHeight(m.getFighter1height());
                        fighter.setReach(m.getFighter1reach());
                    } else {
                        m.setFighter1reach(fighter.getReach());
                        m.setFighter1height(fighter.getHeight());
                    }
                }else{
                    if(m.getFighter2reach()>0 && m.getFighter2height()>0)
                    {
                        fighter.setHeight(m.getFighter2height());
                        fighter.setReach(m.getFighter2reach());
                    }
                    else {
                        m.setFighter2reach(fighter.getReach());
                        m.setFighter2height(fighter.getHeight());
                    }
                }
                fighterRepository.save(fighter);
            }
    }

    public void saveMatchups(Matchup[] matchups,Event event){
        //set the event for each matchup
        int p = 0;
        while (p < matchups.length) {
            matchups[p].setEvent(event);
            matchups[p].setDate(event.getEvent_date());
                //predict the matchup if it has not taken place
            Date currentDate = new Date();
            if (currentDate.compareTo(event.getEvent_date())<0) {
//                Predictor predictor = new Predictor();
                    //before predicting the fight ensure we have a profile for each fighter(in the case of newcomers create one)
                int fighter1Id=matchups[p].getFighter1_id();
                int fighter2Id=matchups[p].getFighter2_id();

                Fighter fighter1=fighterRepository.findOne(fighter1Id);
                Fighter fighter2=fighterRepository.findOne(fighter2Id);
                if(fighter1==null){
                    fighter1=getFighterProfile(fighter1Id);
                }
                if(fighter2==null){
                    fighter2=getFighterProfile(fighter2Id);
                }
                if((fighter1!=null) && (fighter2!=null))
                {
                    matchups[p].setResult(null);
                    //update fighter names for matchups for future display purposes
                    if(matchups[p].getFighter1_first_name()==null &&matchups[p].getFighter2_first_name()==null){
                        matchups[p].setFighter1_first_name(fighter1.getFirst_name());
                        matchups[p].setFighter2_first_name(fighter2.getFirst_name());
                        matchups[p].setFighter1_last_name(fighter1.getLast_name());
                        matchups[p].setFighter2_last_name(fighter2.getLast_name());
                    }
                     matchupRepository.save(matchups[p]);
//                    prediction.setMatchup(matchups[p]);
//                    predictionRepository.save(prediction);
                    addMatchupToFighter(matchups[p].getFighter1_id(), matchups[p]);
                    addMatchupToFighter(matchups[p].getFighter2_id(), matchups[p]);
                }
                else {
                    //there is not currently a profile set up for each ufc fighter, ignore matchup and prediction
                    addMatchupToFighter(matchups[p].getFighter1_id(), matchups[p]);
                    addMatchupToFighter(matchups[p].getFighter2_id(), matchups[p]);
                }
            }else {
                //if fight has taken place save the result and the fight stats
                if (matchups[p].getResult() != null) {
                    //some earlier fights dont record stats and will send a 404
                    Matchup m=getMatchupStats(matchups[p]);
                    resultRepository.save(matchups[p].getResult());
                    if(m!=null){
                            matchups[p]=m;
                    }
                    matchupRepository.save(matchups[p]);
                    addMatchupToFighter(matchups[p].getFighter1_id(), matchups[p]);
                    addMatchupToFighter(matchups[p].getFighter2_id(), matchups[p]);
                }
            }
            p++;
        }
    }

    public Fighter getFighterProfile(int id){
        ///nb --need to change to save only the fgihter we need
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Fighter[]> responseEntity2 = restTemplate.getForEntity("http://ufc-data-api.ufc.com/api/v3/us/fighters", Fighter[].class);
        Fighter[] fighters = responseEntity2.getBody();
        System.out.print("fighter saved");
        Fighter f=findFighterById(fighters,id);
        if(f!=null){
            fighterRepository.save(f);
            return fighterRepository.findOne(id);
        }
        else {
            return null;
        }
    }

    public Fighter findFighterById(Fighter[] fighters,int id){
        boolean found=false;
        int i=0;
        while(!found){
            ///break if ufc profile has not been created for fighter listed in matchup
            if(i==fighters.length)
            {
                break;
            }
            if(fighters[i].getId()==id){
                return fighters[i];
            }
            i++;
        }
        return null;
    }

    public Matchup getMatchupStats(Matchup matchup)
    {
        //some statistics on matchups are unavailable for previous events and will return a 404 error
        RestTemplate restTemplate=new RestTemplate();
        try{
            ResponseEntity<String> responseEntity3 = restTemplate.getForEntity(matchup.getFm_stats_feed_url(), String.class);
            String matchupStatsString = responseEntity3.getBody();
            return getMatchupStats(matchup,matchupStatsString);
        }
        catch (final HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }
        return null;

    }

    public Matchup getMatchupStats(Matchup matchup,String matchupStatsString){
        JsonElement jelement = new JsonParser().parse(matchupStatsString);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonObject jarray = jobject.getAsJsonObject("FMLiveFeed");
        JsonObject jarray2 = jarray.getAsJsonObject("FightStats");
        if(jarray2==null){
            //one in every 100 or so matchup stats json from api will follow an obscure format which must be parse differently
            //simply return the matchup without updating any stats
            JsonObject jo=jarray.getAsJsonObject("FightCard");
            JsonArray jo1=jo.getAsJsonArray("Fight");
            JsonElement jo2=jo1.get(2);
            JsonObject fightStats=jo2.getAsJsonObject();
            JsonArray fightStats2=fightStats.getAsJsonArray("FightStats");

            JsonElement fighter2=fightStats2.get(0);
            JsonElement fighter1=fightStats2.get(0);

            getFightersMatchupStats(fighter1,matchup,1);
            getFightersMatchupStats(fighter2,matchup,2);
            return matchup;

        }
        JsonObject fighter1stats = jarray2.getAsJsonObject("Red");//fighter1
        JsonObject fighter2stats = jarray2.getAsJsonObject("Blue");//fighter2
        //save the stats of each fighter
        matchup=getFighterMatchupStats(fighter1stats,matchup,1);
        matchup=getFighterMatchupStats(fighter2stats,matchup,2);

        return matchup;
    }

    //get information about what happend in the fight --> takedowns landed, top control time , stikes landed atc
    private Matchup getFighterMatchupStats(JsonObject fighterstats,Matchup matchup,int fighter) {
        JsonObject grappling=fighterstats.getAsJsonObject("Grappling");
        JsonObject strikes=fighterstats.getAsJsonObject("Strikes");
        JsonObject controlTime=fighterstats.getAsJsonObject("TIP");
        //total strikes landed
        JsonObject totalStrikes=strikes.getAsJsonObject().get("Total Strikes").getAsJsonObject();
        JsonElement strLanded=totalStrikes.get("Landed");
        int strikesLanded=strLanded.getAsInt();
        JsonElement strAttempted=totalStrikes.get("Attempts");
        int strikesAttempted=strAttempted.getAsInt();
        //knockdowns
        JsonObject knockdowns=strikes.getAsJsonObject().get("Knock Down").getAsJsonObject();
        int knockdownsLanded=knockdowns.get("Landed").getAsInt();

        ////////grappling/////
        //takedowns
        JsonObject takedowns=grappling.getAsJsonObject("Takedowns").getAsJsonObject();
        int takedownsLanded=takedowns.get("Landed").getAsInt();
        int takedownsAttempted=takedowns.get("Attempts").getAsInt();
        JsonObject submissions=grappling.getAsJsonObject("Submissions").getAsJsonObject();
        int submissionsAttempted=submissions.get("Attempts").getAsInt();
        JsonObject standups=grappling.getAsJsonObject("Standups").getAsJsonObject();
        int standupsLanded=standups.get("Landed").getAsInt();

        /////control time////////
        String standingTime=controlTime.get("Standing Time").getAsString();
        String groundTime=controlTime.get("Ground Time").getAsString();
        String groundControlTime=controlTime.get("Ground Control Time").getAsString();

        if(fighter==1){
            matchup.getResult().setFighter1groundControlTime(groundControlTime);
            matchup.getResult().setFighter1groundTime(groundTime);
            matchup.getResult().setFighter1standingTime(standingTime);

            matchup.getResult().setFighter1KnockdownsLanded(knockdownsLanded);
            matchup.getResult().setFighter1StrikesAttempted(strikesAttempted);
            matchup.getResult().setFighter1StrikesLanded(strikesLanded);

            matchup.getResult().setFighter1takedownsAttempted(takedownsAttempted);
            matchup.getResult().setFighter1takedownsLanded(takedownsLanded);

            matchup.getResult().setFighter1submissionsAttempted(submissionsAttempted);
            matchup.getResult().setIsValid(true);

        }
        else if(fighter==2){
            matchup.getResult().setFighter2groundControlTime(groundControlTime);
            matchup.getResult().setFighter2groundTime(groundTime);
            matchup.getResult().setFighter2standingTime(standingTime);

            matchup.getResult().setFighter2KnockdownsLanded(knockdownsLanded);
            matchup.getResult().setFighter2StrikesAttempted(strikesAttempted);
            matchup.getResult().setFighter2StrikesLanded(strikesLanded);

            matchup.getResult().setFighter2takedownsAttempted(takedownsAttempted);
            matchup.getResult().setFighter2takedownsLanded(takedownsLanded);

            matchup.getResult().setFighter2submissionsAttempted(submissionsAttempted);
            matchup.getResult().setIsValid(true);

        }
        return matchup;
    }

    public Matchup getFightersMatchupStats(JsonElement fighterStatsJson,Matchup matchup, int currentFighter){
        JsonObject fighter=fighterStatsJson.getAsJsonObject();
        JsonArray fighterStats=fighter.getAsJsonArray("Fighter");
        JsonArray strikes=fighterStats.get(0).getAsJsonObject().getAsJsonArray("Strikes");

        int knockdownsLanded=strikes.get(0).getAsJsonObject().get("Landed").getAsInt();
        int totalStrikesLanded=strikes.get(2).getAsJsonObject().get("Landed").getAsInt();
        int strikesAttempted=strikes.get(2).getAsJsonObject().get("Attempts").getAsInt();

        JsonArray grappling=fighterStats.get(1).getAsJsonObject().getAsJsonArray("Grappling");
        int takedownAttempts=grappling.get(0).getAsJsonObject().get("Attempts").getAsInt();
        int takedownsLanded=grappling.get(0).getAsJsonObject().get("Success").getAsInt();
        int submissionAttempts=grappling.get(1).getAsJsonObject().get("Attempts").getAsInt();

        //create method for below
        if(currentFighter==1){
            matchup.getResult().setFighter1KnockdownsLanded(knockdownsLanded);
            matchup.getResult().setFighter1StrikesAttempted(strikesAttempted);
            matchup.getResult().setFighter1StrikesLanded(totalStrikesLanded);

            matchup.getResult().setFighter1takedownsAttempted(takedownAttempts);
            matchup.getResult().setFighter1takedownsLanded(takedownsLanded);

            matchup.getResult().setFighter1submissionsAttempted(submissionAttempts);
            matchup.getResult().setIsValid(true);

        }
        else if(currentFighter==2){
            matchup.getResult().setFighter2KnockdownsLanded(knockdownsLanded);
            matchup.getResult().setFighter2StrikesAttempted(strikesAttempted);
            matchup.getResult().setFighter2StrikesLanded(totalStrikesLanded);

            matchup.getResult().setFighter2takedownsAttempted(takedownAttempts);
            matchup.getResult().setFighter2takedownsLanded(takedownsLanded);

            matchup.getResult().setFighter2submissionsAttempted(submissionAttempts);

            matchup.getResult().setIsValid(true);
        }
        return matchup;
    }
}

