package com.example.Services;

import com.example.DAO.NewsRepository;
import com.example.Entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Oisín on 4/20/2017.
 */
@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;


    public List<News> findAll() {
        return newsRepository.findValid();
    }
}
