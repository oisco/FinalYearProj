package com.example.Entity;

import javax.persistence.*;

/**
 * Created by Oisín on 4/1/2017.
 */
@Entity
@NamedNativeQuery(name = "TestingResult.getRidOfFoldResult",query ="delete from testing_result where dtype=?1")
public abstract class TestingResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    double accuracy;
    int numberOfFights;

    public TestingResult(double accuracy, int numberOfFights) {
        this.accuracy = accuracy;
        this.numberOfFights = numberOfFights;
    }

    public TestingResult(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getNumberOfFights() {
        return numberOfFights;
    }

    public void setNumberOfFights(int numberOfFights) {
        this.numberOfFights = numberOfFights;
    }
}
