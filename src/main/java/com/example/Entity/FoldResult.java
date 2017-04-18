package com.example.Entity;

import javax.persistence.Entity;

/**
 * Created by Oisín on 4/1/2017.
 */
@Entity
public class FoldResult extends TestingResult {
    public FoldResult(double accuracy, int numberOfFights) {
        super(accuracy, numberOfFights);
    }

    public FoldResult(){}
}
