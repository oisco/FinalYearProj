package com.example.Services;

import com.example.DAO.EventRepository;
import com.example.DAO.FighterRepository;
import com.example.DAO.MatchupRepository;
import com.example.Entity.Event;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/29/2016.
 */
@Service
public class FighterService {
    @Autowired
    FighterRepository fighterRepository;

    public List<Fighter> findAll()
    {
        return fighterRepository.findAll();
    }

    public List<Object[]> findAllToDisplay(){
        return fighterRepository.findAllToDisplay();
    }
    public Fighter get(int id) {
        return fighterRepository.findOne(id);
    }
}
