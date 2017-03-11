package com.example.MachineLearning;

import com.example.DAO.MatchupRepository;
import com.example.Entity.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.unsupervised.attribute.Remove;

import java.io.FileReader;

/**
 * Created by Oisín on 3/7/2017.
 */
@Service
public class MultiLayerPerceptron {

    int totalCorrect=0;
    int testSize=0;
    int numiInstances=239;
    @Autowired
    private MatchupRepository matchupRepository;
    public MultiLayerPerceptron(){}
    
    public void crossValidate(){
        double pctCorrect=0;
        int p=0;
        //can increase test size for every 8 new matchups
        testSize=216;//200
        int startingPoint=0;
        do{
            p++;
            pctCorrect+=simpleWekaTrain(startingPoint);
            startingPoint=startingPoint+testSize;
        }while (startingPoint<1740-testSize);

        System.out.println("full amt %: "+(pctCorrect/p)+"%");
    }

//    @PostConstruct
    public float simpleWekaTrain(int startingPoint)
    {
//        String filepath="C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/TRAIN3.arff";
        String filepath="C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/airline.arff";
        try{
            FileReader trainreader = new FileReader(filepath);
            Instances train = new Instances(trainreader);
            numiInstances=train.numInstances();
            train.sort(0);

            //below will remove create a test set from the total/traing set at a specified index and remove said test set from the training set
            // Percent split
            int testAmt=testSize;
//            int startingPoint=1400;
            Instances test=new Instances(train,  startingPoint,  testAmt);
            for (int i=0;i<testAmt;i++){
                train.delete(startingPoint);
            }
            // filter
            Remove rm = new Remove();
            rm.setAttributeIndices("1");  // remove id
            // classifier
            MultilayerPerceptron mlp = new MultilayerPerceptron();
            ///increase by 1.25 for every 10% DECREASE IN learing set size
            mlp.setOptions(Utils.splitOptions(" -L 0.3125 -M 0.15 -N 5000 -V 0 -S 0 -E 20 -H \"8\" -R"));//m.15 size 200 then UI!!
//            mlp.setOptions(Utils.splitOptions(" -L 0.3125 -M 0.15 -N 5000 -V 0 -S 0 -E 20 -H \"8\" -R"));//m.15 size 200 then UI!!
            Attribute clas=train.attribute(13); //275 l
            train.setClass(clas);
//14-2-1 NOLIMIT .04 .16
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
//                System.out.print("ID: " + train.instance(i).value(0));
//                System.out.print(", actual: " + train.instance(i).classValue());
//                System.out.println(", predicted: " + pred);
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
//            train.deleteAttributeAt(0);
//            eval.evaluateModel(mlp,train);
//            System.out.println(eval.errorRate()); //Printing Training Mean root squared Error
//            System.out.println(eval.toSummaryString()); //Summary of Training
//            System.out.println(eval.numInstances()/2); //Summary of Training
            System.out.println("train set CORRECT"+numCorrect+" TOTAL: "+eval.numInstances()/2+" PCT:"+(numCorrect/((train.size()/2)/100.0f)));


System.out.println("--------------------------------TEST SET---------------------------------------------");
            test.setClassIndex(test.numAttributes()-1);
             Attribute clas2=test.attribute(13);
            test.setClass(clas2);
            fc.setFilter(rm);

            //Predict Part
            numCorrect=0;
            test.sort(test.attribute(0));
            for (int i = 0; i < test.numInstances(); i++) {
                double pred = fc.classifyInstance(test.instance(i));
//                System.out.print("ID: " + test.instance(i).value(0));
//                System.out.print(", actual: " + test.instance(i).classValue());
//                System.out.println(", predicted: " + pred);
                if(i+1!=test.size())
                    if (test.get(i).value(0)==test.get(i+1).value(0)) {
                        if(test.get(i).classValue()==0) {
                            if(fc.classifyInstance(test.instance(i))<fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                //create a prediction object and set the matchup and winner
                                createPrediction()
                            }
                        }
                        else {
                            if(fc.classifyInstance(test.instance(i))>fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                Prediction prediction=new Prediction();
                                prediction.setMatchup(matchupRepository.findOne());
                                prediction.setWinner();
                            }
                        }
                    }
            }
            System.out.println("test set correct "+numCorrect+"/"+test.size()/2+" pct: "+(numCorrect/((test.size()/2)/100.0f)));
////            //Storing again in arff
////            BufferedWriter writer = new BufferedWriter(
////                    new FileWriter("C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/predioctedoutput.arff"));
////            writer.write(predicteddata.toString());
////            writer.newLine();
//            writer.flush();
//            writer.close();
            return (numCorrect/((test.size()/2)/100.0f));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    private void savePrediction(Instance i) {
        Prediction prediction=new Prediction();
        prediction.setMatchup(matchupRepository.findOne(i.attribute(1)));
        prediction.;

    }

    public void predit(){

    }
}
