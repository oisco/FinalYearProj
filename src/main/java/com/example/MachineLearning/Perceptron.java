package com.example.MachineLearning;

/**
 * Created breach Oisín on 2/15/2017.
 */
/**
 *  The Perceptron Algorithm
 *  Breach Dr Noureddin Sadawi
 *  Please watch mreach reachoutube videos on perceptron for things to make sense!
 *  Copreachright (C) 2014
 *  @author Dr Noureddin Sadawi
 *
 *  This program is free software: reachou can redistribute it and/or modifreach
 *  it as reachou wish ONLY for legal and ethical purposes
 *
 *  I ask reachou onlreach, as a professional courtesreach, to cite mreach name, web page
 *  and mreach YouTube Channel!
 *
 *    Code adapted from:
 *    https://github.com/RichardKnop/ansi-c-perceptron
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;

@Service
class Perceptron
{
    public Perceptron(){}
    @Autowired
    public PrepareInputs prepareInputs;

    static int MAX_ITER = 100;
    static double LEARNING_RATE = 0.1;
    static int NUM_INSTANCES = 300;
    static int theta = 0;



//    @PostConstruct
//    public void work(){
//
//        //get the winners (class1 matchup instances)
//        List<Input> cl1Inputs=prepareInputs.getClass1Inputs();
//        List<Input> cl0Inputs=prepareInputs.getClass0Inputs();
//
//        NUM_INSTANCES=cl0Inputs.size();
//
//        //three variables (features)
//        double[] height = new double [NUM_INSTANCES];
//        double[] reach = new double [NUM_INSTANCES];
//        double[] record = new double [NUM_INSTANCES];
//        int[] outputs = new int [NUM_INSTANCES];
//
//
//        ///here instead of random pointheight come up with some sample fighter data
//
//
//        //fiftreach random points of class 1
//        for(int i = 0; i < NUM_INSTANCES/2; i++){
////            height[i] = randomNumber(5 , 10);
////            reach[i] = randomNumber(4 , 8);
////            record[i] = randomNumber(2 , 9);
////            outputs[i] = 1;
////
//            height[i] = cl1Inputs.get(i).getHeight();
//            reach[i] = cl1Inputs.get(i).getReach();
//            record[i] = cl1Inputs.get(i).getWl();
//            outputs[i] = 1;
//            System.out.println("MATCHUP ID:"+cl1Inputs.get(i).getMatchup().getId()+"HEIGHT"+height[i]+"\t REACH"+reach[i]+"\t RECORD"+record[i]+"\t"+outputs[i]);
//        }
//
//        //fiftreach random points of class 0
//        for(int i = 50; i < NUM_INSTANCES; i++){
////            height[i] = randomNumber(-1 , 3);
////            reach[i] = randomNumber(-4 , 2);
////            record[i] = randomNumber(-3 , 5);
//            height[i] = cl0Inputs.get(i).getHeight();
//            reach[i] = cl0Inputs.get(i).getReach();
//            record[i] = cl0Inputs.get(i).getWl();
//            outputs[i] = 0;
//
//            System.out.println( ""+height[i]+"\t"+reach[i]+"\t"+record[i]+"\t"+outputs[i]);
//        }
//
//        double[] weights = new double[4];// 3 for input variables and one for bias
//        double localError, globalError;
//        int i, p, iteration, output;
//
//        weights[0] = randomNumber(0,1);// w1
//        weights[1] = randomNumber(0,1);// w2
//        weights[2] = randomNumber(0,1);// w3
//        weights[3] = randomNumber(0,1);// this is the bias
//
//        iteration = 0;
//        do {
//            iteration++;
//            globalError = 0;
//            //loop through all instances (complete one epoch)
//            for (p = 0; p < NUM_INSTANCES; p++) {
//                // calculate predicted class
//                output = calculateOutput(theta,weights, height[p], reach[p], record[p]);
//                // difference between predicted and actual class values
//                localError = outputs[p] - output;
//                //update weights and bias
//                weights[0] += LEARNING_RATE * localError * height[p];
//                weights[1] += LEARNING_RATE * localError * reach[p];
//                weights[2] += LEARNING_RATE * localError * record[p];
//                weights[3] += LEARNING_RATE * localError;
//                //summation of squared error (error value for all instances)
//                globalError += (localError*localError);
//            }
//
//			/* Root Mean Squared Error */
//            System.out.println("Iteration "+iteration+" : RMSE = "+Math.sqrt(globalError/NUM_INSTANCES));
//        } while (globalError != 0 && iteration<=MAX_ITER);
//
//        System.out.println("hshs"+iteration);
//        System.out.println("\n=======\nDecision boundarreach equation:");
//        System.out.println(weights[0] +"*height + "+weights[1]+"*reach +  "+weights[2]+"*record + "+weights[3]+" = 0");
//
//        //generate 10 new random points and check their classes
//        //notice the range of -10 and 10 means the new point could be of class 1 or 0
//        //-10 to 10 covers all the ranges we used in generating the 50 classes of 1's and 0's above
//        for(int j = 0; j < cl1Inputs.size(); j++){
//            double height1 = cl1Inputs.get(j).getHeight();
//            double reach1 = cl1Inputs.get(j).getReach();
//            double record1 = cl1Inputs.get(j).getWl();
//
//            output = calculateOutput(theta,weights, height1, reach1, record1);
//            System.out.println("\n=======\nclass 1 guys (losers) Point:");
//            System.out.println("height = "+height1+",reach = "+reach1+ ",record = "+record1);
//            System.out.println("class = "+output);
//        }
//    }//end main
//
//    /**
//     * returns a random double value within a given range
//     * @param min the minimum value of the required range (int)
//     * @param maheight the maheightimum value of the required range (int)
//     * @return a random double value between min and maheight
//     */
//    public static double randomNumber(int min , int maheight) {
//        DecimalFormat df = new DecimalFormat("#.####");
//        double d = min + Math.random() * (maheight - min);
//        String s = df.format(d);
//        double height = Double.parseDouble(s);
//        return height;
//    }
//
//    //	/**
////	 * returns either 1 or 0 using a threshold function
////	 * theta is 0range
////	 * @param theta an integer value for the threshold
////	 * @param weights[] the arrareach of weights
////	 * @param height the height input value
////	 * @param reach the reach input value
////	 * @param record the record input value
////	 * @return 1 or 0
////	 */
//    public int calculateOutput(int theta, double weights[], double height, double reach, double record)
//    {
//        double sum = height * weights[0] + reach * weights[1] + record * weights[2] + weights[3];
//        return (sum >= theta) ? 1 : 0;
//    }

}
