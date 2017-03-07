package com.example.MachineLearning;

import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.unsupervised.attribute.Remove;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by Oisín on 3/7/2017.
 */
@Service
public class MultiLayerPerceptron {
    public MultiLayerPerceptron(){}

//    @PostConstruct
    public void simpleWekaTrain()
    {
        String filepath="C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/airline.arff";
        try{
            FileReader trainreader = new FileReader(filepath);
            Instances totalSet = new Instances(trainreader);
            totalSet.sort(0);
            // Percent split

            int trainSize = totalSet.size()-200;
//            (int) Math.round(totalSet.numInstances() * 80.5
//                    / 100);
            int testSize = totalSet.numInstances() - trainSize;
            Instances train = new Instances(totalSet, 0, trainSize);
            Instances test = new Instances(totalSet, trainSize, testSize);

            // filter
            Remove rm = new Remove();
            rm.setAttributeIndices("1");  // remove id
            // classifier
            MultilayerPerceptron mlp = new MultilayerPerceptron();
            mlp.setOptions(Utils.splitOptions(" -L 0.05 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"15, 7\" -R"));
            Attribute clas=train.attribute(16);
            train.setClass(clas);

            // meta-classifier
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(rm);
            fc.setClassifier(mlp);
            //build on training set
            fc.buildClassifier(train);

            int numCorrect=0;
            //sort by matchupId
            train.sort(train.attribute(0));
            for (int i = 0; i < train.numInstances(); i++) {
                double pred = fc.classifyInstance(train.instance(i));
                System.out.print("ID: " + train.instance(i).value(0));
                System.out.print(", actual: " + train.instance(i).classValue());
                System.out.println(", predicted: " + pred);
                if(i+1!=train.size())
                    if (train.get(i).value(0)==train.get(i+1).value(0)) {
                        if(train.get(i).classValue()==0) {
                            if(fc.classifyInstance(train.instance(i))<fc.classifyInstance(train.instance(i+1)))
                            {
                                numCorrect++;
                            }
                        }
                        else {
                            if(fc.classifyInstance(train.instance(i))>fc.classifyInstance(train.instance(i+1)))
                            {
                                numCorrect++;
                            }
                        }
                    }
            }

            //evauluate training data
            Evaluation eval = new Evaluation(train);
//            train.sort(train.attribute(0));
            train.deleteAttributeAt(0);
            eval.evaluateModel(mlp,train);
            System.out.println(eval.errorRate()); //Printing Training Mean root squared Error
            System.out.println(eval.toSummaryString()); //Summary of Training
            System.out.println(eval.numInstances()/2); //Summary of Training
            System.out.println("NUM CORRECT"+numCorrect);


System.out.println("--------------------------------TEST SET---------------------------------------------");

//            Instances datapredict = new Instances(
//                    new BufferedReader(
//                            new FileReader("C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/testset2.arff")));
//            datapredict.setClassIndex(test.numAttributes()-1);
             Attribute clas2=test.attribute(16);
            test.setClass(clas2);
            fc.setFilter(rm);

            //Predict Part
            numCorrect=0;
            test.sort(test.attribute(0));
            for (int i = 0; i < test.numInstances(); i++) {
                double pred = fc.classifyInstance(test.instance(i));
                System.out.print("ID: " + test.instance(i).value(0));
                System.out.print(", actual: " + test.instance(i).classValue());
                System.out.println(", predicted: " + pred);
                if(i+1!=test.size())
                    if (test.get(i).value(0)==test.get(i+1).value(0)) {
                        if(test.get(i).classValue()==0) {
                            if(fc.classifyInstance(test.instance(i))<fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                            }
                        }
                        else {
                            if(fc.classifyInstance(test.instance(i))>fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                            }
                        }
                    }
            }
            System.out.println("test set correct "+numCorrect+"/"+test.size()/2);
////            //Storing again in arff
////            BufferedWriter writer = new BufferedWriter(
////                    new FileWriter("C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/predioctedoutput.arff"));
////            writer.write(predicteddata.toString());
////            writer.newLine();
//            writer.flush();
//            writer.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void predit(){

    }
}
