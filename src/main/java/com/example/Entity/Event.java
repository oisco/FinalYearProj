package com.example.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Oisín on 10/28/2016.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedNativeQueries({
                @NamedNativeQuery(name = "Event.findByDateGreaterThan", query = "select * from Event where event_date > NOW() order by event_date", resultClass = Event.class),
                @NamedNativeQuery(name = "Event.findNextEvent", query = "select * from event where event_date> NOW() order by event_date limit 1", resultClass = Event.class),
                @NamedNativeQuery(name = "Event.findPastEvents", query = "select id,base_title,title_tag_line,event_date from event where event_date< NOW()  order by event_date desc"),
                @NamedNativeQuery(name = "Event.findToGetMatchups", query = "select * from event where id not in(select distinct(event_id) from matchup) AND EVENT_DATE<NOW() ORDER BY(id) DESC;" ,resultClass = Event.class),
        @NamedNativeQuery(name = "Event.findUpcomingEvents", query = "select id,base_title,title_tag_line,event_date from event where event_date> NOW()  order by event_date asc  limit 7")})
public class Event {
    @Id
    private int id;

    @OneToMany(mappedBy="event", fetch = FetchType.LAZY)
    private List<Matchup> matchups;

    private String base_title;
    private Date event_date;
    private String location;
    private String event_status;
    private String title_tag_line;
    private String feature_image;

    public Event(){

    }

    public Event(int id, List<Matchup> matchups, String base_title, Date event_date, String location, String event_status, String title_tag_line, String feature_image) {
        this.id = id;
        this.matchups = matchups;
        this.base_title = base_title;
        this.event_date = event_date;
        this.location = location;
        this.event_status = event_status;
        this.title_tag_line = title_tag_line;
        this.feature_image = feature_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public List<Matchup> getMatchups() {
        return matchups;
    }

    public void setMatchups(List<Matchup> matchups) {
        this.matchups = matchups;
    }


    public String getLocation() {
        return location;
    }

    public String getBase_title() {
        return base_title;
    }

    public void setBase_title(String base_title) {
        this.base_title = base_title;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getTitle_tag_line() {
        return title_tag_line;
    }

    public void setTitle_tag_line(String title_tag_line) {
        this.title_tag_line = title_tag_line;
    }
}
