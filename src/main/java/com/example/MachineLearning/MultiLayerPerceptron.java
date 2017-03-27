package com.example.MachineLearning;

import com.example.DAO.FighterRepository;
import com.example.DAO.FoldResultRepository;
import com.example.DAO.MatchupRepository;
import com.example.DAO.PredictionRepository;
import com.example.Entity.Fighter;
import com.example.Entity.FoldResult;
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


    MultilayerPerceptron mlp;
    Instances train;
    int totalCorrect=0;
    int testSize=0;
    int numberOfFolds=10;
    int numInstances=239;
    int fold=1;
    
    FilteredClassifier fc;

    public MultiLayerPerceptron(){}
    
    public void crossValidate(){
        predictionRepository.deleteAll();
        foldResultRepository.deleteAll();
        double pctCorrect=0;
        int p=0;
        int startingPoint=0;
        do{
            p++;
            FileReader trainreader = null;
            try {
                trainreader = new FileReader("src/main/resources/PerceptronInputs/PastMatchups.arff");

            train = new Instances(trainreader);
            numInstances=train.numInstances();
            train.sort(0);
                //add the modulus to the first fold if not an even split
                if(fold==10){
//                    testSize=(numInstances/numberOfFolds)+(numInstances%numberOfFolds);
                    testSize=178+16;
                }
                else {
//                    testSize=(numInstances/numberOfFolds);
                    testSize=178;
                }

            //below will  create a test set from the total/training set at a specified index and remove said test set from the training set
            Instances test=new Instances(train,  startingPoint,  testSize);
                //remove test instances from training set
            for (int i=0;i<testSize;i++){
                train.delete(startingPoint);
            }

            fold++;
            pctCorrect+=trainAndTest(train,test);
            startingPoint+=testSize;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }while (startingPoint<=numInstances-testSize);

        System.out.println("full amt %: "+(pctCorrect/p)+"%");
    }

    public float trainAndTest(Instances train,Instances test)
    {
        try{
            buildPerceptronModel();
            //build on training se

            fc.buildClassifier(train);

            //sort by matchupId
            train.sort(train.attribute(0));
            //train the perceptron
            int numCorrect=trainPerceptron(train,fc);

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
//             Attribute clas2=test.attribute(15);
//            test.setClass(clas2);
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
                            if(fc.classifyInstance(test.instance(i))<fc.classifyInstance(test.instance(i+1))) {
                                numCorrect++;
                                totalCorrect+=numCorrect;
                                //pass the winer instance
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

    private void buildPerceptronModel() throws Exception {
        // filter
        Remove rm = new Remove();
        //remove fighter and matchup id
        rm.setAttributeIndices("1,2");
        // classifier1
        mlp = new MultilayerPerceptron();
//        mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.75
//        mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.125 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.09
//        mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.75
//                mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.75 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.42
//                mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //61.2
//                mlp.setOptions(Utils.splitOptions(" -L 0.425 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k=10 57.3
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k=10 57.08
//                mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k=10 59.81
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"7\" -R")); //k=10 58.82
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"7\" -R")); //k-10 56
//                mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 58
//                mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 58
//                mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 58.76
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 58.75
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 59.44
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 59.98
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 59.75
//                mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 60.18
                mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //k-10 60.18


//                mlp.setOptions(Utils.splitOptions(" -L 0.2 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //58
//                mlp.setOptions(Utils.splitOptions(" -L 0.1 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //57.4
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //56.82
//                mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //57
//                mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //56
//                mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.5k=15
//                mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //57.5 k=15

//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.075 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.42
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //61.2
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.09 -70s and some 40s
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.125 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.46
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));
//        mlp.setOptions(Utils.splitOptions(" -L 0.2 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"6\" -R"));//60
//        mlp.setOptions(Utils.splitOptions(" -L 0.1 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"6\" -R"));59.4
//        mlp.setOptions(Utils.splitOptions(" -L 0.2 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"6\" -R"));58
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));//59.75
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));//61.315

//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R"));//59.53
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //593
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.075 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.91
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.125 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.31
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.75
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.75

//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.19
//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.2
//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.75
//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.17
//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.3 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.86
//        mlp.setOptions(Utils.splitOptions(" -L 0.35 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.5


//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"6\" -R")); //59.05
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"7,2\" -R")); //59.75
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"7,3\" -R")); //60.91
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"6,3\" -R")); //59.17
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,3\" -R")); //60.64
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,4\" -R")); //61.09
//        mlp.setOptions(Utils.splitOptions(" -L 0.4 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,4\" -R")); //60.08
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,4\" -R")); //61.31
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,4,1\" -R")); //60.2
//        mlp.setOptions(Utils.splitOptions(" -L 0.45 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,4,2\" -R")); //59.86

//        mlp.setOptions(Utils.splitOptions(" -L 0.25 -M 0.175 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.42
//        mlp.setOptions(Utils.splitOptions(" -L 0.25 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //58
//        mlp.setOptions(Utils.splitOptions(" -L 0.25 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59
//        mlp.setOptions(Utils.splitOptions(" -L 0.25 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.3

//        mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.1 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //58
//        mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.15 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.65
//        mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.2 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //60.5
//        mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.25 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //58.8
//        mlp.setOptions(Utils.splitOptions(" -L 0.3 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.3

//        mlp.setOptions(Utils.splitOptions(" -L 0.2 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //59.3
//        mlp.setOptions(Utils.splitOptions(" -L 0.1 -M 0.05 -N 4000 -V 0 -S 0 -E 20 -H \"8,2\" -R")); //58.19





        Attribute clas=train.attribute(15); //275 l
        train.setClass(clas);
        // meta-classifier
        fc = new FilteredClassifier();
        fc.setFilter(rm);
        fc.setClassifier(mlp);
    }

    private int trainPerceptron(Instances train, FilteredClassifier fc) throws Exception {
        int numCorrect=0;

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
        return numCorrect;
    }

    public void PredictUpcoming(){
        //rebuild perceptron on full data set and test on future matchups
        try {

            FileReader trainreader = new FileReader("src/main/resources/PerceptronInputs/PastMatchups.arff");
            train = new Instances(trainreader);
            numInstances=train.numInstances();
            train.sort(0);

            FileReader futureMatchups = new FileReader("src/main/resources/PerceptronInputs/FutureMatchups.arff");
            Instances matchupsToPredict = new Instances(futureMatchups);
            matchupsToPredict.sort(0);

            //build perceptron on full training set
            buildPerceptronModel();
            fc.buildClassifier(train);

            matchupsToPredict.setClassIndex(matchupsToPredict.numAttributes()-1);

            for(int i=0;i<matchupsToPredict.numInstances();i+=2){

                int matchupId= (int) matchupsToPredict.instance(i).value(0);
                double pred=fc.classifyInstance(matchupsToPredict.instance(i));
                double pred2=fc.classifyInstance(matchupsToPredict.instance(i+1));
                System.out.println("matchupId: "+matchupId+"first matchup predicted"+pred+" fighterId"+matchupsToPredict.instance(i).value(1));
                System.out.println("matchupId: "+matchupId+"first matchup predicted"+pred2+" fighterId"+matchupsToPredict.instance(i+1).value(1));
                Prediction p;
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


    ///need to set the id to be that of the predcted winner
    private void savePrediction(Instance instance,boolean wasCorrect) {
        Prediction prediction=new Prediction();
        int matchupId= (int) instance.value(0);
        int fighterId= (int) instance.value(1);//actual winner
        Matchup matchup =matchupRepository.findOne(matchupId);
        Fighter fighter=fighterRepository.findOne(fighterId);

        prediction.setMatchup(matchupRepository.findOne(matchupId));
//        String label=matchup.fighter1_first_name+' '+matchup.getFighter1_last_name()+" vs "+matchup.fighter2_first_name+' '+matchup.getFighter2_last_name();
        prediction.setCorrect(wasCorrect);
        prediction.setWinner(fighter);
        predictionRepository.save(prediction);
    }


}
