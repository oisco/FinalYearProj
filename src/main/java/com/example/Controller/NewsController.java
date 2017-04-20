package com.example.Controller;

import com.example.Entity.News;
import com.example.Services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Oisín on 4/20/2017.
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;


    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<News> getAll(){
        return newsService.findAll();
    }

}
