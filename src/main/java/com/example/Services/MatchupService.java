package com.example.Services;

import com.example.DAO.MatchupRepository;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Oisín on 10/29/2016.
 */

@Service
public class MatchupService {
    @Autowired
    private MatchupRepository matchupRepository;

    public void createMatchup(Matchup matchup){
        matchupRepository.save(matchup);
    }

    public Matchup get(int id) {
    return matchupRepository.findOne(id);
    }
}
