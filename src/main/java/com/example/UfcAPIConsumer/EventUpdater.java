package com.example.UfcAPIConsumer;

import com.example.DAO.FighterRepository;
import com.example.DAO.MatchupRepository;
import com.example.Entity.Fighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Oisín on 2/6/2017.
 */
public class EventUpdater {
    @Autowired
    MatchupRepository matchupRepository;
    FighterRepository fighterRepository;

    //will need to find method to find events to be updated -->check status and date fields

    public void updateFighterRecordsForEvent(int id){

        ///get list of fighters from an event
        List<Integer> fighterIds=matchupRepository.findFightersToUpdate(511353);

        //call API and update fighters
        RestTemplate restTemplate=new RestTemplate();

        //find each fighter and update their records
        //have to update not just save-->> need to keep links intact


    }

}
