package com.example.Services;

import com.example.DAO.EventRepository;
import com.example.Entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/29/2016.
 */
@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;


    public List<Event> findAll(){
        return eventRepository.findAll();
    }


    public void save(Event event){
        eventRepository.save(event);
    }




    public Event get(int id) {
       return eventRepository.findOne(id);
    }

    public List<Object[]> findUpcoming() {
        return eventRepository.findUpcomingEvents();
    }

    public List<Object[]> findPastEvents(){
        return eventRepository.findPastEvents();
    }

    //returns 5 upcoming events for homescreen display
    public List<Event> findNextEvents() {
        return eventRepository.getNextEvents();
    }
}
