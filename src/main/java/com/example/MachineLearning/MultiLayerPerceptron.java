package com.example.MachineLearning;

import com.example.DAO.*;
import com.example.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.unsupervised.attribute.Remove;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Oisín on 3/7/2017.
 */
@Service
public class MultiLayerPerceptron {

    @Autowired
    private MatchupRepository matchupRepository;
    @Autowired
    private FighterRepository fighterRepository;
    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private FoldResultRepository foldResultRepository;
    @Autowired
    private LearningCurveResultRepository learningCurveResultRepository;

    MultilayerPerceptron mlp;
    Instances train;
    int totalCorrect=0;
    int testSize=0;
    int numberOfFolds=10;
    int numInstances=239;
    int fold;
    FilteredClassifier fc;

    public MultiLayerPerceptron(){}
    
    public double crossValidate(int offset){
        double foldPctCorrect=0;


        int p=0;
        int startingPoint=0;
        fold=1;

        do{
            p++;
            FileReader trainreader = null;
            try {
                //retrive the training instances from the file
                trainreader = new FileReader("src/main/resources/PerceptronInputs/PastMatchups.arff");
            train = new Instances(trainreader);
                train.sort(0);//sort by matchup id --group all instances of the same matchup together

                //below is used to remove records from the data set in order to determine the learning curve
                for(int i=0;i<offset;i++){
                    train.delete(train.numInstances()-1);
                }
            numInstances=train.numInstances();

                //below will find the best size for each test set based on the amount of records
                testSize=(numInstances-(((numInstances/2)%numberOfFolds)*2))/numberOfFolds;

                //below will  create a test set from the total/training set at a specified index
                // and removes said test set from the training set
            Instances test=new Instances(train,  startingPoint,  testSize);
                //remove test instances from training set
            for (int i=0;i<testSize;i++){
                train.delete(startingPoint);
            }

                //trains on one set ,tests on another and
                // returns the % of matchups predicted correctly on the test set
            foldPctCorrect+=trainAndTest(train,test);
            startingPoint+=testSize;
                fold++;


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }while (startingPoint<=numInstances-testSize);

        //save the total % of accuracy from the above cross fold validation to be used as a learning curve point
        LearningCurveResult learningCurveResult=new LearningCurveResult();
        double accuracy = Math.round((foldPctCorrect/p) * 100.0) / 100.0;

        learningCurveResult.setAccuracy(accuracy); //average of 10 folds
        learningCurveResult.setNumberOfFights(numInstances/2);
        learningCurveResultRepository.save(learningCurveResult);

        System.out.println("full amt %: "+(foldPctCorrect/p)+"%");

        return (foldPctCorrect/fold);//average accuracy out of 10 folds
    }

    public float trainAndTest(Instances train,Instances test)
    {
        try{
            setPerceptronConfiguration();

            //build the model on the training set --this is where the backpropagation, updating of weights etc occurs
            fc.buildClassifier(train);

            //from here the model is trained and can be used to clssify unseen instances

            //sort by matchupId
            train.sort(train.attribute(0));
            //train the perceptron
            int numCorrect=trainPerceptron(train,fc);

            //evauluate training data
            System.out.println("fold:"+fold+" train set CORRECT"+numCorrect+" TOTAL: "+train.numInstances()/2+" PCT:"+(numCorrect/((train.size()/2)/100.0f)));


System.out.println("--------------------------------TEST SET---------------------------------------------");
            test.setClassIndex(test.numAttributes()-1);

            //Predict Part
            numCorrect=0;
            test.sort(test.attribute(0));//sort all records based on their matchup id
            for (int i = 0; i < test.numInstances(); i=i+2) {//
                double pred = fc.classifyInstance(test.instance(i));
                if(i+1!=test.size())//avoid out of bounds
                    if (test.get(i).value(0)==test.get(i+1).value(0)) {//2 records of same matchup
                        //is the loser 
                        if(test.get(i).classValue()==0) {//the instance we are looking at is the loser
                            //was this instance predicted with a lower class value than the other instance
                            if(fc.classifyInstance(test.instance(i))<fc.classifyInstance(test.instance(i+1))) {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                //pass the predicted winner instance
                                savePrediction(test.instance(i+1),true);
                            }else {
                                savePrediction(test.instance(i),false);
                            }
                        }
                        else {//is of class 1 winner
                            if(fc.classifyInstance(test.instance(i))>fc.classifyInstance(test.instance(i+1)))
                            //was this instance predicted with a higher class value than the other instance
                            {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                savePrediction(test.instance(i),true);
                            }
                            else {//if it wasnt then the prediction is wrong
                                savePrediction(test.instance(i+1),false);
                            }
                        }
                    }
            }
            float accuracy=(numCorrect/((test.size()/2)/100.0f));
            System.out.println("test set correct "+numCorrect+"/"+test.size()/2+" pct: "+accuracy);

            //save the result
            FoldResult foldResult=new FoldResult();
            foldResult.setAccuracy(accuracy);
            foldResultRepository.save(foldResult);
            System.out.println("test size"+testSize);


            return (numCorrect/((test.size()/2)/100.0f));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }

    private void setPerceptronConfiguration() throws Exception {
        // filter
        Remove rm = new Remove();
        //remove fighter and matchup id
        rm.setAttributeIndices("1,2");
        mlp = new MultilayerPerceptron();

        //L-LEARNING RATE, M-MOMENTUM H-HIDDEN LAYER
        mlp.setOptions(Utils.splitOptions(" -L 0.15 -M 0.1 -N 500 -V 0 -S 0 -E 20 -H \"5\" -R"));
        Attribute clas=train.attribute(train.numAttributes()-1); //win/lose class
        train.setClass(clas);
        // meta-classifier
        fc = new FilteredClassifier();
        fc.setFilter(rm); //remove the ids from the input nodes
        fc.setClassifier(mlp);
    }

    private int trainPerceptron(Instances train, FilteredClassifier fc) throws Exception {
        int numCorrect=0;

        for (int i = 0; i < train.numInstances(); i++) {

            if(i+1!=train.size())//avoid out of bounds
                if (train.get(i).value(0)==train.get(i+1).value(0)) {//two records of the same matchup id
                    if(train.get(i).classValue()==0) {//the first instance is a loser
                        if(fc.classifyInstance(train.instance(i))<fc.classifyInstance(train.instance(i+1))) //was the ouput lower than the other record of same id (the winner)
                        {
                            numCorrect++;
                        }
                    }
                    else {//the first instance is a winner
                        if (fc.classifyInstance(train.instance(i)) > fc.classifyInstance(train.instance(i + 1)))//was the ouput higher than the other record of same id (the loser)
                        {
                            numCorrect++;
                        }
                    }
                }
        }
        return numCorrect;
    }

    public void PredictUpcoming(){
        //rebuild perceptron on full data set and test on future matchups
        try {

            //USE FULL SET OF PAST MATCHUPS FOR TRAINING
            FileReader trainreader = new FileReader("src/main/resources/PerceptronInputs/PastMatchups.arff");
            train = new Instances(trainreader);
            numInstances=train.numInstances();
            train.sort(0);

            FileReader futureMatchups = new FileReader("src/main/resources/PerceptronInputs/FutureMatchups.arff");
            Instances matchupsToPredict = new Instances(futureMatchups);
            matchupsToPredict.sort(0);

            //build perceptron on full training set
            setPerceptronConfiguration();
            fc.buildClassifier(train);

            matchupsToPredict.setClassIndex(matchupsToPredict.numAttributes()-1);

            for(int i=0;i<matchupsToPredict.numInstances();i+=2){

                int matchupId= (int) matchupsToPredict.instance(i).value(0);
                double pred=fc.classifyInstance(matchupsToPredict.instance(i));//FIGHER 1 IN MATCHUPS CHANCE OF WINNING
                double pred2=fc.classifyInstance(matchupsToPredict.instance(i+1));//FIGHTER 2 IN MATCHUP
                System.out.println("matchupId: "+matchupId+"first matchup predicted"+pred+" fighterId"+matchupsToPredict.instance(i).value(1));
                System.out.println("matchupId: "+matchupId+"first matchup predicted"+pred2+" fighterId"+matchupsToPredict.instance(i+1).value(1));
                Prediction p;
                Matchup m=matchupRepository.findOne(matchupId);

                //below does not need to set predicted winnner labels as we will always have a fighter profile for upcoming matchups
                if(pred>pred2){
                    p=new Prediction(matchupRepository.findOne(matchupId),fighterRepository.findOne(((int) matchupsToPredict.instance(i).value(1)))
                            ,false);
                }
                else
                {
                     p=new Prediction(matchupRepository.findOne(matchupId),fighterRepository.findOne(((int) matchupsToPredict.instance(i+1).value(1)))
                            ,false);
                }
                predictionRepository.save(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void savePrediction(Instance instance,boolean wasCorrect) {
        Prediction prediction=new Prediction();
        int matchupId= (int) instance.value(0);
        int fighterId= (int) instance.value(1);//PREDICTED winner
        Matchup matchup =matchupRepository.findOne(matchupId);
        Fighter fighter=fighterRepository.findOne(fighterId);

        prediction.setMatchup(matchupRepository.findOne(matchupId));
        String label=matchup.fighter1_first_name+' '+matchup.getFighter1_last_name()+" vs "+matchup.fighter2_first_name+' '+matchup.getFighter2_last_name();

        //for some matchups we will not have a fighter profile and therefore cannot set the winner
        //insted add a label of the predicted winners first name
        String predictedWinner=matchupRepository.findPredictedWinnerLabel(fighterId);

        prediction.setPredictedWinnerName(predictedWinner);
        prediction.setCorrect(wasCorrect);
        prediction.setWinner(fighter);
        prediction.setLabel(label);
        predictionRepository.save(prediction);
    }


}
