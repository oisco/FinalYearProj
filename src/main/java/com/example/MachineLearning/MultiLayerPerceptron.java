package com.example.MachineLearning;

import com.example.DAO.FighterRepository;
import com.example.DAO.MatchupRepository;
import com.example.DAO.PredictionRepository;
import com.example.Entity.Fighter;
import com.example.Entity.Matchup;
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
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
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
    @Autowired
    private FighterRepository fighterRepository;
    @Autowired
    private PredictionRepository predictionRepository;

    public MultiLayerPerceptron(){}
    
    public void crossValidate(){
        predictionRepository.deleteAll();
        double pctCorrect=0;
        int p=0;
        //can increase test size for every 8 new matchups
        testSize=222;//200
        int startingPoint=0;
        do{
            p++;
            pctCorrect+=simpleWekaTrain(startingPoint);
            startingPoint=startingPoint+testSize;
        }while (startingPoint<=1776-testSize);

        System.out.println("full amt %: "+(pctCorrect/p)+"%");
    }

//    @PostConstruct
    public float simpleWekaTrain(int startingPoint)
    {
        //scale data
        Normalize norm = new Normalize();

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
            //remove fighter and matchup id
            rm.setAttributeIndices("1,2");
            // classifier
            MultilayerPerceptron mlp = new MultilayerPerceptron();
//            mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));
            mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.0 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));
            Attribute clas=train.attribute(15); //275 l
            train.setClass(clas);
            // meta-classifier
            FilteredClassifier fc = new FilteredClassifier();
            fc.setFilter(rm);
            fc.setClassifier(mlp);
            //build on training se

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
             Attribute clas2=test.attribute(15);
            test.setClass(clas2);
            fc.setFilter(rm);

            //Predict Part
            numCorrect=0;
            test.sort(test.attribute(0));

            for (int i = 0; i < test.numInstances(); i=i+2) {
                double pred = fc.classifyInstance(test.instance(i));
//                System.out.print("ID: " + test.instance(i).value(0));
//                System.out.print(", actual: " + test.instance(i).classValue());
//                System.out.println(", predicted: " + pred);
                if(i+1!=test.size())
                    if (test.get(i).value(0)==test.get(i+1).value(0)) {//2 records of same matchup
                        //is the loser 
                        if(test.get(i).classValue()==0) {
                            if(fc.classifyInstance(test.instance(i))<fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                savePrediction(test.instance(i+1),true);
                            }else {
                                savePrediction(test.instance(i+1),false);
                            }
                        }
                        else {
                            if(fc.classifyInstance(test.instance(i))>fc.classifyInstance(test.instance(i+1)))
                            {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                savePrediction(test.instance(i),true);
                            }
                            else {
                                savePrediction(test.instance(i),false);

                            }
                        }
                    }
            }
            System.out.println("test set correct "+numCorrect+"/"+test.size()/2+" pct: "+(numCorrect/((test.size()/2)/100.0f)));
////            //Storing again in arff
////            BufferedWriter writer = new BufferedWriter(
////                    new FileWriter("C:/Users/Oisín/Documents/SHIT TO DO/fyp/testMLData/predioctedoutput.arff"));
////            writer.write(predicted.data.toString());
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

    private void savePrediction(Instance instance,boolean wasCorrect) {
        Prediction prediction=new Prediction();
        int matchupId= (int) instance.value(0);
        int fighterId= (int) instance.value(1);
        Matchup matchup =matchupRepository.findOne(matchupId);
        Fighter fighter=fighterRepository.findOne(fighterId);

        prediction.setMatchup(matchupRepository.findOne(matchupId));
        String label=matchup.fighter1_first_name+' '+matchup.getFighter1_last_name()+" vs "+matchup.fighter2_first_name+' '+matchup.getFighter2_last_name();
        prediction.setCorrect(wasCorrect);
        prediction.setWinner(fighter);
        predictionRepository.save(prediction);
    }

    public void predit(){

    }
}
