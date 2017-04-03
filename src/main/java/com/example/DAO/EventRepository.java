package com.example.DAO;

import com.example.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

  @Query(value = "SELECT * FROM EVENT where EVENT_DATE >NOW() ORDER BY EVENT_DATE ASC LIMIT 5;", nativeQuery = true)
  List<Event> getNextEvents();
}
