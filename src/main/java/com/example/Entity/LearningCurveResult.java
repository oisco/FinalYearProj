package com.example.Entity;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;

/**
 * Created by Oisín on 4/1/2017.
 */
@Entity
@NamedNativeQuery(name = "LearningCurveResult.findLearningCurveValues",query ="select lr.* from testing_result lr where dtype='LearningCurveResult'")
public class LearningCurveResult extends TestingResult {
    public LearningCurveResult(double accuracy, int numberOfFights) {
        super(accuracy, numberOfFights);
    }

    public LearningCurveResult(){}
}
