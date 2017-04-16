package com.example.DAO;

import com.example.Entity.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/29/2016.
 */
@Repository
public interface MatchupRepository extends JpaRepository<Matchup,Integer> {

    public List<Integer>findFightersToUpdate(int eventId);
    //MACHINE LEARNING INPUTS
    public List<Object[]> findMLClass1Inputs();
    public List<Object[]> findMLClass0Inputs();
    public List<Object[]> findFutureMatchupsToPredict();

    public int fightersNoOfLosses(int id, Date date);
    public int fightersNoOfWins(int id, Date date);

    public List<Matchup> findByStatus(String status);

    List<Object[]> findPastFightersMatchupStats(int fighterId,Date date);

    int findNoOfPastUfcFinishes(int fighterId, Date date);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM FIGHTER_MATCHUPS WHERE\n" +
            "MATCHUPS_ID IN (SELECT ID FROM MATCHUP WHERE DATE>NOW())", nativeQuery = true)
    void deleteMatchupLinks();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Matchup where date>now()", nativeQuery = true)
    void deleteUpcomingMatchups();




    List<Matchup> findByDateGreaterThan(Date d);
}
