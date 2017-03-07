package com.example.DAO;

import com.example.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Oisín on 1/20/2017.
 */
@Repository
public interface ResultRepository extends JpaRepository<Result,Integer>{
}
