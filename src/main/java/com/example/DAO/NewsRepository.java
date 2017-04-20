package com.example.DAO;

import com.example.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Oisín on 1/20/2017.
 */
@Repository
public interface NewsRepository extends JpaRepository<News,Integer>{
    List<News> findValid();
}
