package com.example.Controller;

import com.example.DAO.MatchupRepository;
import com.example.Entity.Matchup;
import com.example.Entity.Matchup;
import com.example.Services.MatchupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Oisín on 10/28/2016.
 */
@RestController
@RequestMapping("/matchups")

public class MatchupController {

    @Autowired
    public MatchupService matchupService;

    public MatchupController(MatchupService matchupService){
        this.matchupService=matchupService;
    }

    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public Matchup view(@PathVariable int id) {
        return matchupService.get(id);
    }


    /*
    // @Autowired

    public MatchupController(MatchupRepository matchupRepository)
    {
        this.matchupRepository=matchupRepository;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Matchup> getAll(){

        return matchupRepository.findAll();
    }


    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public List<Matchup> create(@RequestBody Matchup matchup) {
        matchupRepository.save(matchup);
        return matchupRepository.findAll();
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public List<Matchup> delete(@PathVariable int id) {
        matchupRepository.delete(id);
        return matchupRepository.findAll();
    }
    */
}


