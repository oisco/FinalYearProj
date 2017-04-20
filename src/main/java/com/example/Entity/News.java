package com.example.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

/**
 * Created by Oisín on 4/20/2017.
 */
@Entity
@NamedNativeQuery( name = "News.findValid",query = "select * from news where thumbnail!='';")
public class News {
    @Id
    private int id;

    String url_name;
    String thumbnail;
    String title;

    public News(int id, String url_name, String thumbnail, String title) {
        this.id = id;
        this.url_name = url_name;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    public News(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl_name() {
        return url_name;
    }

    public void setUrl_name(String url_name) {
        this.url_name = url_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
