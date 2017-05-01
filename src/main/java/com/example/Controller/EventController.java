package com.example.Controller;

import com.example.Entity.Event;
import com.example.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Oisín on 10/22/2016.
 */
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    public EventController(EventService eventService)
    {
        this.eventService=eventService;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Event> getAll(){
        return eventService.findAll();
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public List<Event> create(@RequestBody Event event) {
        eventService.save(event);
        return eventService.findAll();
    }


    @RequestMapping(value = "/upcoming",method = RequestMethod.GET)
    public List<Object[]> findUpcoming() {
        return eventService.findUpcoming();
    }

    @RequestMapping(value = "/next",method = RequestMethod.GET)
    public List<Event> findNext() {
        return eventService.findNextEvents();
    }


    @RequestMapping(value = "/previous",method = RequestMethod.GET)
    public List<Object[]> findPastEvents() {
        return eventService.findPastEvents();
    }


    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public Event view(@PathVariable int id) {
        return eventService.get(id);
    }

}

