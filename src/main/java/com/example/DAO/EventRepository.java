package com.example.DAO;

import com.example.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/29/2016.
 */
@Repository
public interface EventRepository extends JpaRepository<Event,Integer>{
  public Event findOne(int id);
    public List<Event> findByDateGreaterThan();
  public List<Event> findToGetMatchups();



  List<Object[]> findPastEvents();
    List<Object[]> findUpcomingEvents();

  @Query(value = "SELECT * FROM EVENT where EVENT_DATE >NOW() ORDER BY EVENT_DATE ASC LIMIT 3;", nativeQuery = true)
  List<Event> getNextEvents();

  //find any events from the past week
  @Query(value = "select * from event where event_date>now() - INTERVAL 1 week and event_date<now();", nativeQuery = true)
  ArrayList<Event> findToUpdate();

}
