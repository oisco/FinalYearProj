package com.example.DAO;

import com.example.Entity.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/29/2016.
 */
@Repository
public interface MatchupRepository extends JpaRepository<Matchup,Integer> {

    public List<Integer>findFightersToUpdate(int eventId);

    public List<Object[]> findMLClass1Inputs();
    public List<Object[]> findMLClass0Inputs();

    public int fightersNoOfLosses(int id, Date date);
    public int fightersNoOfWins(int id, Date date);

    public List<Matchup> findByStatus(String status);

    List<Object[]> findPastFightersMatchupStats(int fighterId,Date date);
}
