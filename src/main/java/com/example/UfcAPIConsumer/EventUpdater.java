package com.example.UfcAPIConsumer;

import com.example.DAO.EventRepository;
import com.example.DAO.FighterRepository;
import com.example.DAO.MatchupRepository;
import com.example.DAO.PredictionRepository;
import com.example.Entity.Event;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 2/6/2017.
 */
@Service
public class EventUpdater {
    @Autowired
    MatchupRepository matchupRepository;
    @Autowired
    StartupService startupService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    PredictionRepository predictionRepository;

    @PostConstruct
    public void refreshEvents(){
        //remove all upcoming matchups to accomodate for fight card changes (rplacement,cancellation, etc.)

        removeAllUpcomingMatchups();

        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Event[]> responseEntity = restTemplate.getForEntity("http://ufc-data-api.ufc.com/api/v3/us/events", Event[].class);
        Event[] events = responseEntity.getBody();
        Date now = new Date();

        //nb delete all upcoming matchups

        boolean allUpcomingEvents=true;

        int i=0;
        do{
            //events will be returned odered by date (upcoming first)
            if(now.compareTo(events[i].getEvent_date())<0){
                //save the evet and get its matchups
                eventRepository.save(events[i]);
                startupService.getEventInfo(events[i]);
            }
            else {
                allUpcomingEvents=false;
            }
            i++;
        }while (allUpcomingEvents);

        //re-run cross validation
        startupService.onStartup();

    }

    //will need to find method to find events to be updated -->check status and date fields

    public void updateFighterRecordsForEvent(int eventId){

        ///get list of fighters from an event
        List<Integer> fighterIds=matchupRepository.findFightersToUpdate(eventId);

        //call API and update fighters
        RestTemplate restTemplate=new RestTemplate();

        //find each fighter and update their records
        //have to update not just save-->> need to keep links intact

    }

//    @PostConstruct
    //this method deletes all upcoming matchups,
    //the reason for this is to ensure if fights are cancelled or opponents are replaced this will be reflected
    public void removeAllUpcomingMatchups(){
        predictionRepository.deleteAll();
        matchupRepository.deleteMatchupLinks();
        matchupRepository.deleteUpcomingMatchups();
//        ArrayList<Matchup> allUpcomingMatchup=matchupRepository.findUpcomingToDelete();
    }

}
