package com.example.DAO;

import com.example.Entity.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Oisín on 10/22/2016.
 */

@Repository
public interface FighterRepository extends JpaRepository<Fighter,Integer>{
    //custom jpa filter
    public List<Fighter> findByIdLessThan(int id);
    public Fighter findOne(int id);
    public List<Object[]> findAllToDisplay();

}
